package eu.vendeli.tgbot.types.internal

sealed class Identifier<T : Any> {
    abstract val get: T

    data class String(val to: kotlin.String) : Identifier<kotlin.String>() {
        override val get: kotlin.String get(): kotlin.String = to
    }

    data class Long(val to: kotlin.Long) : Identifier<kotlin.Long>() {
        override val get: kotlin.Long get(): kotlin.Long = to
    }

    companion object {
        fun from(recipient: kotlin.Long) = Long(recipient)
        fun from(recipient: kotlin.String) = String(recipient)
    }
}
