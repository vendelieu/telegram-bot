package eu.vendeli.tgbot.types.inline

import kotlinx.serialization.Serializable

/**
 * This object represents an inline button that switches the current user to inline mode in a chosen chat, with an optional default inline query.
 *
 * Api reference: https://core.telegram.org/bots/api#switchinlinequerychosenchat
 * @property query Optional. The default inline query to be inserted in the input field. If left empty, only the bot's username will be inserted
 * @property allowUserChats Optional. True, if private chats with users can be chosen
 * @property allowBotChats Optional. True, if private chats with bots can be chosen
 * @property allowGroupChats Optional. True, if group and supergroup chats can be chosen
 * @property allowChannelChats Optional. True, if channel chats can be chosen
 */
@Serializable
data class SwitchInlineQueryChosenChat(
    val query: String? = null,
    val allowUserChats: Boolean? = null,
    val allowBotChats: Boolean? = null,
    val allowGroupChats: Boolean? = null,
    val allowChannelChats: Boolean? = null,
)
