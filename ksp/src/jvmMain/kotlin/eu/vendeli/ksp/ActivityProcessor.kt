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
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import eu.vendeli.ksp.dto.CollectorsContext
import eu.vendeli.ksp.utils.CommonAnnotationHandler
import eu.vendeli.ksp.utils.FileBuilder
import eu.vendeli.ksp.utils.activitiesType
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
            addOptIn()

            addImport("eu.vendeli.tgbot.utils", "InvocationLambda", "Invocable")
            addImport("eu.vendeli.tgbot.types.internal", "InvocationMeta")
            addImport("eu.vendeli.tgbot.types.internal.configuration", "RateLimits")

            addSuspendCallFun()
            addSuspendCallFun(true)
            addImport("eu.vendeli.tgbot.utils", "getInstance")
        }

        targetPackage?.forEachIndexed { idx, pkg ->
            if (pkg.isBlank()) logger.error("Defined package #$idx is blank.")
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

    private fun processPackage(fileSpec: FileBuilder, resolver: Resolver, target: Pair<Int, String>? = null) {
        val pkg = target?.second
        val idxPostfix = target?.first?.let { "$it" } ?: "0"
        val filePkg = pkg ?: "eu.vendeli.tgbot.generated"

        val botCtxSpec = FileSpec.builder(filePkg, "BotCtx")
        processCtxProviders(botCtxSpec, resolver, pkg)

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
        val context = CollectorsContext(
            activitiesFile = fileSpec,
            botCtxFile = botCtxSpec,
            injectableTypes = injectableTypes,
            logger = logger,
            idxPostfix = idxPostfix,
            pkg = filePkg,
        )

        collectCommandActivities(commandHandlerSymbols, context)
        collectInputActivities(inputHandlerSymbols, inputChainSymbols, context)
        collectCommonActivities(commonHandlerData, context)
        collectUpdateTypeActivities(updateHandlerSymbols, context)
        collectUnprocessed(unprocessedHandlerSymbol, context)

        @Suppress("SpreadOperator")
        botCtxSpec.build().runCatching {
            writeTo(
                codeGenerator = codeGenerator,
                dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray()),
            )
        }
    }

    @Suppress("SpellCheckingInspection")
    private fun FileBuilder.addSuppressions() {
        addAnnotation(
            AnnotationSpec
                .builder(Suppress::class)
                .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                .addMember(
                    "\t\n            \"NOTHING_TO_INLINE\",\n" +
                        "            \"ObjectPropertyName\",\n" +
                        "            \"UNUSED_ANONYMOUS_PARAMETER\",\n" +
                        "            \"UnnecessaryVariable\",\n" +
                        "            \"TopLevelPropertyNaming\",\n" +
                        "            \"UNNECESSARY_SAFE_CALL\",\n" +
                        "            \"RedundantNullableReturnType\",\n" +
                        "            \"KotlinConstantConditions\",\n" +
                        "            \"USELESS_ELVIS\",\n",
                ).build(),
        )
    }

    private fun FileBuilder.addOptIn() {
        addImport("eu.vendeli.tgbot.annotations.internal", "KtGramInternal")
        addAnnotation(
            AnnotationSpec
                .builder(ClassName("kotlin", "OptIn"))
                .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                .addMember("KtGramInternal::class")
                .build(),
        )
    }

    private fun FileBuilder.addSuspendCallFun(withMeta: Boolean = false) = addFunction(
        FunSpec
            .builder("suspendCall")
            .apply {
                addModifiers(KModifier.PRIVATE, KModifier.INLINE)
                if (withMeta) addParameter(
                    ParameterSpec
                        .builder(
                            "meta",
                            TypeVariableName("InvocationMeta"),
                        ).build(),
                )
                addParameter(
                    ParameterSpec
                        .builder(
                            "block",
                            TypeVariableName("InvocationLambda"),
                            KModifier.NOINLINE,
                        ).build(),
                )
                if (!withMeta) {
                    returns(TypeVariableName("InvocationLambda"))
                    addStatement("return block")
                } else {
                    returns(TypeVariableName("Invocable"))
                    addStatement("return block to meta")
                }
            }.build(),
    )
}
