package eu.vendeli.tgbot.types.internal.options

data class SetGameScoreOptions(
    var force: Boolean? = null,
    var disableEditMessage: Boolean? = null,
) : OptionsInterface<SetGameScoreOptions>
