package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.serde.IdentifierSerializer
import kotlinx.serialization.Serializable

@Serializable(IdentifierSerializer::class)
sealed class Identifier {
    abstract val get: Any

    @Serializable
    data class String(val to: kotlin.String) : Identifier() {
        override val get: kotlin.String get(): kotlin.String = to
    }

    @Serializable
    data class Long(val to: kotlin.Long) : Identifier() {
        override val get: kotlin.Long get(): kotlin.Long = to
    }

    companion object {
        fun from(recipient: kotlin.Long) = Long(recipient)
        fun from(recipient: kotlin.String) = String(recipient)

        fun from(recipient: User) = Long(recipient.id)
    }
}
