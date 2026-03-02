package eu.vendeli.ktnip.codegen

import com.squareup.kotlinpoet.CodeBlock
import eu.vendeli.ktnip.dto.CommandHandlerParams
import eu.vendeli.ktnip.dto.LambdaParameters
import eu.vendeli.ktnip.dto.ParameterResolutionStrategy
import eu.vendeli.ktnip.utils.FileBuilder
import eu.vendeli.tgbot.types.component.UpdateType

/**
 * Generates code blocks for Activity invocation.
 * Uses ParameterResolutionStrategy to generate appropriate code for each parameter.
 */
class InvocationCodeGenerator(
    private val fileBuilder: FileBuilder,
) {
    /**
     * Generates code to resolve a parameter based on its strategy.
     *
     * @param strategy The resolution strategy
     * @param parameterIndex The index of the parameter in the function
     * @return Code block that assigns the parameter value
     */
    fun generateParameterResolution(
        strategy: ParameterResolutionStrategy,
        parameterIndex: Int,
    ): CodeBlock {
        val nullabilityMark = if (strategy.isNullable) "" else "!!"

        return when (strategy) {
            is ParameterResolutionStrategy.User -> {
                CodeBlock.of("val param%L = user%L\n", parameterIndex, nullabilityMark)
            }

            is ParameterResolutionStrategy.Chat -> {
                fileBuilder.addImport("eu.vendeli.tgbot.types.component", "chatOrNull")
                CodeBlock.of("val param%L = update.chatOrNull%L\n", parameterIndex, nullabilityMark)
            }

            is ParameterResolutionStrategy.Bot -> {
                CodeBlock.of("val param%L = bot\n", parameterIndex)
            }

            is ParameterResolutionStrategy.ProcessingContext -> {
                CodeBlock.of("val param%L = context\n", parameterIndex)
            }

            is ParameterResolutionStrategy.Update -> {
                CodeBlock.of("val param%L = update\n", parameterIndex)
            }

            is ParameterResolutionStrategy.StringParameter -> {
                CodeBlock.of(
                    "val param%L = parameters[%S]%L\n",
                    parameterIndex,
                    strategy.parameterName,
                    nullabilityMark,
                )
            }

            is ParameterResolutionStrategy.PrimitiveParameter -> {
                CodeBlock.of(
                    "val param%L = parameters[%S]?.%L%L\n",
                    parameterIndex,
                    strategy.parameterName,
                    strategy.conversionMethod,
                    nullabilityMark,
                )
            }

            is ParameterResolutionStrategy.TypedUpdate -> {
                fileBuilder.addImport("eu.vendeli.tgbot.types.component", strategy.updateTypeSimpleName)
                CodeBlock.of(
                    "val param%L = (update as? %L)%L\n",
                    parameterIndex,
                    strategy.updateTypeSimpleName,
                    nullabilityMark,
                )
            }

            is ParameterResolutionStrategy.Injectable -> {
                fileBuilder.addImport("eu.vendeli.tgbot.utils.common", "getInstance")
                CodeBlock.of(
                    "val param%L = bot.getInstance(%L::class)!!.get(update, bot)\n",
                    parameterIndex,
                    strategy.injectableClassName.canonicalName,
                )
            }

            is ParameterResolutionStrategy.Unsupported -> {
                CodeBlock.of(
                    "val param%L = null // Can't acquire `%L` type since it's not in resolvable/autowiring types\n",
                    parameterIndex,
                    strategy.typeName,
                )
            }
        }
    }

    /**
     * Generates complete invocation code block for an Activity.
     *
     * @param funName Function reference to invoke
     * @param hasInstance Whether instance resolution is needed (non-static, non-object)
     * @param instanceQualifier Qualified name of the class if instance needed
     * @param parameterStrategies Map of parameter index to resolution strategy
     * @param updateType UpdateType for special handling (e.g., callback auto-answer)
     * @param parameters Additional lambda parameters (e.g., CallbackQueryAutoAnswer)
     * @return Complete invocation code block
     */
    fun generateInvocationCode(
        funName: String,
        hasInstance: Boolean,
        instanceQualifier: String?,
        parameterStrategies: Map<Int, ParameterResolutionStrategy>,
        updateType: UpdateType?,
        parameters: List<LambdaParameters>,
    ): CodeBlock = CodeBlock
        .builder()
        .apply {
            val isCallbackAutoAnswer = updateType == UpdateType.CALLBACK_QUERY &&
                parameters.contains(CommandHandlerParams.CallbackQueryAutoAnswer)

            val strategies = parameterStrategies.values
            val isBotNeeded = hasInstance ||
                isCallbackAutoAnswer ||
                strategies.any {
                    it is ParameterResolutionStrategy.Bot || it is ParameterResolutionStrategy.Injectable
                }
            val isUpdateNeeded = isCallbackAutoAnswer ||
                strategies.any {
                    it is ParameterResolutionStrategy.Chat ||
                        it is ParameterResolutionStrategy.Update ||
                        it is ParameterResolutionStrategy.TypedUpdate ||
                        it is ParameterResolutionStrategy.Injectable
                }
            val isParametersNeeded = strategies.any {
                it is ParameterResolutionStrategy.StringParameter ||
                    it is ParameterResolutionStrategy.PrimitiveParameter
            }
            val isUserNeeded = strategies.any { it is ParameterResolutionStrategy.User }

            beginControlFlow("return context.run {")
            if (isBotNeeded) add("val bot = context.bot\n")
            if (isUpdateNeeded || isUserNeeded) add("val update = context.update\n")
            if (isParametersNeeded) add("val parameters = context.parameters\n")

            val callArgs = mutableListOf<String>()

            // Instance resolution if needed
            if (hasInstance && instanceQualifier != null) {
                callArgs.add("inst")
                fileBuilder.addImport("eu.vendeli.tgbot.utils.common", "getInstance")
                add("val inst = bot.getInstance(%L::class)!!\n", instanceQualifier)
            }

            // User resolution
            if (isUserNeeded) {
                fileBuilder.addImport("eu.vendeli.tgbot.types.component", "userOrNull")
                add("val user = update.userOrNull\n")
            }

            parameterStrategies.keys.sorted().forEach { index ->
                val strategy = parameterStrategies[index]!!
                add(generateParameterResolution(strategy, index))
                callArgs.add("param$index")
            }

            // Callback query auto-answer
            if (isCallbackAutoAnswer) {
                fileBuilder.addImport("eu.vendeli.tgbot.api.answer", "answerCallbackQuery")
                fileBuilder.addImport("eu.vendeli.tgbot.types.component", "CallbackQueryUpdate", "getUser")
                add(
                    "answerCallbackQuery((update as CallbackQueryUpdate).callbackQuery.id).send(update.getUser(), bot)\n",
                )
            }

            // Actual invocation
            add("\n%L.invoke(\n\t%L\n)\n", funName, callArgs.joinToString(", "))
            endControlFlow()
        }.build()
}
