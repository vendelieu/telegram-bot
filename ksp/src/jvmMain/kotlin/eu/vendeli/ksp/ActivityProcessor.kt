@file:Suppress("UnnecessaryVariable")

package eu.vendeli.ksp

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import eu.vendeli.ksp.utils.CommonAnnotationHandler
import eu.vendeli.ksp.utils.FileBuilder
import eu.vendeli.ksp.utils.activitiesType
import eu.vendeli.ksp.utils.addZeroLimitsProp
import eu.vendeli.ksp.utils.autoWiringClassName
import eu.vendeli.ksp.utils.getAnnotatedClassSymbols
import eu.vendeli.ksp.utils.getAnnotatedFnSymbols
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.CommandHandler.CallbackQuery
import eu.vendeli.tgbot.annotations.CommonHandler
import eu.vendeli.tgbot.annotations.Injectable
import eu.vendeli.tgbot.annotations.InputChain
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.annotations.UpdateHandler

class ActivityProcessor(
    options: Map<String, String>,
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    private val targetPackage = options["package"]?.split(';')
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val fileSpec = FileSpec.builder("eu.vendeli.tgbot.generated", "ActivitiesData").apply {
            addSuppressions()
            addImport("eu.vendeli.tgbot.utils", "InvocationLambda", "Invocable")
            addImport("eu.vendeli.tgbot.types.internal", "InvocationMeta")
            addImport("eu.vendeli.tgbot.types.internal.configuration", "RateLimits")

            addSuspendCallFun()
            addZeroLimitsProp()
        }

        targetPackage?.forEachIndexed { idx, pkg ->
            processPackage(fileSpec, resolver, idx to pkg)
        } ?: processPackage(fileSpec, resolver)

        fileSpec.apply {
            val paramInitBlock = StringBuilder()
            paramInitBlock.append("mapOf(")
            (targetPackage ?: listOf("default")).forEachIndexed { idx, pkg ->
                val block = "listOf(__TG_COMMANDS$idx, __TG_INPUTS$idx, " +
                    "  __TG_COMMONS$idx, __TG_UPDATE_TYPES$idx, __TG_UNPROCESSED$idx)"
                paramInitBlock.append("\"$pkg\" to $block,")
            }
            paramInitBlock.append(")")
            addProperty(
                PropertySpec
                    .builder(
                        "__ACTIVITIES",
                        activitiesType,
                        KModifier.INTERNAL,
                    ).apply {
                        initializer(paramInitBlock.toString())
                    }.build(),
            )
        }

        @Suppress("SpreadOperator")
        fileSpec.build().runCatching {
            writeTo(
                codeGenerator = codeGenerator,
                dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray()),
            )
        }

        return emptyList()
    }

    private fun processPackage(fileSpec: FileSpec.Builder, resolver: Resolver, target: Pair<Int, String>? = null) {
        val pkg = target?.second
        val idxPostfix = target?.first?.let { "$it" } ?: "0"

        val commandHandlerSymbols = resolver.getAnnotatedFnSymbols(pkg, CommandHandler::class, CallbackQuery::class)
        val inputHandlerSymbols = resolver.getAnnotatedFnSymbols(pkg, InputHandler::class)
        val updateHandlerSymbols = resolver.getAnnotatedFnSymbols(pkg, UpdateHandler::class)
        val unprocessedHandlerSymbol = resolver
            .getAnnotatedFnSymbols(pkg, UnprocessedHandler::class)
            .firstOrNull()

        resolver
            .getAnnotatedFnSymbols(
                pkg,
                CommonHandler.Text::class,
                CommonHandler.Regex::class,
            ).forEach { function ->
                function.annotations
                    .filter {
                        it.shortName.asString() == CommonHandler.Text::class.simpleName!! ||
                            it.shortName.asString() == CommonHandler.Regex::class.simpleName!!
                    }.forEach {
                        CommonAnnotationHandler.parse(function, it.arguments)
                    }
            }
        val commonHandlerData = CommonAnnotationHandler.collect()

        val inputChainSymbols = resolver.getAnnotatedClassSymbols(InputChain::class, pkg)

        val injectableTypes = resolver.getAnnotatedClassSymbols(Injectable::class, pkg).associate { c ->
            c
                .getAllSuperTypes()
                .first { it.toClassName() == autoWiringClassName }
                .arguments
                .first()
                .toTypeName() to
                c.toClassName()
        }
        fileSpec.apply {
            collectCommandActivities(commandHandlerSymbols, injectableTypes, logger, idxPostfix)
            collectInputActivities(inputHandlerSymbols, inputChainSymbols, injectableTypes, logger, idxPostfix)
            collectCommonActivities(commonHandlerData, injectableTypes, logger, idxPostfix)
            collectUpdateTypeActivities(updateHandlerSymbols, injectableTypes, logger, idxPostfix)
            collectUnprocessed(unprocessedHandlerSymbol, injectableTypes, logger, idxPostfix)
        }
    }

    @Suppress("SpellCheckingInspection")
    private fun FileBuilder.addSuppressions() {
        addAnnotation(
            AnnotationSpec
                .builder(Suppress::class)
                .apply {
                    addMember(
                        "\t\n            \"NOTHING_TO_INLINE\",\n" +
                            "            \"ObjectPropertyName\",\n" +
                            "            \"UNUSED_ANONYMOUS_PARAMETER\",\n" +
                            "            \"UnnecessaryVariable\",\n" +
                            "            \"TopLevelPropertyNaming\",\n" +
                            "            \"UNNECESSARY_SAFE_CALL\",\n" +
                            "            \"RedundantNullableReturnType\",\n" +
                            "            \"KotlinConstantConditions\",\n" +
                            "            \"USELESS_ELVIS\",\n",
                    )
                    useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                }.build(),
        )
    }

    private fun FileBuilder.addSuspendCallFun() = addFunction(
        FunSpec
            .builder("suspendCall")
            .apply {
                addModifiers(KModifier.PRIVATE, KModifier.INLINE)
                addParameter(
                    ParameterSpec
                        .builder(
                            "block",
                            TypeVariableName("InvocationLambda"),
                            KModifier.NOINLINE,
                        ).build(),
                )
                returns(TypeVariableName("InvocationLambda"))
                addStatement("return block")
            }.build(),
    )
}
