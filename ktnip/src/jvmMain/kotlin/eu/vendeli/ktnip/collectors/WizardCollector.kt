package eu.vendeli.ktnip.collectors

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.ktnip.annotation.AnnotationParser
import eu.vendeli.ktnip.dto.CollectorsContext
import eu.vendeli.ktnip.utils.FileBuilder
import eu.vendeli.ktnip.utils.findAnnotationRecursively
import eu.vendeli.ktnip.utils.getActivityId
import eu.vendeli.ktnip.utils.getActivityObjectName
import eu.vendeli.ktnip.utils.getAnnotatedClassSymbols
import eu.vendeli.tgbot.annotations.WizardHandler
import eu.vendeli.tgbot.types.chain.WizardStep
import eu.vendeli.tgbot.types.chain.WizardStateManager
import eu.vendeli.tgbot.utils.common.fqName

/**
 * Collects WizardHandler annotated functions.
 * Generates concrete WizardEngine implementations and Activities that start wizard flows.
 */
internal class WizardCollector : BaseCollector() {
    override fun collect(resolver: Resolver, ctx: CollectorsContext) {
        ctx.logger.info("Collecting wizard handlers.")
        val symbols = resolver.getAnnotatedClassSymbols(WizardHandler::class, ctx.pkg)

        symbols.forEach { classDecl ->
            val annotation = classDecl.annotations
                .findAnnotationRecursively(WizardHandler::class)
                ?: throw IllegalStateException("No WizardHandler annotation found for $classDecl")

            // Parse annotation
            val annotationData = AnnotationParser.parseWizardHandler(annotation.arguments)

            // Find nested WizardStep classes/objects within the wizard class
            val wizardStepFqName = WizardStep::class.fqName
            val nestedSteps = classDecl.declarations
                .filterIsInstance<KSClassDeclaration>()
                .filter { nestedClass ->
                    nestedClass.getAllSuperTypes().any { superType ->
                        superType.declaration.qualifiedName?.asString() == wizardStepFqName
                    }
                }
                .toList()

            if (nestedSteps.isEmpty()) {
                ctx.logger.warn(
                    "Class ${classDecl.qualifiedName?.asString()} annotated with @WizardHandler " +
                        "must contain at least one nested class/object that extends WizardStep. Skipping.",
                )
                return@forEach
            }

            val classQualifier = classDecl.qualifiedName!!.getQualifier()
            val classShortName = classDecl.simpleName.asString()
            val classQualifiedName = classDecl.qualifiedName!!.asString()
            
            val isObject = classDecl.classKind == com.google.devtools.ksp.symbol.ClassKind.OBJECT
            
            // Build list of step accessors
            val stepsList = nestedSteps.map { step ->
                val stepName = step.simpleName.asString()
                if (isObject) {
                    ctx.activitiesFile.addImport(classQualifier, classShortName)
                    "$classShortName.$stepName"
                } else {
                    "$classQualifiedName.$stepName"
                }
            }
            val stepsListCode = stepsList.joinToString(",\n        ", "listOf(\n        ", "\n    )")

            val objectName = classDecl.getActivityObjectName()
            val activityId = classDecl.getActivityId()
            val engineObjectName = "${objectName}Engine"

            // Match nested steps to state managers based on store() return type
            val stepToManagerMap = matchStepsToStateManagers(
                nestedSteps,
                annotationData.stateManagers,
                resolver,
                ctx,
            )

            // Extract metadata for engine activity from class
            val rateLimits = eu.vendeli.ktnip.annotation.AnnotationExtractor.extractRateLimits(classDecl)
            val guardClass = eu.vendeli.ktnip.annotation.AnnotationExtractor.extractGuard(classDecl)
            val argParserClass = eu.vendeli.ktnip.annotation.AnnotationExtractor.extractArgParser(classDecl)
            val metadata = eu.vendeli.ktnip.dto.ActivityMetadata(
                id = activityId,
                qualifier = classQualifier,
                function = classShortName,
                rateLimits = rateLimits,
                guardClass = guardClass ?: eu.vendeli.tgbot.implementations.DefaultGuard::class.fqName,
                argParserClass = argParserClass ?: eu.vendeli.tgbot.implementations.DefaultArgParser::class.fqName,
            )

            // Generate concrete WizardEngine implementation as Activity
            generateWizardEngine(
                engineObjectName = engineObjectName,
                activityId = activityId,
                funQualifier = classQualifier,
                funShortName = classShortName,
                stepToManagerMap = stepToManagerMap,
                metadata = metadata,
                stepsListCode = stepsListCode,
                fileBuilder = ctx.activitiesFile,
            )

            // Generate Activity for command trigger (starts wizard)
            val startActivityObject = TypeSpec.objectBuilder("${objectName}Start")
                .addSuperinterface(ClassName("eu.vendeli.tgbot.core", "Activity"))
                .addModifiers(KModifier.INTERNAL)
                .addProperty(
                    PropertySpec.builder("id", INT, KModifier.OVERRIDE)
                        .initializer("%L", activityId)
                        .build(),
                )
                .addProperty(
                    PropertySpec.builder("qualifier", STRING, KModifier.OVERRIDE)
                        .initializer("%S", classQualifier)
                        .build(),
                )
                .addProperty(
                    PropertySpec.builder("function", STRING, KModifier.OVERRIDE)
                        .initializer("%S", classShortName)
                        .build(),
                )
                .addFunction(
                    FunSpec.builder("invoke")
                        .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
                        .addParameter("context", ClassName("eu.vendeli.tgbot.types.component", "ProcessingContext"))
                        .returns(Any::class.asTypeName().copy(nullable = true))
                        .addCode(
                            buildStartWizardCode(
                                fileBuilder = ctx.activitiesFile,
                                activityId = activityId,
                            ),
                        )
                        .build(),
                )
                .build()

            ctx.activitiesFile.addType(startActivityObject)

            // Generate Activity for wizard input handler
            val inputActivityObject = TypeSpec.objectBuilder("${objectName}Input")
                .addSuperinterface(ClassName("eu.vendeli.tgbot.core", "Activity"))
                .addModifiers(KModifier.INTERNAL)
                .addProperty(
                    PropertySpec.builder("id", INT, KModifier.OVERRIDE)
                        .initializer("%L", activityId + 1)
                        .build(),
                )
                .addProperty(
                    PropertySpec.builder("qualifier", STRING, KModifier.OVERRIDE)
                        .initializer("%S", classQualifier)
                        .build(),
                )
                .addProperty(
                    PropertySpec.builder("function", STRING, KModifier.OVERRIDE)
                        .initializer("%S", classShortName)
                        .build(),
                )
                .addFunction(
                    FunSpec.builder("invoke")
                        .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
                        .addParameter("context", ClassName("eu.vendeli.tgbot.types.component", "ProcessingContext"))
                        .returns(Any::class.asTypeName().copy(nullable = true))
                        .addCode(
                            buildWizardInputCode(
                                fileBuilder = ctx.activitiesFile,
                                activityId = activityId,
                            ),
                        )
                        .build(),
                )
                .build()

            ctx.activitiesFile.addType(inputActivityObject)

            // Register commands
            annotationData.triggers.forEach { trigger ->
                annotationData.scope.forEach { updT ->
                    ctx.logger.info("Wizard trigger: $trigger UpdateType: ${updT.name} --> ${classDecl.qualifiedName?.asString()}")
                    ctx.loadFun.addStatement(
                        "registerCommand(%S, UpdateType.%L, %N.id)",
                        trigger,
                        updT.name,
                        "${objectName}Start",
                    )
                }
            }

            // Register all activities first
            ctx.loadFun.addStatement(
                "registerActivity(%N)",
                "${objectName}Start",
            )
            ctx.loadFun.addStatement(
                "registerActivity(%N)",
                "${objectName}Input",
            )
            ctx.loadFun.addStatement(
                "registerActivity(%N)",
                engineObjectName,
            )

            // Register input handler for each step
            // The engine sets inputListener to "wizard:${activityId}:${step.id}"
            // The step's id defaults to its fully qualified class name
            nestedSteps.forEach { step ->
                // Use the step's qualified name as the stepId (matches default id property)
                val stepQualifiedName = step.qualifiedName!!.asString()
                
                // Register input for this specific step
                // Format: "wizard:${activityId}:${stepQualifiedName}"
                ctx.loadFun.addStatement(
                    "registerInput(%S, %N.id)",
                    "wizard:${activityId}:$stepQualifiedName",
                    "${objectName}Input",
                )
            }

            // Generate type-safe state accessor extensions for steps with state managers
            generateStateAccessorExtensions(
                stepToManagerMap = stepToManagerMap,
                resolver = resolver,
                ctx = ctx,
            )
        }
    }

