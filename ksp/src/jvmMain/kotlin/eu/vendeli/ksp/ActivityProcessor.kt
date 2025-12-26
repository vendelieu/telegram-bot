@file:Suppress("UnnecessaryVariable")

package eu.vendeli.ksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.writeTo
import eu.vendeli.ksp.dto.CollectorsContext
import eu.vendeli.ksp.utils.*
import eu.vendeli.ksp.collectors.*
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.annotations.internal.KtGramInternal

class ActivityProcessor(
    options: Map<String, String>,
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    private var isProcessed = false
    private val targetPackage = options["package"]?.split(';')
    private val autoAnswerCallback = options["autoAnswerCallback"]?.toBooleanStrictOrNull() == true

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isProcessed) return emptyList()

        val activitiesFile = FileSpec.builder("eu.vendeli.tgbot.generated", "ActivitiesData").apply {
            addSuppressions()
            addOptIn()

            addImport("eu.vendeli.tgbot.core", "Activity")
            addImport("eu.vendeli.tgbot.types.component", "ProcessingContext", "UpdateType", "CommonMatcher", "userOrNull")
            addImport("eu.vendeli.tgbot.types.configuration", "RateLimits")
            addImport("eu.vendeli.tgbot.implementations", "DefaultGuard", "DefaultArgParser")

            addImport("eu.vendeli.tgbot.utils.common", "getInstance")
        }

        val loaders = mutableListOf<TypeSpec>()
        val packages = targetPackage ?: listOf(null)
        val isMultiPackage = packages.size > 1

        packages.forEachIndexed { idx, pkg ->
            if (pkg != null && pkg.isBlank()) logger.error("Defined package #$idx is blank.")

            val className = if (isMultiPackage && pkg != null) {
                pkg.split('.').joinToString("") { it.replaceFirstChar { c -> c.uppercaseChar() } } + "Loader"
            } else {
                "KtGramCtxLoader"
            }

            val filePkg = pkg ?: "eu.vendeli.tgbot.generated"

            val loadFun = FunSpec.builder("load")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("bot", ClassName("eu.vendeli.tgbot", "TelegramBot"))
                .addCode("return bot.update.registry.run {\n")
                .addStatement("bot.update.____ctxUtils = %T", ClassName(filePkg, "__CtxUtils"))

            processPackage(activitiesFile, loadFun, resolver, if (pkg != null) idx to pkg else null)

            loadFun.addCode("}\n")

            loaders.add(
                TypeSpec.classBuilder(className)
                    .addSuperinterface(ClassName("eu.vendeli.tgbot.interfaces.helper", "ContextLoader"))
                    .apply {
                        if (isMultiPackage && pkg != null) {
                            addProperty(
                                PropertySpec.builder("pkg", String::class, KModifier.OVERRIDE)
                                    .initializer("%S", pkg)
                                    .build(),
                            )
                        }
                    }
                    .addFunction(loadFun.build())
                    .build(),
            )
        }

        val loaderFile = FileSpec.builder("eu.vendeli.tgbot.generated", "KtGramCtxLoader").apply {
            addSuppressions()
            addOptIn()

            addImport("eu.vendeli.tgbot.types.component", "CommonMatcher")
            addImport("eu.vendeli.tgbot.interfaces.helper", "ContextLoader")
            addImport("eu.vendeli.tgbot", "TelegramBot")
            addImport("eu.vendeli.tgbot.core", "ActivityRegistry")
            addImport("eu.vendeli.tgbot.types.component", "UpdateType")

            loaders.forEach { addType(it) }
        }

        @Suppress("SpreadOperator")
        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())
        activitiesFile.build().runCatching {
            writeTo(
                codeGenerator = codeGenerator,
                dependencies = dependencies,
            )
        }
        loaderFile.build().runCatching {
            writeTo(
                codeGenerator = codeGenerator,
                dependencies = dependencies,
            )
        }

        codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName = "META-INF.services",
            fileName = "eu.vendeli.tgbot.interfaces.helper.ContextLoader",
            "",
        ).bufferedWriter().use { writer ->
            loaders.forEach {
                writer.write("eu.vendeli.tgbot.generated.${it.name}\n")
            }
        }

        isProcessed = true
        return emptyList()
    }

    private fun processPackage(
        fileSpec: FileBuilder,
        loadFun: FunSpec.Builder,
        resolver: Resolver,
        target: Pair<Int, String>? = null,
    ) {
        val pkg = target?.second
        val filePkg = pkg ?: "eu.vendeli.tgbot.generated"

        val botCtxSpec = FileSpec.builder(filePkg, "BotCtx")

        val injectableTypes = resolver.getInjectableTypes(pkg)

        val context = CollectorsContext(
            activitiesFile = fileSpec,
            botCtxFile = botCtxSpec,
            injectableTypes = injectableTypes,
            logger = logger,
            loadFun = loadFun,
            pkg = filePkg,
            autoAnswerCallback = autoAnswerCallback,
        )

        val collectors = listOf(
            BotCtxCollector(),
            CommandCollector(),
            InputCollector(),
            InputChainCollector(),
            CommonCollector(),
            UpdateHandlerCollector(),
            UnprocessedHandlerCollector(),
        )

        collectors.forEach { it.collect(resolver, context) }

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
        val internalAnnoName = KtGramInternal::class.simpleName!!
        addImport("eu.vendeli.tgbot.annotations.internal", internalAnnoName)

        val experimentalAnnoName = ExperimentalFeature::class.simpleName!!
        addImport("eu.vendeli.tgbot.annotations.internal", experimentalAnnoName)

        addAnnotation(
            AnnotationSpec
                .builder(ClassName("kotlin", "OptIn"))
                .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                .addMember("$internalAnnoName::class")
                .addMember("$experimentalAnnoName::class")
                .build(),
        )
    }
}
