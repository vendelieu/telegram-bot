package eu.vendeli.ktnip.collectors

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.ktnip.dto.CollectorsContext
import eu.vendeli.ktnip.utils.*
import eu.vendeli.tgbot.annotations.InputChain
import eu.vendeli.tgbot.types.chain.StatefulLink
import eu.vendeli.ktnip.utils.buildChainStateBindings
import eu.vendeli.ktnip.utils.TypeConstants.ChainingStrategyDefault
import eu.vendeli.ktnip.utils.TypeConstants.linkQName

internal class InputChainCollector : Collector {
    override fun collect(resolver: Resolver, ctx: CollectorsContext) {
        val symbols = resolver.getAnnotatedClassSymbols(InputChain::class, ctx.pkg)
        if (!symbols.iterator().hasNext()) {
            ctx.logger.info("No input chains were found.")
            return
        }
        ctx.logger.info("Collecting input chains.")

        symbols.forEach { chain ->
            val isAutoCleanChain = chain.annotations
                .findAnnotationRecursively(InputChain::class)
                ?.arguments
                ?.firstOrNull()
                ?.value
                ?.cast<Boolean>() == true

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

            if (statefulLinks.isNotEmpty()) buildChainStateBindings(ctx.botCtxFile, chain, statefulLinks, ctx.logger)

            links.asSequence().forEachIndexed { idx, link ->
                val curLinkId = link.qualifiedName!!.asString()
                val qualifier = link.qualifiedName!!.getQualifier()
                val name = link.simpleName.asString()
                val reference = "$qualifier.$name"

                val chainingStrategy = link
                    .getDeclaredProperties()
                    .find {
                        it.simpleName.asString() == "chainingStrategy"
                    }?.type
                    ?.resolve()
                    ?.toTypeName() ?: ChainingStrategyDefault

                val nextLink = when {
                    chainingStrategy == ChainingStrategyDefault && idx < links.lastIndex -> {
                        links.getOrNull(idx + 1)?.qualifiedName?.asString()
                    }

                    chainingStrategy is ParameterizedTypeName -> { // linkTo
                        chainingStrategy.typeArguments.firstOrNull()?.toString()
                    }

                    else -> null // DoNothing | lastChain
                }

                val isStatefulLink = link in statefulLinks
                val activityId = link.getActivityId()
                val objectName = link.getActivityObjectName()

                val activityObject = TypeSpec.objectBuilder(objectName)
                    .addSuperinterface(ClassName("eu.vendeli.tgbot.core", "Activity"))
                    .addModifiers(KModifier.INTERNAL)
                    .addProperty(
                        PropertySpec.builder("id", INT, KModifier.OVERRIDE)
                            .initializer("%L", activityId)
                            .build(),
                    )
                    .addProperty(
                        PropertySpec.builder("qualifier", STRING, KModifier.OVERRIDE)
                            .initializer("%S", qualifier)
                            .build(),
                    )
                    .addProperty(
                        PropertySpec.builder("function", STRING, KModifier.OVERRIDE)
                            .initializer("%S", name)
                            .build(),
                    )

                val invokeFun = FunSpec.builder("invoke")
                    .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
                    .addParameter(INVOCATION_LAMBDA_PARAMS, ClassName("eu.vendeli.tgbot.types.component", "ProcessingContext"))
                    .returns(Any::class.asTypeName().copy(nullable = true))
                    .addCode(
                        buildCodeBlock {
                            beginControlFlow("return context.run {")
                            add("val bot = context.bot\n")
                            add("val update = context.update\n")
                            ctx.activitiesFile.addImport("eu.vendeli.tgbot.types.component", "userOrNull")
                            add("val user = update.userOrNull\n")
                            add("if(user == null) return@run Unit\n")
                            add("val inst = bot.config.classManager.getInstance<$reference>()!!\n")
                            add("inst.beforeAction?.invoke(user, update, bot)\n")

                            add("val breakPoint = inst.breakCondition?.invoke(user, update, bot) == true\n")
                            add(
                                "if (breakPoint && inst.retryAfterBreak){\n\tbot.inputListener[user] = \"%L\"\n}\n",
                                curLinkId,
                            )
                            add("if (breakPoint) {\n\tinst.breakAction(user, update, bot)\n\treturn@run Unit\n}\n")
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
                                        "val inst$stateIdx = bot.config.classManager.getInstance<${statefulLink.qualifiedName!!.asString()}>()!!\n",
                                    )
                                    add("val $keyParam = inst$stateIdx.state.stateKey.select(update)\n")
                                    add("if($keyParam != null)\n")
                                    add("inst$stateIdx.state.del($keyParam)\n\n")
                                }
                            }
                            add("inst.afterAction?.invoke(user, update, bot)\n")
                            endControlFlow()
                        },
                    )

                activityObject.addFunction(invokeFun.build())
                ctx.activitiesFile.addType(activityObject.build())

                ctx.loadFun.addStatement("registerActivity(%N)", objectName)
                ctx.loadFun.addStatement("registerInput(%S, %N.id)", curLinkId, objectName)
            }
        }
    }
}