    private fun matchStepsToStateManagers(
        allSteps: List<KSClassDeclaration>,
        stateManagers: List<KSClassDeclaration>,
        resolver: Resolver,
        ctx: CollectorsContext,
    ): Map<KSClassDeclaration, KSClassDeclaration> {
        val stepToManagerMap = mutableMapOf<KSClassDeclaration, KSClassDeclaration>()
        val wizardStateManagerFqName = WizardStateManager::class.fqName

        allSteps.forEach { stepClass ->
            // Find store() function
            val storeFunction = stepClass
                .getAllFunctions()
                .find { it.simpleName.asString() == WizardStep::store.name && it.parameters.size == 1 }

            if (storeFunction == null) {
                return@forEach // Step doesn't store state
            }

            val storeReturnType = storeFunction.returnType?.resolve()?.toTypeName()
                ?: return@forEach

            val returnTypeName = storeReturnType.toString()
            if (returnTypeName == "kotlin.Any?" || returnTypeName == "kotlin.Unit") {
                return@forEach // Generic return type, skip
            }

            // Find matching state manager
            val matchingManager = stateManagers.firstOrNull { managerClass ->
                managerClass.getAllSuperTypes().any { superType ->
                    val decl = superType.declaration as? KSClassDeclaration
                    if (decl?.qualifiedName?.asString() == wizardStateManagerFqName) {
                        // Check type argument
                        val typeArg = superType.arguments.firstOrNull()?.type?.resolve()
                        typeArg?.toTypeName()?.toString() == returnTypeName
                    } else {
                        false
                    }
                }
            }

            if (matchingManager != null) {
                stepToManagerMap[stepClass] = matchingManager
                ctx.logger.info(
                    "Matched step ${stepClass.simpleName.asString()} (returns $returnTypeName) " +
                        "with state manager ${matchingManager.simpleName.asString()}",
                )
            }
        }

        return stepToManagerMap
    }

