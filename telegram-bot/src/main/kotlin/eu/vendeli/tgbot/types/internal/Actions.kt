package eu.vendeli.tgbot.types.internal

internal data class Actions(
    val commands: Map<String, Invocation>,
    val inputs: Map<String, Invocation>,
    val regexCommands: Map<Regex, Invocation>,
    val updateHandlers: Map<UpdateType, Invocation>,
    val unhandled: Invocation? = null,
)
