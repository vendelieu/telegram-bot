package eu.vendeli.aide.ir

import eu.vendeli.aide.utils.ANNOTATIONS_PKG
import eu.vendeli.aide.utils.isTelegramBotType
import eu.vendeli.aide.utils.isUserType
import eu.vendeli.aide.utils.vParameters
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class SendAutoAppenderExtension(
    private val doAutoSend: Boolean,
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        if (doAutoSend) moduleFragment.transformChildrenVoid(
            object : IrElementTransformerVoid() {
                override fun visitFunction(declaration: IrFunction): IrStatement {
                    // 1. Check for handler annotations
                    if (isHandlerFunction(declaration)) {
                        // 2. Find User/Bot parameters by type
                        val (userParam, botParam) = findParametersByType(declaration, pluginContext)

                        // 3. Transform Action-returning calls
                        declaration.body = declaration.body?.transform(
                            SendAutoAppenderTransformer(userParam, botParam, pluginContext),
                            null,
                        )
                    }
                    return super.visitFunction(declaration)
                }
            },
        )
    }

    private fun isHandlerFunction(func: IrFunction): Boolean = func.annotations.any { annotation ->
        annotation.type.classFqName
            ?.asString()
            ?.startsWith(ANNOTATIONS_PKG) == true
    }

    private fun findParametersByType(
        func: IrFunction,
        pluginContext: IrPluginContext,
    ): Pair<IrValueParameter?, IrValueParameter?> {
        val userParam = func.vParameters.firstOrNull {
            it.type.isUserType(pluginContext)
        }
        val botParam = func.vParameters.firstOrNull {
            it.type.isTelegramBotType(pluginContext)
        }
        return userParam to botParam
    }
}