    private fun generateWizardEngine(
        engineObjectName: String,
        activityId: Int,
        funQualifier: String,
        funShortName: String,
        stepToManagerMap: Map<KSClassDeclaration, KSClassDeclaration>,
        metadata: eu.vendeli.ktnip.dto.ActivityMetadata,
        stepsListCode: String,
        fileBuilder: FileBuilder,
    ) {
        fileBuilder.addImport("eu.vendeli.tgbot.types.chain", "WizardEngine", "WizardStep", "WizardStateManager")
        fileBuilder.addImport("eu.vendeli.tgbot", "TelegramBot")
        fileBuilder.addImport("eu.vendeli.tgbot.types", "User")
        fileBuilder.addImport("eu.vendeli.tgbot.utils.common", "getInstance")
        fileBuilder.addImport("eu.vendeli.tgbot.types.configuration", "RateLimits")
        fileBuilder.addImport("kotlin.reflect", "KClass")
        
        // Add import for the wizard class (to access nested steps)
        fileBuilder.addImport(funQualifier, funShortName)

        val stepsType = List::class.asTypeName().parameterizedBy(ClassName("eu.vendeli.tgbot.types.chain", "WizardStep"))
        val engineObject = TypeSpec.objectBuilder(engineObjectName)
            .addModifiers(KModifier.INTERNAL)
            .superclass(ClassName("eu.vendeli.tgbot.types.chain", "WizardEngine"))
            // Activity properties
            .addProperty(
                PropertySpec.builder("id", INT, KModifier.OVERRIDE)
                    .initializer("%L", activityId)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder("qualifier", STRING, KModifier.OVERRIDE)
                    .initializer("%S", funQualifier)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder("function", STRING, KModifier.OVERRIDE)
                    .initializer("%S", funShortName)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder("rateLimits", ClassName("eu.vendeli.tgbot.types.configuration", "RateLimits"), KModifier.OVERRIDE)
                    .initializer("%T(%L, %L)", ClassName("eu.vendeli.tgbot.types.configuration", "RateLimits"), metadata.rateLimits.rate, metadata.rateLimits.period)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder("guardClass", ClassName("kotlin.reflect", "KClass").parameterizedBy(
                    WildcardTypeName.producerOf(ClassName("eu.vendeli.tgbot.interfaces.helper", "Guard")),
                ), KModifier.OVERRIDE)
                    .initializer("%T::class", ClassName.bestGuess(metadata.guardClass))
                    .build(),
            )
            .addProperty(
                PropertySpec.builder("argParser", ClassName("kotlin.reflect", "KClass").parameterizedBy(
                    WildcardTypeName.producerOf(ClassName("eu.vendeli.tgbot.interfaces.helper", "ArgumentParser")),
                ), KModifier.OVERRIDE)
                    .initializer("%T::class", ClassName.bestGuess(metadata.argParserClass))
                    .build(),
            )
            // WizardEngine properties - steps are hardcoded
            .addProperty(
                PropertySpec.builder("steps", stepsType)
                    .addModifiers(KModifier.OVERRIDE)
                    .initializer(CodeBlock.of("%L", stepsListCode))
                    .build(),
            )
            .addFunction(
                FunSpec.builder("getStateManagerForStep")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("step", ClassName("kotlin.reflect", "KClass").parameterizedBy(
                        WildcardTypeName.producerOf(ClassName("eu.vendeli.tgbot.types.chain", "WizardStep")),
                    ))
                    .addParameter("bot", ClassName("eu.vendeli.tgbot", "TelegramBot"))
                    .returns(ClassName("eu.vendeli.tgbot.types.chain", "WizardStateManager").parameterizedBy(WildcardTypeName.producerOf(Any::class.asTypeName())).copy(nullable = true))
                    .addCode(
                        buildStateManagerResolutionCode(stepToManagerMap, fileBuilder),
                    )
                    .build(),
            )
            .build()

        fileBuilder.addType(engineObject)
    }

