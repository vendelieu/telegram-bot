@file:Suppress("UnnecessaryVariable")

package eu.vendeli.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.utils.fullName
import java.io.File

class ApiProcessor(
    internal val logger: KSPLogger,
    options: Map<String, String>,
) : SymbolProcessor {
    private val utilsDir = options["outputDir"]!!
    private val apiFile = options["apiFile"]!!

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val fileSpec = FileSpec.builder("eu.vendeli.ktgram.extutils", "TelegramBotSc")
        val apiMash = resolver.getSymbolsWithAnnotation(TgAPI::class.fullName)

        val apiClasses = apiMash.filterIsInstance<KSClassDeclaration>()
        val apiFun = apiMash.filterIsInstance<KSFunctionDeclaration>()

        fileSpec.apply {
            addAnnotation(
                AnnotationSpec
                    .builder(Suppress::class)
                    .addMember("\"KotlinRedundantDiagnosticSuppress\"")
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                    .build(),
            )
            apiFun.forEach { addShortcut(it) }
        }

        fileSpec.build().writeTo(File(utilsDir))

        validateApi(apiClasses, apiFile)

        return emptyList()
    }

    private val tgBotType = TelegramBot::class.asTypeName()
    private fun FileSpec.Builder.addShortcut(declaration: KSFunctionDeclaration) {
        val name = declaration.simpleName.getShortName()
        val parameters = declaration.parameters.mapIndexed { _, it ->
            val isFunType = it.type.resolve().isFunctionType
            when {
                isFunType -> ParameterSpec(it.name!!.getShortName(), it.type.toTypeName(), KModifier.NOINLINE)
                it.isVararg -> ParameterSpec(it.name!!.getShortName(), it.type.toTypeName(), KModifier.VARARG)
                else -> ParameterSpec(it.name!!.getShortName(), it.type.toTypeName())
            }
        }
        val parametersInlined = parameters.joinToString {
            (if (KModifier.VARARG in it.modifiers) "${it.name} = " else "") + it.name
        }

        addFunction(
            FunSpec
                .builder(name)
                .receiver(tgBotType)
                .addModifiers(KModifier.INLINE)
                .addAnnotation(AnnotationSpec.builder(Suppress::class).addMember("\"NOTHING_TO_INLINE\"").build())
                .addParameters(parameters)
                .returns(declaration.returnType!!.toTypeName())
                .addCode("return ${declaration.qualifiedName!!.asString()}($parametersInlined)")
                .build(),
        )
    }
}
