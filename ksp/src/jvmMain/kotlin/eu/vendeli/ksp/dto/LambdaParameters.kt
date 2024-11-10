package eu.vendeli.ksp.dto

sealed class LambdaParameters

sealed class CommandHandlerParams : LambdaParameters() {
    data object CallbackQueryAutoAnswer : CommandHandlerParams()
}