    private fun buildStateManagerResolutionCode(
        stepToManagerMap: Map<KSClassDeclaration, KSClassDeclaration>,
        fileBuilder: FileBuilder,
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
                add("        val manager = bot.getInstance(%T::class) as? %T<*>\n", managerClassName, ClassName("eu.vendeli.tgbot.types.chain", "WizardStateManager"))
                add("        manager\n")
                add("    }\n")
            }
            add("    else -> null\n")
            add("}\n")
        }.build()
    }

    private fun buildStartWizardCode(
        fileBuilder: FileBuilder,
        activityId: Int,
    ): CodeBlock {
        return CodeBlock.builder().apply {
            fileBuilder.addImport("eu.vendeli.tgbot.types.chain", "WizardContext", "WizardEngine")
            fileBuilder.addImport("eu.vendeli.tgbot.types.component", "userOrNull")

            beginControlFlow("return context.run {")
            add("val bot = context.bot\n")
            add("val update = context.update\n")
            add("val registry = context.registry\n")
            add("val user = update.userOrNull ?: return@run Unit\n")

            // Get engine from registry
            add("val wizardEngine = registry.getActivity(%L) as? %T ?: return@run Unit\n", activityId, ClassName("eu.vendeli.tgbot.types.chain", "WizardEngine"))

            // Create wizard context
            add("val wizardCtx = WizardContext(user, update, bot)\n")

            // Start wizard (this will call onEntry of initial step and set inputListener)
            add("wizardEngine.start(wizardCtx)\n")

            endControlFlow()
        }.build()
    }

    private fun buildWizardInputCode(
        fileBuilder: FileBuilder,
        activityId: Int,
    ): CodeBlock {
        return CodeBlock.builder().apply {
            fileBuilder.addImport("eu.vendeli.tgbot.types.chain", "WizardContext", "WizardEngine")
            fileBuilder.addImport("eu.vendeli.tgbot.types.component", "userOrNull")

            beginControlFlow("return context.run {")
            add("val bot = context.bot\n")
            add("val update = context.update\n")
            add("val registry = context.registry\n")
            add("val user = update.userOrNull ?: return@run Unit\n")

            // Get engine from registry
            add("val wizardEngine = registry.getActivity(%L) as? %T ?: return@run Unit\n", activityId, ClassName("eu.vendeli.tgbot.types.chain", "WizardEngine"))

            // Create wizard context
            add("val wizardCtx = WizardContext(user, update, bot)\n")

            // Handle input through wizard engine (it will update inputListener internally)
            add("wizardEngine.handleInput(wizardCtx)\n")

              endControlFlow()
          }.build()
      }

    private fun generateStateAccessorExtensions(
        stepToManagerMap: Map<KSClassDeclaration, KSClassDeclaration>,
        resolver: Resolver,
        ctx: CollectorsContext,
    ) {
        if (stepToManagerMap.isEmpty()) {
            return
        }

        val wizardContextClassName = ClassName("eu.vendeli.tgbot.types.chain", "WizardContext")
        val wizardEngineClassName = ClassName("eu.vendeli.tgbot.types.chain", "WizardEngine")
        val wizardStateManagerClassName = ClassName("eu.vendeli.tgbot.types.chain", "WizardStateManager")

        // Add necessary imports
        ctx.botCtxFile.addImport("eu.vendeli.tgbot.types.chain", "WizardEngine", "WizardStateManager", "UserChatReference", "WizardStep")

        // Build a map of step class to return type for the when expression
        val stepReturnTypeMap = mutableMapOf<KSClassDeclaration, TypeName>()
        stepToManagerMap.forEach { (stepClass, _) ->
            val storeFunction = stepClass
                .getAllFunctions()
                .find { it.simpleName.asString() == "store" && it.parameters.size == 1 }
                ?: return@forEach

            val returnType = storeFunction.returnType?.resolve()?.toTypeName()
                ?: return@forEach

            val returnTypeName = returnType.toString()
            if (returnTypeName != "kotlin.Any?" && returnTypeName != "kotlin.Unit") {
                stepReturnTypeMap[stepClass] = returnType
            }
        }

        if (stepReturnTypeMap.isEmpty()) {
            return
        }

        // Add imports for all step classes
        stepReturnTypeMap.keys.forEach { stepClass ->
            ctx.botCtxFile.addImport(stepClass.packageName.asString(), stepClass.simpleName.asString())
        }
        stepToManagerMap.values.forEach { managerClass ->
            ctx.botCtxFile.addImport(managerClass.packageName.asString(), managerClass.simpleName.asString())
        }

        // Generate overloaded functions for each step with correct return types
        stepReturnTypeMap.forEach { (stepClass, returnType) ->
            val stepClassName = stepClass.toClassName()
            val stepTypeParam = ParameterSpec.builder("step", stepClassName.copy(nullable = true))
                .defaultValue("%L", "null")
                .build()

            // Generate getState function for this specific step
            val stepTypeGeneric = TypeVariableName("S", stepClassName).copy(reified = true)
            val getFun = FunSpec.builder("getState")
                .addModifiers(KModifier.SUSPEND, KModifier.INLINE)
                .receiver(wizardContextClassName)
                .addTypeVariable(stepTypeGeneric)
                .addParameter(stepTypeParam)
                .returns(returnType.copy(nullable = true))
                .addCode(
                    CodeBlock.builder()
                        .addStatement("val engine = bot.update.registry.getActivity(currentWizardId) as? %T ?: return null", wizardEngineClassName)
                        .addStatement("val manager = engine.getStateManagerForStep(%T::class, bot) as? %T<%T> ?: return null", stepClassName, wizardStateManagerClassName, returnType)
                        .addStatement("return manager.get(%T::class, userReference)", stepClassName)
                        .build(),
                )
                .build()

            // Generate setState function for this specific step
            val setFun = FunSpec.builder("setState")
                .addModifiers(KModifier.SUSPEND, KModifier.INLINE)
                .receiver(wizardContextClassName)
                .addTypeVariable(stepTypeGeneric)
                .addParameter("value", returnType.copy(nullable = false))
                .addParameter(stepTypeParam)
                .addCode(
                    CodeBlock.builder()
                        .addStatement("val engine = bot.update.registry.getActivity(currentWizardId) as? %T ?: return", wizardEngineClassName)
                        .addStatement("val manager = engine.getStateManagerForStep(%T::class, bot) as? %T<%T> ?: return", stepClassName, wizardStateManagerClassName, returnType)
                        .addStatement("manager.set(%T::class, userReference, value)", stepClassName)
                        .build(),
                )
                .build()

            // Generate delState function for this specific step
            val delFun = FunSpec.builder("delState")
                .addModifiers(KModifier.SUSPEND, KModifier.INLINE)
                .receiver(wizardContextClassName)
                .addTypeVariable(stepTypeGeneric)
                .addParameter(stepTypeParam)
                .addCode(
                    CodeBlock.builder()
                        .addStatement("val engine = bot.update.registry.getActivity(currentWizardId) as? %T ?: return", wizardEngineClassName)
                        .addStatement("val manager = engine.getStateManagerForStep(%T::class, bot) as? %T<%T> ?: return", stepClassName, wizardStateManagerClassName, returnType)
                        .addStatement("manager.del(%T::class, userReference)", stepClassName)
                        .build(),
                )
                .build()

            // Add functions to the file
            ctx.botCtxFile.addFunction(getFun)
            ctx.botCtxFile.addFunction(setFun)
            ctx.botCtxFile.addFunction(delFun)
        }

        ctx.logger.info("Generated type-safe state extensions for ${stepReturnTypeMap.size} steps")
    }
  }
