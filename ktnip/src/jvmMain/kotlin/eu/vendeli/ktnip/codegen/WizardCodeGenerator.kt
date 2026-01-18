package eu.vendeli.ktnip.codegen

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import eu.vendeli.ktnip.dto.ActivityMetadata
import eu.vendeli.ktnip.utils.FileBuilder
import eu.vendeli.ktnip.utils.TypeConstants
import eu.vendeli.tgbot.types.chain.WizardActivity

/**
 * Generates wizard-related code: WizardActivity, start activity, and input activity.
 * Separates code generation logic from collection logic.
 */
class WizardCodeGenerator(
    private val fileBuilder: FileBuilder,
) {
    /**
     * Generates the WizardActivity object implementation.
     */
    fun generateWizardEngine(
        engineObjectName: String,
        activityId: Int,
        funQualifier: String,
        funShortName: String,
        stepToManagerMap: Map<KSClassDeclaration, KSClassDeclaration>,
        metadata: ActivityMetadata,
        stepsListCode: String,
    ) {
        fileBuilder.addImport("eu.vendeli.tgbot.types.chain", "WizardActivity", "WizardStep", "WizardStateManager")
        fileBuilder.addImport("eu.vendeli.tgbot", "TelegramBot")
        fileBuilder.addImport("eu.vendeli.tgbot.types", "User")
        fileBuilder.addImport("eu.vendeli.tgbot.utils.common", "getInstance")
        fileBuilder.addImport("eu.vendeli.tgbot.types.configuration", "RateLimits")
        fileBuilder.addImport("kotlin.reflect", "KClass")

        // Add import for the wizard class (to access nested steps)
        fileBuilder.addImport(funQualifier, funShortName)

        val stepsType = LIST.parameterizedBy(TypeConstants.wizardStep)
        val engineObject = TypeSpec.objectBuilder(engineObjectName)
            .addModifiers(KModifier.INTERNAL)
            .superclass(TypeConstants.wizardActivity)
            // Activity properties
            .addProperty(
                PropertySpec.builder(WizardActivity::id.name, INT, KModifier.OVERRIDE)
                    .initializer("%L", activityId)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(WizardActivity::qualifier.name, STRING, KModifier.OVERRIDE)
                    .initializer("%S", funQualifier)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(WizardActivity::function.name, STRING, KModifier.OVERRIDE)
                    .initializer("%S", funShortName)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(
                    WizardActivity::rateLimits.name,
                    TypeConstants.rateLimits,
                    KModifier.OVERRIDE,
                )
                    .initializer(
                        if (metadata.rateLimits.period > 0 || metadata.rateLimits.rate > 0) {
                            CodeBlock.of(
                                "%T(%L, %L)",
                                TypeConstants.rateLimits,
                                metadata.rateLimits.rate,
                                metadata.rateLimits.period,
                            )
                        } else {
                            CodeBlock.of(
                                "%T.NOT_LIMITED",
                                TypeConstants.rateLimits,
                            )
                        },
                    )
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(
                    WizardActivity::guardClass.name,
                    TypeConstants.kClass.parameterizedBy(
                        WildcardTypeName.producerOf(TypeConstants.guard),
                    ),
                    KModifier.OVERRIDE,
                )
                    .initializer("%T::class", ClassName.bestGuess(metadata.guardClass))
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(
                    WizardActivity::argParser.name,
                    TypeConstants.kClass.parameterizedBy(
                        WildcardTypeName.producerOf(TypeConstants.argumentParser),
                    ),
                    KModifier.OVERRIDE,
                )
                    .initializer("%T::class", ClassName.bestGuess(metadata.argParserClass))
                    .build(),
            )
            // WizardActivity properties - steps are hardcoded
            .addProperty(
                PropertySpec.builder(WizardActivity::steps.name, stepsType)
                    .addModifiers(KModifier.OVERRIDE)
                    .initializer(CodeBlock.of("%L", stepsListCode))
                    .build(),
            )
            .addFunction(
                FunSpec.builder(WizardActivity::getStateManagerForStep.name)
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter(
                        "step",
                        TypeConstants.kClass.parameterizedBy(
                            WildcardTypeName.producerOf(TypeConstants.wizardStep),
                        ),
                    )
                    .addParameter("bot", TypeConstants.botClass)
                    .returns(
                        TypeConstants.wizardStateManager.parameterizedBy(
                            WildcardTypeName.producerOf(ANY),
                        ).copy(nullable = true),
                    )
                    .addCode(
                        buildStateManagerResolutionCode(stepToManagerMap),
                    )
                    .build(),
            )
            .build()

        fileBuilder.addType(engineObject)
    }

    /**
     * Generates the start activity object that triggers the wizard.
     */
    fun generateStartActivity(
        objectName: String,
        activityId: Int,
        classQualifier: String,
        classShortName: String,
    ) {
        val startActivityObject = TypeSpec.objectBuilder("${objectName}Start")
            .addSuperinterface(TypeConstants.activity)
            .addModifiers(KModifier.INTERNAL)
            .addProperty(
                PropertySpec.builder(WizardActivity::id.name, INT, KModifier.OVERRIDE)
                    .initializer("%L", activityId)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(WizardActivity::qualifier.name, STRING, KModifier.OVERRIDE)
                    .initializer("%S", classQualifier)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(WizardActivity::function.name, STRING, KModifier.OVERRIDE)
                    .initializer("%S", classShortName)
                    .build(),
            )
            .addFunction(
                FunSpec.builder(WizardActivity::invoke.name)
                    .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
                    .addParameter("context", TypeConstants.processingCtx)
                    .returns(Any::class.asTypeName().copy(nullable = true))
                    .addCode(
                        buildStartWizardCode(activityId),
                    )
                    .build(),
            )
            .build()

        fileBuilder.addType(startActivityObject)
    }

    /**
     * Generates the input activity object that handles wizard step input.
     */
    fun generateInputActivity(
        objectName: String,
        activityId: Int,
        classQualifier: String,
        classShortName: String,
    ) {
        val inputActivityObject = TypeSpec.objectBuilder("${objectName}Input")
            .addSuperinterface(TypeConstants.activity)
            .addSuperinterface(TypeConstants.inputSelfManaging)
            .addModifiers(KModifier.INTERNAL)
            .addProperty(
                PropertySpec.builder(WizardActivity::id.name, INT, KModifier.OVERRIDE)
                    .initializer("%L", activityId + 1)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(WizardActivity::qualifier.name, STRING, KModifier.OVERRIDE)
                    .initializer("%S", classQualifier)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(WizardActivity::function.name, STRING, KModifier.OVERRIDE)
                    .initializer("%S", classShortName)
                    .build(),
            )
            .addFunction(
                FunSpec.builder(WizardActivity::invoke.name)
                    .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
                    .addParameter("context", TypeConstants.processingCtx)
                    .returns(ANY.copy(nullable = true))
                    .addCode(
                        buildWizardInputCode(activityId),
                    )
                    .build(),
            )
            .build()

        fileBuilder.addType(inputActivityObject)
    }

    private fun buildStateManagerResolutionCode(
        stepToManagerMap: Map<KSClassDeclaration, KSClassDeclaration>,
    ): CodeBlock {
        return CodeBlock.builder().apply {
            if (stepToManagerMap.isEmpty()) {
                addStatement("return null")
                return@apply
            }

            add("return when (step) {\n")
            stepToManagerMap.forEach { (stepClass, managerClass) ->
                val stepClassName = stepClass.toClassName()
                val managerClassName = managerClass.toClassName()
                fileBuilder.addImport(managerClass.packageName.asString(), managerClass.simpleName.asString())

                add("    %T::class -> {\n", stepClassName)
                add(
                    "        val manager = bot.getInstance(%T::class) as? %T<*>\n",
                    managerClassName,
                    TypeConstants.wizardStateManager,
                )
                add("        manager\n")
                add("    }\n")
            }
            add("    else -> null\n")
            add("}\n")
        }.build()
    }

    private fun buildStartWizardCode(
        activityId: Int,
    ): CodeBlock {
        return CodeBlock.builder().apply {
            fileBuilder.addImport("eu.vendeli.tgbot.types.chain", "WizardContext", "WizardActivity")
            fileBuilder.addImport("eu.vendeli.tgbot.types.component", "userOrNull")

            beginControlFlow("return context.run {")
            add("val bot = context.bot\n")
            add("val update = context.update\n")
            add("val registry = context.registry\n")
            add("val user = update.userOrNull ?: return@run Unit\n")

            // Get engine from registry
            add(
                "val wizardEngine = registry.getActivity(%L) as? %T ?: return@run Unit\n",
                activityId,
                TypeConstants.wizardActivity,
            )

            // Create wizard context
            add("val wizardCtx = WizardContext(user, update, bot)\n")

            // Start wizard (this will call onEntry of initial step and set inputListener)
            add("wizardEngine.start(wizardCtx)\n")

            endControlFlow()
        }.build()
    }

    private fun buildWizardInputCode(
        activityId: Int,
    ): CodeBlock {
        return CodeBlock.builder().apply {
            fileBuilder.addImport("eu.vendeli.tgbot.types.chain", "WizardContext", "WizardActivity")
            fileBuilder.addImport("eu.vendeli.tgbot.types.component", "userOrNull")

            beginControlFlow("return context.run {")
            add("val bot = context.bot\n")
            add("val update = context.update\n")
            add("val registry = context.registry\n")
            add("val user = update.userOrNull ?: return@run Unit\n")

            // Get engine from registry
            add(
                "val wizardEngine = registry.getActivity(%L) as? %T ?: return@run Unit\n",
                activityId,
                TypeConstants.wizardActivity,
            )

            // Create wizard context
            add("val wizardCtx = WizardContext(user, update, bot)\n")

            // Handle input through wizard engine (it will update inputListener internally)
            add("wizardEngine.handleInput(wizardCtx)\n")

            endControlFlow()
        }.build()
    }
}

