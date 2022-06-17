package eu.vendeli.tgbot.types.internal

sealed class ActionRecipientRef {
    abstract fun get(): Any
    data class StringRecipient(val to: String) : ActionRecipientRef() {
        override fun get(): Any = to
    }

    data class LongRecipient(val to: Long) : ActionRecipientRef() {
        override fun get(): Any = to
    }
}
