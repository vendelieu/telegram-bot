package eu.vendeli.ktnip.collectors

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.ktnip.annotation.AnnotationParser
import eu.vendeli.ktnip.codegen.WizardCodeGenerator
import eu.vendeli.ktnip.codegen.WizardStateAccessorGenerator
import eu.vendeli.ktnip.dto.ActivityMetadata
import eu.vendeli.ktnip.dto.CollectorsContext
import eu.vendeli.ktnip.utils.TypeConstants
import eu.vendeli.ktnip.utils.findAnnotationRecursively
import eu.vendeli.ktnip.utils.getActivityId
import eu.vendeli.ktnip.utils.getActivityObjectName
import eu.vendeli.ktnip.utils.getAnnotatedClassSymbols
import eu.vendeli.ktnip.utils.safeCast
import eu.vendeli.ktnip.utils.toKSPClassName
import eu.vendeli.tgbot.annotations.WizardHandler
import eu.vendeli.tgbot.types.chain.WizardStateManager
import eu.vendeli.tgbot.types.chain.WizardStep
import eu.vendeli.tgbot.utils.common.fqName

/**
 * Collects WizardHandler annotated classes/objects.
 * Delegates code generation to WizardCodeGenerator and WizardStateAccessorGenerator.
 */
internal class WizardCollector : BaseCollector() {
    override fun collect(resolver: Resolver, ctx: CollectorsContext) {
        ctx.logger.info("Collecting wizard handlers.")
        val symbols = resolver.getAnnotatedClassSymbols(WizardHandler::class, ctx.pkg)

        symbols.forEach { classDecl ->
            val annotation = classDecl.annotations
                .findAnnotationRecursively(WizardHandler::class)
                ?: error("No WizardHandler annotation found for $classDecl")

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

            val isObject = classDecl.classKind == ClassKind.OBJECT

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
                ctx,
            )

            // Extract metadata for engine activity from class
            val rateLimits = eu.vendeli.ktnip.annotation.AnnotationExtractor.extractRateLimits(classDecl)
            val guardClass = eu.vendeli.ktnip.annotation.AnnotationExtractor.extractGuard(classDecl)
            val argParserClass = eu.vendeli.ktnip.annotation.AnnotationExtractor.extractArgParser(classDecl)
            val metadata = ActivityMetadata(
                id = activityId,
                qualifier = classQualifier,
                function = classShortName,
                rateLimits = rateLimits,
                guardClass = guardClass ?: eu.vendeli.tgbot.implementations.DefaultGuard::class.fqName,
                argParserClass = argParserClass ?: eu.vendeli.tgbot.implementations.DefaultArgParser::class.fqName,
            )

            // Generate wizard code using code generator
            val wizardCodeGenerator = WizardCodeGenerator(ctx.activitiesFile)

            // Generate concrete WizardActivity implementation as Activity
            wizardCodeGenerator.generateWizardEngine(
                engineObjectName = engineObjectName,
                activityId = activityId,
                funQualifier = classQualifier,
                funShortName = classShortName,
                stepToManagerMap = stepToManagerMap,
                metadata = metadata,
                stepsListCode = stepsListCode,
            )

            // Generate Activity for command trigger (starts wizard)
            wizardCodeGenerator.generateStartActivity(
                objectName = objectName,
                activityId = activityId,
                classQualifier = classQualifier,
                classShortName = classShortName,
            )

            // Generate Activity for wizard input handler
            wizardCodeGenerator.generateInputActivity(
                objectName = objectName,
                activityId = activityId,
                classQualifier = classQualifier,
                classShortName = classShortName,
            )

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
            val stateAccessorGenerator = WizardStateAccessorGenerator(ctx.botCtxFile)
            stateAccessorGenerator.generateStateAccessorExtensions(
                stepToManagerMap = stepToManagerMap,
                ctx = ctx,
            )
        }
    }

    private fun matchStepsToStateManagers(
        allSteps: List<KSClassDeclaration>,
        stateManagers: List<KSClassDeclaration>,
        ctx: CollectorsContext,
    ): Map<KSClassDeclaration, KSClassDeclaration> {
        val stepToManagerMap = mutableMapOf<KSClassDeclaration, KSClassDeclaration>()
        val stateManagerAnnotationFqName = WizardHandler.StateManager::class.fqName

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

            // First, check if step has @WizardHandler.StateManager annotation
            val stateManagerAnnotation = stepClass.annotations.firstOrNull { annotation ->
                annotation.annotationType.resolve().declaration.qualifiedName?.asString() == stateManagerAnnotationFqName
            }

            val matchingManager = if (stateManagerAnnotation != null) {
                // Extract state manager from annotation (annotation has a single "value" parameter)
                val annotationValue = stateManagerAnnotation.arguments.firstOrNull()?.value
                when (val value = annotationValue) {
                    is KSType -> {
                        value.declaration.safeCast<KSClassDeclaration>()?.also {
                            if (resolveWizardStateManagerTypeArg(it) != storeReturnType) {
                                ctx.logger.warn(
                                    "WizardStateManager type mismatch: ${it.simpleName.asString()} " +
                                        "handles $returnTypeName, but WizardStep.store() returns $storeReturnType",
                                )
                            }
                        }
                    }

                    else -> null
                }
            } else {
                // Fall back to type-based matching.
                // Manager type param (e.g. String) matches store return type (e.g. String?) when equal when stripped of nullability.
                val storeBaseType = storeReturnType.copy(nullable = false)
                stateManagers.find { manager ->
                    resolveWizardStateManagerTypeArg(manager)?.toTypeName() == storeBaseType
                }
            }

            if (matchingManager != null) {
                stepToManagerMap[stepClass] = matchingManager
                val source = if (stateManagerAnnotation != null) "annotation" else "type matching"
                ctx.logger.info(
                    "Matched step ${stepClass.simpleName.asString()} (returns $returnTypeName) " +
                        "with state manager ${matchingManager.simpleName.asString()} via $source",
                )
            } else {
                ctx.logger.warn(
                    "Step ${stepClass.simpleName.asString()} (returns $returnTypeName) " +
                        "does not have a matching state manager. Be aware that NOOP state manager will be used for it.",
                )
            }
        }

        return stepToManagerMap
    }

    private fun resolveWizardStateManagerTypeArg(
        manager: KSClassDeclaration,
    ): KSType? {
        // Build a map of type parameter substitutions as we traverse the hierarchy
        val typeParamMap = mutableMapOf<String, KSType>()

        fun resolveType(type: KSType): KSType {
            val declaration = type.declaration
            if (declaration is KSTypeParameter) {
                // Look up in our substitution map
                return typeParamMap[declaration.name.asString()] ?: type
            }
            return type
        }

        // Start from the manager class and traverse up
        var currentClass: KSClassDeclaration? = manager

        while (currentClass != null) {
            // Check each supertype
            for (superTypeRef in currentClass.superTypes) {
                val superType = superTypeRef.resolve()
                val superDecl = superType.declaration

                if (superDecl is KSClassDeclaration) {
                    // Map type arguments to type parameters of the superclass
                    val typeParams = superDecl.typeParameters
                    val typeArgs = superType.arguments

                    typeParams.zip(typeArgs).forEach { (param, arg) ->
                        val argType = arg.type?.resolve()
                        if (argType != null) {
                            // Resolve through existing mappings (for chained generics)
                            val resolved = resolveType(argType)
                            typeParamMap[param.name.asString()] = resolved
                        }
                    }

                    // Check if this is WizardStateManager
                    if (superDecl.qualifiedName?.asString() == TypeConstants.wizardStateManagerFqName) {
                        val typeArg = superType.arguments.firstOrNull()?.type?.resolve()
                            ?: return null
                        return resolveType(typeArg)
                    }
                }
            }

            // Move to the primary superclass for next iteration
            currentClass = currentClass.superTypes
                .map { it.resolve().declaration }
                .filterIsInstance<KSClassDeclaration>()
                .firstOrNull { it.classKind == ClassKind.CLASS }
        }

        return null
    }
}
