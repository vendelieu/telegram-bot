package eu.vendeli.tgbot.interfaces

/**
 * Interface to mark that the data may be of a multiple nature.
 *
 */
interface MultipleResponse

/**
 * Interface to pass a nested type in a multiple response.
 *
 * @param Type
 */
interface WrappedTypeOf<Type : MultipleResponse>

/**
 * Extension function to get a nested type.
 *
 * @param Type
 * @return
 */
@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type : MultipleResponse> WrappedTypeOf<Type>.getInnerType(): Class<Type> =
    Type::class.java
