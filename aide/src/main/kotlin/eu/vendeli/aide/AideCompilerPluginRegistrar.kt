package eu.vendeli.aide

import com.google.auto.service.AutoService
import eu.vendeli.aide.ir.SendAutoAppenderExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CompilerPluginRegistrar::class)
class AideCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val doAutoSend = checkNotNull(configuration[AideCommandLineProcessor.AUTO_SEND_OPTION_KEY]).toBoolean()

        IrGenerationExtension.registerExtension(SendAutoAppenderExtension(doAutoSend))
    }

    override val supportsK2: Boolean get() = true
}
