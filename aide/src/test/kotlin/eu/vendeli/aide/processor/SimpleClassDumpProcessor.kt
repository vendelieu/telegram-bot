package eu.vendeli.aide.processor

import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationWithName
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.isFakeOverride
import org.jetbrains.kotlin.name.Name

@OptIn(UnsafeDuringIrConstructionAPI::class)
class SimpleClassDumpProcessor : AbstractTestProcessor() {
    override fun StringBuilder.analyse(data: AnalysisData) {
        data.file.declarations
            .filterIsInstance<IrClass>()
            .forEach { analyseClass(it) }
    }

    private fun StringBuilder.analyseClass(clas: IrClass, indentLevel: Int = 0) {
        appendIndent(indentLevel).appendLine("class ${clas.name}")
        clas.declarations
            .sortedBy { (it as? IrDeclarationWithName)?.name ?: Name.identifier("") }
            .filterNot { it.isFakeOverride }
            .forEach { declaration ->
                when (declaration) {
                    is IrSimpleFunction ->
                        appendIndent(indentLevel + 1)
                            .append("fun ${declaration.name.identifier}(): ")
                            .appendLine(
                                declaration.returnType.classOrNull
                                    ?.owner
                                    ?.name,
                            )

                    is IrProperty ->
                        appendIndent(indentLevel + 1)
                            .append(if (declaration.isVar) "var" else "val")
                            .append(" ")
                            .append(declaration.name)
                            .append(": ")
                            .appendLine(
                                declaration.backingField
                                    ?.type
                                    ?.classOrNull
                                    ?.owner
                                    ?.name
                                    ?: declaration.getter
                                        ?.returnType
                                        ?.classOrNull
                                        ?.owner
                                        ?.name,
                            )

                    is IrClass -> analyseClass(declaration, indentLevel + 1)
                    else -> Unit
                }
            }
    }

    private fun StringBuilder.appendIndent(level: Int): StringBuilder = apply {
        if (level > 0) append(" ".repeat(level * 2))
    }
}
