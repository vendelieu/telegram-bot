package eu.vendeli.tgbot.types.internal

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import eu.vendeli.tgbot.types.ResponseParameters
import kotlinx.coroutines.Deferred

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "ok",
    defaultImpl = Response.Success::class,
)
@JsonSubTypes(
    JsonSubTypes.Type(value = Response.Success::class, name = "true"),
    JsonSubTypes.Type(value = Response.Failure::class, name = "false"),
)
sealed class Response<T>(val ok: Boolean) {
    data class Success<T>(
        val result: T,
    ) : Response<T>(true)

    data class Failure(
        val errorCode: Int,
        val description: String? = null,
        val parameters: ResponseParameters? = null,
    ) : Response<Nothing>(false)
}
inline fun <T> Response<T>.onFailure(block: (Response.Failure) -> Unit): T? = when (this) {
    is Response.Success<T> -> result
    is Response.Failure -> {
        block(this)
        null
    }
}
suspend inline fun <T> Deferred<Response<T>>.onFailure(block: (Response.Failure) -> Unit): T? = await().onFailure(block)
fun <T> Response<T>.isSuccess(): Boolean = this is Response.Success
fun <T> Response<T>.getOrNull(): T? = when (this) {
    is Response.Success<T> -> result
    else -> null
}
suspend inline fun <T> Deferred<Response<out T>>.getOrNull(): T? = await().getOrNull()

@Suppress("UNCHECKED_CAST")
suspend inline fun <T, R> Deferred<Response<out T>>.foldResponse(
    success: Response.Success<T>.() -> R,
    failure: Response.Failure.() -> R,
): R = when (val response = await()) {
    is Response.Success<*> -> success.invoke(response as Response.Success<T>)
    is Response.Failure -> failure.invoke(response)
}
