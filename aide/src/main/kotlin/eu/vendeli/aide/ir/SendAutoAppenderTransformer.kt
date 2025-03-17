package eu.vendeli.aide.ir

import eu.vendeli.aide.utils.ACTION_CLASS
import eu.vendeli.aide.utils.ACTION_PACKAGE
import eu.vendeli.aide.utils.SEND_FUN_NAME
import eu.vendeli.aide.utils.SIMPLE_ACTION_CLASS
import eu.vendeli.aide.utils.isActionType
import eu.vendeli.aide.utils.seekSendFunction
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.ir.hasContinuation
import org.jetbrains.kotlin.com.intellij.openapi.diagnostic.Logger
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.irCall
import org.jetbrains.kotlin.ir.util.isAnonymousObject
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class SendAutoAppenderTransformer(
    private val userParam: IrValueParameter?,
    private val botParam: IrValueParameter?,
    private val context: IrPluginContext,
) : IrElementTransformerVoid() {
    private val callStack = mutableListOf<IrCall>()
    private var skipTransformation = false
    private val logger = Logger.getInstance(javaClass)

    override fun visitVariable(declaration: IrVariable): IrStatement {
        val initializer = declaration.initializer
        if (initializer is IrCall && initializer.type.isActionType(context)) {
            skipTransformation = true
        }
        val result = super.visitVariable(declaration)

        skipTransformation = false
        return result
    }

    override fun visitCall(expression: IrCall): IrExpression {
        callStack.add(expression)
        val result = if (shouldWrapCall(expression)) {
            createSendCall(expression)
        } else {
            super.visitCall(expression)
        }
        callStack.removeLastOrNull()
        return result
    }

    // Only wrap calls that:
    // 1. Are action calls, Are not already a send call.
    // 2. Is not in parameter.
    // 3. Aren't nested within another send call.
    // 4. Are not in anonymous objects
    // 5. Have continuation
    @OptIn(ObsoleteDescriptorBasedAPI::class, UnsafeDuringIrConstructionAPI::class)
    private fun shouldWrapCall(call: IrCall): Boolean = when {
        callStack.any { !it.type.isActionType(context) || isSendCall(it) } -> false
        skipTransformation -> false
        isSendCall(call) -> false
        call.symbol.owner.isAnonymousObject -> false
        !call.symbol.owner.hasContinuation() -> false
        else -> true
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    private fun isSendCall(call: IrCall): Boolean =
        call.symbol.owner.name
            .asString() == SEND_FUN_NAME

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    private fun createSendCall(
        original: IrCall,
        isSimple: Boolean = false,
    ): IrCall {
        val callableId = CallableId(
            packageName = FqName(ACTION_PACKAGE),
            className = if (!isSimple) FqName(ACTION_CLASS) else FqName(SIMPLE_ACTION_CLASS),
            callableName = Name.identifier(SEND_FUN_NAME),
        )
        val sendFunctionSymbol = context.referenceFunctions(callableId).seekSendFunction(context, isSimple)
        return irCall(
            call = original,
            newSymbol = sendFunctionSymbol,
            newReturnType = sendFunctionSymbol.owner.returnType,
        ).apply {
            if (!isSimple) {
                if (userParam == null) {
                    logger.error(
                        "User param is not present which is required for send call",
                    )
                }
                if (botParam == null) {
                    logger.error("Bot param is not present which is required for send call")
                }

                putValueArgument(0, irGet(userParam!!, original.startOffset, original.endOffset))
                putValueArgument(1, irGet(botParam!!, original.startOffset, original.endOffset))
            } else {
                if (botParam == null) {
                    logger.error("Bot param is not present which is required for send call")
                }
                putValueArgument(0, irGet(botParam!!, original.startOffset, original.endOffset))
            }

            dispatchReceiver = original
        }
    }

    private fun irGet(param: IrValueParameter, startOffset: Int, endOffset: Int): IrExpression =
        IrGetValueImpl(startOffset = startOffset, endOffset = endOffset, symbol = param.symbol, origin = null)
}
