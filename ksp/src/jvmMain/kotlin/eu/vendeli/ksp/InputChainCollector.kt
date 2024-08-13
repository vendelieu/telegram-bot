package eu.vendeli.ksp

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.ksp.utils.FileBuilder
import eu.vendeli.ksp.utils.cast
import eu.vendeli.ksp.utils.chainLinkClass
import eu.vendeli.ksp.utils.stateManager
import eu.vendeli.tgbot.implementations.DefaultGuard

internal fun FileBuilder.collectInputChains(
    symbols: Sequence<KSClassDeclaration>,
    logger: KSPLogger,
    pkg: String? = null,
): CodeBlock? = buildCodeBlock {
    if (!symbols.iterator().hasNext()) {
        logger.info("No input chains were found.")
        return null
    }
    symbols.forEach { chain ->
        val links = chain.declarations
            .filter { i ->
                i is KSClassDeclaration && i.getAllSuperTypes().any { it.toTypeName() == chainLinkClass }
            }.toList()
            .cast<List<KSClassDeclaration>>()

        val isStateManagerChain = chain.getAllSuperTypes().firstOrNull {
            it.toClassName() == stateManager
        } != null

        if (isStateManagerChain) {
            addImport("eu.vendeli.tgbot.types.internal", "StoredState")
        }

        links.asSequence().forEachIndexed { idx, link ->
            val curLinkId = link.qualifiedName!!.asString()
            val qualifier = link.qualifiedName!!.getQualifier()
            val name = link.simpleName.asString()
            val reference = "$qualifier.$name"

            val nextLink = if (idx < links.lastIndex) {
                links.getOrNull(idx + 1)
            } else {
                null
            }?.qualifiedName?.asString()

            val block = buildCodeBlock {
                indent()
                beginControlFlow("suspendCall { classManager, update, user, bot, parameters ->")
                    .apply {
                        add("if(user == null) return@suspendCall Unit\n")
                        add("val inst = classManager.getInstance($reference::class) as $reference\n")
                        add("inst.beforeAction?.invoke(user, update, bot)\n")
                        add("val nextLink: String? = %P\n", nextLink)
                        add("val breakPoint = inst.breakCondition?.invoke(user, update, bot) ?: false\n")
                        add(
                            "if (breakPoint && inst.retryAfterBreak){\nbot.inputListener[user] = \"%L\"\n}\n",
                            curLinkId,
                        )
                        add("if (breakPoint) {\ninst.breakAction(user, update, bot)\nreturn@suspendCall Unit\n}\n")
                        if (isStateManagerChain) {
                            add("val chainInst = classManager.getInstance($qualifier::class) as $qualifier\n")
                            if (idx == 0) add("chainInst.clearAllState(user, \"$qualifier\")\n")
                            add("chainInst.setState(user, \"$reference\", StoredState(update, inst.stateSelector))\n")
                        }
                        if (pkg != null) add(
                            "if (\n\tbot.update.userClassSteps[user.id] != %S\n) %L.____clearClassData(user.id)\n",
                            qualifier,
                            pkg,
                        )
                        add("inst.action(user, update, bot)\n")
                        add("if (nextLink != null) bot.inputListener[user] = nextLink\n")
                        add("inst.afterAction?.invoke(user, update, bot)\n")
                    }.endControlFlow()
            }

            add(
                "\"$curLinkId\" to (%L to InvocationMeta(\"%L\", \"%L\", %L, %L::class)),\n",
                block,
                qualifier,
                name,
                "zeroRateLimits",
                DefaultGuard::class.qualifiedName,
            )
        }
    }
}
