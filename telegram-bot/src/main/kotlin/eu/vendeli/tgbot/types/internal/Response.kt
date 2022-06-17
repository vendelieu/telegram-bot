package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.ResponseParameters

sealed class Response<out T>
data class Success<out T>(
    val ok: Boolean,
    val result: T,
) : Response<T>()

data class Failure(
    val ok: Boolean,
    val errorCode: Int,
    val description: String,
    val parameters: ResponseParameters? = null,
) : Response<Nothing>()

inline fun <T> Response<T>.onFailure(block: (Failure) -> T?): T? = when (this) {
    is Success<T> -> result
    is Failure -> block(this)
}

fun <T> Response<T>.isSuccess(): Boolean = this is Success
fun <T> Response<T>.getOrNull(): T? = when (this) {
    is Success<T> -> result
    else -> null
}
