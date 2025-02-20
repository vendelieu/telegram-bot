package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.types.common.ResponseParameters
import kotlinx.coroutines.Deferred
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("ok")
@OptIn(ExperimentalSerializationApi::class)
sealed class Response<T>(
    val ok: Boolean,
) {
    @Serializable
    @SerialName("true")
    data class Success<T>(
        val result: T,
    ) : Response<T>(true)

    @Serializable
    @SerialName("false")
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

/**
 * Handles request's failure case.
 * @return a fail-safe response.
 */
suspend inline fun <T> Deferred<Response<out T>>.onFailure(block: (Response.Failure) -> Unit): T? =
    await().onFailure(block)

/**
 * Whether the request completed successfully.
 */
fun <T> Response<T>.isSuccess(): Boolean = this is Response.Success

/**
 * Get response or null on failure.
 *
 * @return response payload
 */
fun <T> Response<T>.getOrNull(): T? = when (this) {
    is Response.Success<T> -> result
    else -> null
}

suspend inline fun <T> Deferred<Response<out T>>.getOrNull(): T? = await().getOrNull()

/**
 * Handles response success and failure cases.
 */
@Suppress("UNCHECKED_CAST")
suspend inline fun <T, R> Deferred<Response<out T>>.foldResponse(
    success: Response.Success<T>.() -> R,
    failure: Response.Failure.() -> R,
): R = when (val response = await()) {
    is Response.Success<*> -> success.invoke(response as Response.Success<T>)
    is Response.Failure -> failure.invoke(response)
}
