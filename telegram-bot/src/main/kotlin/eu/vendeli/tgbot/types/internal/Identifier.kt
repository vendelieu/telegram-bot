package eu.vendeli.tgbot.types.internal

sealed class Identifier {
    abstract val get: Any

    data class String(val to: kotlin.String) : Identifier() {
        override val get get(): Any = to
    }

    data class Long(val to: kotlin.Long) : Identifier() {
        override val get get(): Any = to
    }

    companion object {
        fun from(recipient: kotlin.Long) = Long(recipient)
        fun from(recipient: kotlin.String) = String(recipient)
    }
}
