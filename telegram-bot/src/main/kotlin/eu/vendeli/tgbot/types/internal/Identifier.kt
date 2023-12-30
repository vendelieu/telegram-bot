package eu.vendeli.tgbot.types.internal

import com.fasterxml.jackson.annotation.JsonValue
import eu.vendeli.tgbot.types.User

sealed class Identifier<T : Any> {
    @get:JsonValue
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

        fun from(recipient: User) = Long(recipient.id)
    }
}
