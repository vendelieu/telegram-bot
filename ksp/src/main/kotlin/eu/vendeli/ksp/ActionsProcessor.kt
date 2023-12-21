@file:Suppress("UnnecessaryVariable")

package eu.vendeli.ksp

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.Injectable
import eu.vendeli.tgbot.annotations.InputChain
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.RegexCommandHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.interfaces.Autowiring

class ActionsProcessor(
    options: Map<String, String>,
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    private val targetPackage = options["package"]
    override fun process(resolver: Resolver): List<KSAnnotated> {
        targetPackage?.split(';')?.forEach {
            processPackage(resolver, it)
        } ?: processPackage(resolver)

        return emptyList()
    }

    private fun processPackage(resolver: Resolver, pkg: String? = null) {
        val commandHandlerSymbols = resolver.getAnnotatedFnSymbols(CommandHandler::class, pkg)
        val inputHandlerSymbols = resolver.getAnnotatedFnSymbols(InputHandler::class, pkg)
        val regexHandlerSymbols = resolver.getAnnotatedFnSymbols(RegexCommandHandler::class, pkg)
        val updateHandlerSymbols = resolver.getAnnotatedFnSymbols(UpdateHandler::class, pkg)
        val unprocessedHandlerSymbol = resolver.getAnnotatedFnSymbols(UnprocessedHandler::class, pkg)
            .firstOrNull()

        val inputChainSymbols = resolver.getAnnotatedClassSymbols(InputChain::class, pkg)

        val injectableTypes = resolver.getAnnotatedClassSymbols(Injectable::class, pkg).filter { obj ->
            Autowiring::class.asTypeName() in obj.getAllSuperTypes().map { it.toClassName() }
        }.associate { c ->
            c.annotations.first {
                it.shortName.asString() == Injectable::class.simpleName
            }.arguments.first().value.cast<KSType>().toTypeName() to c.toClassName()
        }

        val fileSpec = FileSpec.builder("eu.vendeli.tgbot", "ActionsData".withPostfix(pkg).escape()).apply {
            addSuppressions()
            addImport("eu.vendeli.tgbot.utils", "InvocationLambda", "Invocable")
            addImport("eu.vendeli.tgbot.types.internal", "InvocationMeta")
            addImport("eu.vendeli.tgbot.types.internal.configuration", "RateLimits")

            addSuspendCallFun()

            collectCommandActions(commandHandlerSymbols, injectableTypes, logger)
            collectInputActions(inputHandlerSymbols, inputChainSymbols, injectableTypes, logger)
            collectRegexActions(regexHandlerSymbols, injectableTypes, logger)
            collectUpdateTypeActions(updateHandlerSymbols, injectableTypes, logger)
            collectUnprocessed(unprocessedHandlerSymbol, injectableTypes, logger)

            addProperty(
                PropertySpec.builder(
                    "\$ACTIONS".withPostfix(pkg).escape(),
                    List::class.asTypeName().parameterizedBy(ANY.copy(true)),
                    KModifier.INTERNAL,
                ).apply {
                    initializer(
                        "%L",
                        "listOf(`TG_\$COMMANDS`, `TG_\$INPUTS`, `TG_\$REGEX`, `TG_\$UPDATE_TYPES`, `TG_\$UNPROCESSED`)",
                    )
                }.build(),
            )
        }.build()

        @Suppress("SpreadOperator")
        fileSpec.runCatching {
            writeTo(
                codeGenerator = codeGenerator,
                dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray()),
            )
        }
    }

    @Suppress("SpellCheckingInspection")
    private fun FileBuilder.addSuppressions() {
        addAnnotation(
            AnnotationSpec.builder(Suppress::class).apply {
                addMember(
                    "\n\t\"NOTHING_TO_INLINE\", \"ObjectPropertyName\", " +
                        "\"UNUSED_ANONYMOUS_PARAMETER\", \"UnnecessaryVariable\"\n",
                )
                useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
            }.build(),
        )
    }

    private fun FileBuilder.addSuspendCallFun() = addFunction(
        FunSpec.builder("suspendCall").apply {
            addModifiers(KModifier.PRIVATE, KModifier.INLINE)
            addParameter(
                ParameterSpec.builder(
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
