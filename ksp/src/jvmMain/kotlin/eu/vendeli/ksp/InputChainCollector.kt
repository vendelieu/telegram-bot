package eu.vendeli.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toTypeName

internal fun collectInputChains(
    symbols: Sequence<KSClassDeclaration>,
    logger: KSPLogger,
): CodeBlock? = buildCodeBlock {
    if (!symbols.iterator().hasNext()) {
        logger.info("No input chains were found.")
        return null
    }
    symbols.forEach { chain ->
        val links = chain.declarations.filter { i ->
            i is KSClassDeclaration && i.superTypes.any { it.toTypeName() == chainLinkClass }
        }.toList().cast<List<KSClassDeclaration>>()

        links.asSequence().forEachIndexed { idx, c ->
            val curLinkId = c.qualifiedName!!.asString()
            val qualifier = c.qualifiedName!!.getQualifier()
            val name = c.simpleName.asString()
            val reference = "$qualifier.$name"

            val nextLink = if (idx < links.lastIndex) {
                links.getOrNull(idx + 1)
            } else {
                null
            }?.qualifiedName?.asString()

            val block = buildCodeBlock {
                indent()
                beginControlFlow("suspendCall { classManager, update, user, bot, parameters ->").apply {
                    add("if(user == null) return@suspendCall Unit\n")
                    add("val inst = classManager.getInstance($reference::class) as $reference\n")
                    add("val nextLink: String? = %P\n", nextLink)
                    add("val breakPoint = inst.breakCondition?.invoke(user, update, bot) ?: false\n")
                    add(
                        "if (breakPoint && inst.retryAfterBreak) bot.inputListener[user] = \"%L\"\n",
                        curLinkId,
                    )
                    add("if (breakPoint) {\ninst.breakAction(user, update, bot)\nreturn@suspendCall Unit\n}\n")

                    add("inst.action(user, update, bot)")
                    add(".also {\nif (nextLink != null) bot.inputListener[user] = nextLink }\n")
                }.endControlFlow()
            }

            add(
                "\"$curLinkId\" to (%L to InvocationMeta(\"%L\", \"%L\", %L)),\n",
                block,
                qualifier,
                name,
                "zeroRateLimits",
            )
        }
    }
}
