package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.media.PhotoSize
import kotlinx.serialization.Serializable

/**
 * This object represent a user's profile pictures.
 * @property totalCount Total number of profile pictures the target user has
 * @property photos Requested profile pictures (in up to 4 sizes each)
 * Api reference: https://core.telegram.org/bots/api#userprofilephotos
*/
@Serializable
data class UserProfilePhotos(
    val totalCount: Int,
    val photos: List<List<PhotoSize>>,
)
