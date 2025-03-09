package eu.vendeli.aide

import eu.vendeli.aide.processor.AbstractTestProcessor
import eu.vendeli.aide.utils.TestIrExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

@OptIn(ExperimentalCompilerApi::class)
class TestComponentRegistrar(
    private val processor: AbstractTestProcessor,
) : CompilerPluginRegistrar() {
    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        IrGenerationExtension.registerExtension(TestIrExtension(processor))
    }

    override val supportsK2: Boolean
        get() = true
}
