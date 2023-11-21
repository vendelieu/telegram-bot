package eu.vendeli.tgbot.types.internal

internal data class Actions(
    val commands: Map<String, Invocation>,
    val inputs: Map<String, Invocation>,
    val regexCommands: Map<Regex, Invocation>,
    val updateHandlers: Map<UpdateType, Invocation>,
    val unhandled: Invocation? = null,
)

internal enum class ActionType(private val literal: String) {
    COMMAND("Command"),
    REGEX_COMMAND("Regex command"),
    INPUT("Input"),
    TYPE_HANDLER("Type handler"),
    UNPROCESSED_HANDLER("Unprocessed handler"),
    ;

    override fun toString(): String = literal
}
