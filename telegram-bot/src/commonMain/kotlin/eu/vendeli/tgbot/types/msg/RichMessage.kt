package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * Rich formatted message.
 *
 * [Api reference](https://core.telegram.org/bots/api#richmessage)
 * @property blocks Content of the message
 * @property isRtl Optional. True, if the rich message must be shown right-to-left
 */
@Serializable
data class RichMessage(
    val blocks: List<RichBlock>,
    val isRtl: Boolean? = null,
)
