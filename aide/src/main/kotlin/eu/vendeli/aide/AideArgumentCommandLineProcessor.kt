package eu.vendeli.aide

import com.google.auto.service.AutoService
import eu.vendeli.aide.utils.Option
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@AutoService(CommandLineProcessor::class)
@OptIn(ExperimentalCompilerApi::class)
class AideArgumentCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = "eu.vendeli.aide"
    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        AUTO_SEND_OPTION,
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option.optionName) {
            AUTO_SEND -> configuration.put(AUTO_SEND_OPTION_KEY, value)
            else -> error("Unknown plugin option: ${option.optionName}")
        }
    }

    companion object {
        const val AUTO_SEND = "autoSend"

        val AUTO_SEND_OPTION = Option(AUTO_SEND, "Do automatically call send() after the actions method")

        internal val AUTO_SEND_OPTION_KEY = CompilerConfigurationKey<String>(AUTO_SEND_OPTION.description)
    }
}
