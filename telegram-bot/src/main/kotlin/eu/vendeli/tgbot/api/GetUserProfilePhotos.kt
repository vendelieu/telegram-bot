@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.UserProfilePhotos
import eu.vendeli.tgbot.types.internal.TgMethod

class GetUserProfilePhotosAction(
    userId: Long,
    offset: Int? = null,
    limit: Int? = null,
) : SimpleAction<UserProfilePhotos> {
    override val method: TgMethod = TgMethod("getUserProfilePhotos")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
        if (offset != null) parameters["offset"] = offset
        if (limit != null) parameters["limit"] = limit
    }
}

fun getUserProfilePhotos(userId: Long, offset: Int? = null, limit: Int? = null) =
    GetUserProfilePhotosAction(userId, offset, limit)
