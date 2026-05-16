package eu.vendeli.tgbot.types.poll

import eu.vendeli.tgbot.types.common.Location
import eu.vendeli.tgbot.types.common.Venue
import eu.vendeli.tgbot.types.media.Animation
import eu.vendeli.tgbot.types.media.Audio
import eu.vendeli.tgbot.types.media.Document
import eu.vendeli.tgbot.types.media.LivePhoto
import eu.vendeli.tgbot.types.media.PhotoSize
import eu.vendeli.tgbot.types.media.Sticker
import eu.vendeli.tgbot.types.media.Video
import kotlinx.serialization.Serializable

/**
 * This object describes the media to be displayed in a poll. At most one of the optional fields can be present in any given object.
 *
 * [Api reference](https://core.telegram.org/bots/api#pollmedia)
 * @property animation Optional. Media is an animation, information about the animation
 * @property audio Optional. Media is an audio file, information about the file; currently, can't be received in a poll option
 * @property document Optional. Media is a general file, information about the file; currently, can't be received in a poll option
 * @property livePhoto Optional. Media is a live photo, information about the live photo
 * @property location Optional. Media is a shared location, information about the location
 * @property photo Optional. Media is a photo, available sizes of the photo
 * @property sticker Optional. Media is a sticker, information about the sticker; currently, for poll options only
 * @property venue Optional. Media is a venue, information about the venue
 * @property video Optional. Media is a video, information about the video
 */
@Serializable
data class PollMedia(
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val livePhoto: LivePhoto? = null,
    val location: Location? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val venue: Venue? = null,
    val video: Video? = null,
)
