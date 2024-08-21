package eu.vendeli.ksp

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import eu.vendeli.ksp.dto.CollectorsContext
import eu.vendeli.ksp.utils.cast
import eu.vendeli.ksp.utils.linkQName
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.types.internal.chain.StatefulLink

internal fun collectInputChains(
    symbols: Sequence<KSClassDeclaration>,
    ctx: CollectorsContext,
): CodeBlock? = ctx.run {
    buildCodeBlock {
        if (!symbols.iterator().hasNext()) {
            logger.info("No input chains were found.")
            return null
        }
        symbols.forEach { chain ->
            val links = chain.declarations
                .filter { i ->
                    i is KSClassDeclaration &&
                        i.getAllSuperTypes().any {
                            it.declaration.qualifiedName?.asString() == linkQName
                        }
                }.toList()
                .cast<List<KSClassDeclaration>>()

            val statefulLinks = chain.declarations
                .filter { d ->
                    d is KSClassDeclaration &&
                        d
                            .getAllSuperTypes()
                            .find { it.declaration.simpleName.asString() == StatefulLink::class.simpleName } != null
                }.cast<Sequence<KSClassDeclaration>>()
                .toHashSet()

            if (statefulLinks.isNotEmpty()) buildChainStateBindings(ctx.botCtxFile, chain, statefulLinks, logger)

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
                val isStatefulLink = statefulLinks.contains(link)

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
                            if (pkg != null) add(
                                "if (\n\tbot.update.userClassSteps[user.id] != %S\n) %L.____clearClassData(user.id)\n",
                                qualifier,
                                pkg,
                            )
                            if (isStatefulLink) {
                                add("val linkState = inst.action(user, update, bot)\n")
                                add("val stateKey = inst.state.stateKey.select(update)\n")
                                add("if(stateKey != null) inst.state.set(stateKey, linkState)\n")
                            } else {
                                add("inst.action(user, update, bot)\n")
                            }
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
}
