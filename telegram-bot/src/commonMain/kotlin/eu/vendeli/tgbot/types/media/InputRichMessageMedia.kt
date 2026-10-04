package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * Describes a media element embedded in an outgoing rich message.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputrichmessagemedia)
 * @property id Unique identifier of the media used in a tg://photo?id=, tg://video?id=, tg://document?id=, or tg://audio?id= link. 1-64 characters, only A-Z, a-z, 0-9, _ and - are allowed.
 * @property media The media to be sent. Everything except the media itself and its properties is ignored.
 */
@Serializable
data class InputRichMessageMedia(
    val id: String,
    val media: InputRichMedia,
)
