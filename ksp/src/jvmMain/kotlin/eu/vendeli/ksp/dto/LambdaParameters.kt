package eu.vendeli.ksp.dto

sealed class LambdaParameters

sealed class CallbackQueryParams : LambdaParameters() {
    data object AutoAnswer : CallbackQueryParams()
}

