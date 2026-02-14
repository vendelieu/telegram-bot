package eu.vendeli.tgbot.types.user

import eu.vendeli.tgbot.types.media.Audio
import kotlinx.serialization.Serializable

/**
 * This object represents the audios displayed on a user's profile.
 *
 * [Api reference](https://core.telegram.org/bots/api#userprofileaudios)
 * @property totalCount Total number of profile audios for the target user
 * @property audios Requested profile audios
 */
@Serializable
data class UserProfileAudios(
    val totalCount: Int,
    val audios: List<Audio>,
)
