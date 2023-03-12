package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.media.PhotoSize

data class UserProfilePhotos(
    val totalCount: Int,
    val photos: List<List<PhotoSize>>,
)
