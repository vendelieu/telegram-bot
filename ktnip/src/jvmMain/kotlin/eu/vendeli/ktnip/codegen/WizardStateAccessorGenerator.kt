package eu.vendeli.ktnip.codegen

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.ktnip.dto.CollectorsContext
import eu.vendeli.ktnip.utils.FileBuilder

/**
 * Generates type-safe state accessor extension functions for WizardContext.
 * Creates getState, setState, and delState functions for each wizard step with a state manager.
 */
class WizardStateAccessorGenerator(
    private val fileBuilder: FileBuilder,
) {
    /**
     * Generates state accessor extensions for wizard steps.
     *
     * @param stepToManagerMap Map of step classes to their corresponding state manager classes
     * @param ctx Collectors context for logging
     */
    fun generateStateAccessorExtensions(
        stepToManagerMap: Map<KSClassDeclaration, KSClassDeclaration>,
        ctx: CollectorsContext,
    ) {
        if (stepToManagerMap.isEmpty()) {
            return
        }

        val wizardContextClassName = ClassName("eu.vendeli.tgbot.types.chain", "WizardContext")
        val wizardEngineClassName = ClassName("eu.vendeli.tgbot.types.chain", "WizardActivity")
        val wizardStateManagerClassName = ClassName("eu.vendeli.tgbot.types.chain", "WizardStateManager")

        // Add necessary imports
        fileBuilder.addImport("eu.vendeli.tgbot.types.chain", "WizardActivity", "WizardStateManager", "UserChatReference", "WizardStep")

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
            fileBuilder.addImport(stepClass.packageName.asString(), stepClass.simpleName.asString())
        }
        stepToManagerMap.values.forEach { managerClass ->
            fileBuilder.addImport(managerClass.packageName.asString(), managerClass.simpleName.asString())
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
            fileBuilder.addFunction(getFun)
            fileBuilder.addFunction(setFun)
            fileBuilder.addFunction(delFun)
        }

        ctx.logger.info("Generated type-safe state extensions for ${stepReturnTypeMap.size} steps")
    }
}

