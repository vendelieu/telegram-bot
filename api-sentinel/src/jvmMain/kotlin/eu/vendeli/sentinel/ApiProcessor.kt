@file:Suppress("UnnecessaryVariable")

package eu.vendeli.sentinel

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.types.component.ActivityCtx
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.utils.common.fqName
import kotlinx.serialization.json.Json
import java.io.File

class ApiProcessor(
    internal val logger: KSPLogger,
    options: Map<String, String>,
) : SymbolProcessor {
    private val tgBaseDir = options["tgBaseDir"]!!
    private val utilsDir = options["utilsDir"]!!
    private val apiFile = options["apiFile"]!!

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val fileSpec = FileSpec.builder("eu.vendeli.ktgram.extutils", "TelegramBotSc")
        val apiMash = resolver.getSymbolsWithAnnotation(TgAPI::class.fqName)

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
        resolver
            .resolveSymbolsFromDir("$tgBaseDir/api")
            .filter { i ->
                i.annotations.firstOrNull { it.shortName.getShortName() == TgAPI::class.simpleName!! } == null
            }.forEach {
                logger.invalid {
                    "${it.qualifiedName!!.asString()} not marked with @TgApi."
                }
            }

        val apiJson = Json.parseToJsonElement(File(apiFile).readText())
        validateApi(apiClasses, apiJson)

        @Suppress("UNCHECKED_CAST")
        val types = (
            resolver.resolveSymbolsFromDir("$tgBaseDir/types") {
                it is KSClassDeclaration && it.classKind != ClassKind.ENUM_CLASS
            } as List<KSClassDeclaration>
        ).asSequence()
        validateTypes(types, apiJson)

        addUpdateEvent2FDSL()

        return emptyList()
    }

    private val tgBotType = TelegramBot::class.asTypeName()
    private fun FileSpec.Builder.addShortcut(declaration: KSFunctionDeclaration) {
        val name = declaration.simpleName.getShortName()
        val isSimple = declaration.returnType
            ?.resolve()
            ?.declaration
            ?.safeCast<KSClassDeclaration>()
            ?.getAllSuperTypes()
            ?.any {
                it.declaration.qualifiedName?.asString() == simpleActionType
            } ?: false

        val parameters = declaration.parameters
            .mapIndexed { _, it ->
                val resolvedType = it.type.resolve()
                val isFunType = resolvedType.isFunctionType
                when {
                    isFunType -> ParameterSpec.builder(
                        it.name!!.getShortName(),
                        it.type.toTypeName(),
                        KModifier.NOINLINE,
                    )

                    it.isVararg -> ParameterSpec.builder(
                        it.name!!.getShortName(),
                        it.type.toTypeName(),
                        KModifier.VARARG,
                    )

                    else -> ParameterSpec.builder(it.name!!.getShortName(), it.type.toTypeName())
                }.apply {
                    if (resolvedType.isMarkedNullable) defaultValue("null")
                }.build()
            }.toMutableSet()

        if (!isSimple) parameters.add(ParameterSpec.builder("chatId", Long::class.asTypeName()).build())

        val parametersInlined = parameters.filterNot { !isSimple && it.name == "chatId" }.joinToString {
            (if (KModifier.VARARG in it.modifiers) "${it.name} = " else "") + it.name
        }

        val sendTail = if (isSimple) {
            ".send(this)"
        } else {
            ".send(chatId, this)"
        }

        addFunction(
            FunSpec
                .builder(name)
                .receiver(tgBotType)
                .addModifiers(KModifier.INLINE, KModifier.SUSPEND)
                .addAnnotation(AnnotationSpec.builder(Suppress::class).addMember("\"NOTHING_TO_INLINE\"").build())
                .addParameters(parameters)
                .returns(UNIT)
                .addCode("return ${declaration.qualifiedName!!.asString()}($parametersInlined)" + sendTail)
                .build(),
        )
    }

    private fun addUpdateEvent2FDSL() {
        FileSpec
            .builder("eu.vendeli.tgbot.utils.common", "FunctionalDSLUtils")
            .apply {
                val fdslType = FunctionalHandlingDsl::class.asTypeName()
                val blockType = ActivityCtx::class.asTypeName()
                addImport("eu.vendeli.tgbot.types.component", "UpdateType")

                UpdateType.entries.forEach { type ->
                    val nameRef = type.name
                        .lowercase()
                        .snakeToCamelCase()
                    val funName = nameRef
                        .beginWithUpperCase()

                    val pUpdateType = funName + "Update"
                    addImport("eu.vendeli.tgbot.types.component", pUpdateType)

                    val pUpdateTypeClass = ClassName("eu.vendeli.tgbot.types.component", pUpdateType)
                    val activityCtxType = blockType.parameterizedBy(pUpdateTypeClass)
                    val blockTypeRef = LambdaTypeName.get(activityCtxType, returnType = UNIT).copy(suspending = true)

                    addFunction(
                        FunSpec
                            .builder("on$funName")
                            .receiver(fdslType)
                            .addKdoc(
                                "Action that is performed on the presence of " +
                                    "[eu.vendeli.tgbot.types.common.Update.$nameRef] in the [eu.vendeli.tgbot.types.common.Update].",
                            ).addParameter(ParameterSpec.builder("block", blockTypeRef).build())
                            .addCode("functionalActivities.onUpdateActivities[$type] = block.cast()")
                            .build(),
                    )
                }
            }.build()
            .writeTo(File(tgBaseDir.replace("eu/vendeli/tgbot", "")))
    }
}
