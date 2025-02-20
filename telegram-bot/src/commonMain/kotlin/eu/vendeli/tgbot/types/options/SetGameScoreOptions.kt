package eu.vendeli.tgbot.types.options

import kotlinx.serialization.Serializable

@Serializable
data class SetGameScoreOptions(
    var force: Boolean? = null,
    var disableEditMessage: Boolean? = null,
) : Options
