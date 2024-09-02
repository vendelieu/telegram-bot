package eu.vendeli.ksp

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.ksp.dto.CollectorsContext
import eu.vendeli.ksp.utils.ChainingStrategyDefault
import eu.vendeli.ksp.utils.cast
import eu.vendeli.ksp.utils.linkQName
import eu.vendeli.tgbot.annotations.InputChain
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
            val isAutoCleanChain = chain.annotations
                .first {
                    it.shortName.asString() == InputChain::class.simpleName
                }.arguments
                .firstOrNull()
                ?.value
                ?.cast<Boolean>() ?: false

            val links = chain.declarations
                .filter { i ->
                    i is KSClassDeclaration &&
                        i.getAllSuperTypes().any {
                            it.declaration.qualifiedName?.asString() == linkQName
                        }
                }.toList()
                .cast<List<KSClassDeclaration>>()

            val statefulLinks = links
                .filter { l ->
                    l
                        .getAllSuperTypes()
                        .find { it.declaration.simpleName.asString() == StatefulLink::class.simpleName } != null
                }.toHashSet()

            if (statefulLinks.isNotEmpty()) buildChainStateBindings(ctx.botCtxFile, chain, statefulLinks, logger)

            links.asSequence().forEachIndexed { idx, link ->
                val curLinkId = link.qualifiedName!!.asString()
                val qualifier = link.qualifiedName!!.getQualifier()
                val name = link.simpleName.asString()
                val reference = "$qualifier.$name"

                val chainingStrategy = link.getDeclaredProperties().find {
                    it.simpleName.asString() == "chainingStrategy"
                }?.type?.resolve()?.toTypeName() ?: ChainingStrategyDefault

                val nextLink = when {
                    chainingStrategy == ChainingStrategyDefault && idx < links.lastIndex -> {
                        links.getOrNull(idx + 1)?.qualifiedName?.asString()
                    }
                    chainingStrategy is ParameterizedTypeName -> { // linkTo
                        chainingStrategy.typeArguments.firstOrNull()?.toString()
                    }
                    else -> null // DoNothing | lastChain
                }

                logger.warn(nextLink.toString())
                val isStatefulLink = link in statefulLinks

                val block = buildCodeBlock {
                    indent()
                    beginControlFlow("suspendCall { classManager, update, user, bot, parameters ->")
                        .apply {
                            add("if(user == null) return@suspendCall Unit\n")
                            add("val inst = classManager.getInstance<$reference>()!!\n")
                            add("inst.beforeAction?.invoke(user, update, bot)\n")

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
                            if (nextLink != null) add("bot.inputListener[user] = %P\n", nextLink)

                            if (idx == links.lastIndex && isAutoCleanChain) {
                                add("\n// clearing state\n")
                                statefulLinks.forEachIndexed { stateIdx, statefulLink ->
                                    val keyParam = "stateKey$stateIdx"
                                    add(
                                        "val inst$stateIdx = classManager.getInstance<${statefulLink.qualifiedName!!.asString()}>()!!\n",
                                    )
                                    add("val $keyParam = inst$stateIdx.state.stateKey.select(update)\n")
                                    add("if($keyParam != null)\n")
                                    add("inst$stateIdx.state.del($keyParam)\n\n")
                                }
                            }
                            add("inst.afterAction?.invoke(user, update, bot)\n")
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
}
