package eu.vendeli.tgbot.types.internal

data class Actions(
    val commands: Map<String, Invocation>,
    val inputs: Map<String, Invocation>,
    val updateHandlers: Map<UpdateType, Invocation>,
    val unhandled: Invocation? = null,
)
