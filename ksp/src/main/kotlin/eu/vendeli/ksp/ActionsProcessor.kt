@file:Suppress("UnnecessaryVariable")

package eu.vendeli.ksp

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import eu.vendeli.tgbot.annotations.AutoWiring
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.RegexCommandHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.interfaces.Autowiring

class ActionsProcessor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val commandHandlerSymbols = resolver.getSymbolsWithAnnotation(CommandHandler::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()
        val inputHandlerSymbols = resolver.getSymbolsWithAnnotation(InputHandler::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()
        val regexHandlerSymbols = resolver.getSymbolsWithAnnotation(RegexCommandHandler::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()
        val unprocessedHandlerSymbol = resolver.getSymbolsWithAnnotation(UnprocessedHandler::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>().firstOrNull()
        val updateHandlerSymbols = resolver.getSymbolsWithAnnotation(UpdateHandler::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()

        val autowiringTypes = resolver.getSymbolsWithAnnotation(
            AutoWiring::class.qualifiedName!!,
        ).filterIsInstance<KSClassDeclaration>().filter { obj ->
            Autowiring::class.asTypeName() in obj.getAllSuperTypes().map { it.toClassName() }
        }.associate { c ->
            c.annotations.first {
                it.shortName.asString() == "AutoWiring"
            }.arguments.first().value.cast<KSType>().toTypeName() to c.toClassName()
        }

        val fileSpec = FileSpec.builder("eu.vendeli.tgbot", "ActionsData").apply {
            addSuppressions()
            addImport("eu.vendeli.tgbot.utils", "InvocationLambda", "Invocable")
            addImport("eu.vendeli.tgbot.types.internal", "InvocationMeta")
            addImport("eu.vendeli.tgbot.types.internal.configuration", "RateLimits")

            addSuspendCallFun()

            collectActions(
                logger = logger,
                autowiringTypes = autowiringTypes,
                commandHandlerSymbols = commandHandlerSymbols,
                inputHandlerSymbols = inputHandlerSymbols,
                regexCommandHandlerSymbols = regexHandlerSymbols,
                updateHandlerSymbols = updateHandlerSymbols,
                unprocessedHandlerSymbols = unprocessedHandlerSymbol,
            )
        }.build()

        @Suppress("SpreadOperator")
        fileSpec.writeTo(
            codeGenerator = codeGenerator,
            dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray()),
        )

        return emptyList()
    }

    @Suppress("SpellCheckingInspection")
    private fun FileSpec.Builder.addSuppressions() {
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

    private fun FileSpec.Builder.addSuspendCallFun() = addFunction(
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
