package eu.vendeli.aide.utils

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.isSubtypeOfClass
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

private val actionClassId = ClassId(
    FqName(ACTION_PACKAGE),
    FqName(ACTION_CLASS),
    false,
)
private val simpleActionClassId = ClassId(
    FqName(ACTION_PACKAGE),
    FqName(SIMPLE_ACTION_CLASS),
    false,
)
internal val userClassId = ClassId(
    FqName(USER_PACKAGE),
    FqName(USER_CLASS),
    false,
)
internal val botClassId = ClassId(
    FqName(BOT_PACKAGE),
    FqName(BOT_CLASS),
    false,
)

internal fun IrType.isActionType(context: IrPluginContext): Boolean =
    isType(context, actionClassId) || isType(context, simpleActionClassId)

internal fun IrType.isUserType(context: IrPluginContext): Boolean =
    isType(context, userClassId)

internal fun IrType.isTelegramBotType(context: IrPluginContext): Boolean =
    isType(context, botClassId)

internal fun IrType.isType(context: IrPluginContext, classId: ClassId): Boolean {
    val classSymbol = context.referenceClass(classId) ?: return false
    return isSubtypeOfClass(classSymbol)
}

@OptIn(UnsafeDuringIrConstructionAPI::class)
internal fun Collection<IrSimpleFunctionSymbol>.seekSendFunction(
    context: IrPluginContext,
    isSimpleAction: Boolean = false,
) = first {
    // make sure it's fire&forget variant of send
    it.owner.returnType == context.irBuiltIns.unitType &&
        if (!isSimpleAction) {
            it.owner.valueParameters.size == 2 &&
                it.owner.valueParameters
                    .first()
                    .type
                    .isUserType(context) &&
                it.owner.valueParameters
                    .last()
                    .type
                    .isTelegramBotType(context)
        } else {
            it.owner.valueParameters.size == 1 &&
                it.owner.valueParameters
                    .first()
                    .type
                    .isTelegramBotType(context)
        }
}
