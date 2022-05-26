package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.UserProfilePhotos
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetUserProfilePhotosAction(
    userId: Long,
    offset: Int? = null,
    limit: Int? = null,
) : SimpleAction<UserProfilePhotos> {
    override val method: TgMethod = TgMethod("getUserProfilePhotos")
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["user_id"] = userId
        if (offset != null) parameters["offset"] = offset
        if (limit != null) parameters["limit"] = limit
    }
}

fun getUserProfilePhotos(userId: Long, offset: Int? = null, limit: Int? = null) =
    GetUserProfilePhotosAction(userId, offset, limit)
