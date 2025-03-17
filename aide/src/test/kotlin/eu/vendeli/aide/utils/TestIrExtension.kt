package eu.vendeli.aide.utils

import eu.vendeli.aide.processor.AbstractTestProcessor
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid

class TestIrExtension(
    private val processor: AbstractTestProcessor,
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val visitor = TestIrVisitor(processor, pluginContext)
        moduleFragment.acceptVoid(visitor)
    }
}

private class TestIrVisitor(
    private val processor: AbstractTestProcessor,
    private val context: IrPluginContext,
) : IrElementVisitorVoid {
    override fun visitFile(declaration: IrFile) {
        super.visitFile(declaration)
        processor.analyse(AbstractTestProcessor.AnalysisData(declaration, context))
    }

    override fun visitElement(element: IrElement) {
        element.acceptChildrenVoid(this)
    }
}
