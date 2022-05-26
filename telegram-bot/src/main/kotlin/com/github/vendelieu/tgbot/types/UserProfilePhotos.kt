package com.github.vendelieu.tgbot.types

data class UserProfilePhotos(
    val totalCount: Int,
    val photos: List<List<PhotoSize>>,
)
