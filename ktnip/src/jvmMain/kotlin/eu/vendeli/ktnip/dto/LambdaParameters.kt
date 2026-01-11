package eu.vendeli.ktnip.dto

sealed class LambdaParameters

sealed class CommandHandlerParams : LambdaParameters() {
    data object CallbackQueryAutoAnswer : CommandHandlerParams()
}
