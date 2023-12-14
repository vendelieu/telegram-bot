@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.UserProfilePhotos
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetUserProfilePhotosAction(
    userId: Long,
    offset: Int? = null,
    limit: Int? = null,
) : SimpleAction<UserProfilePhotos>() {
    override val method = TgMethod("getUserProfilePhotos")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId
        if (offset != null) parameters["offset"] = offset
        if (limit != null) parameters["limit"] = limit
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun getUserProfilePhotos(userId: Long, offset: Int? = null, limit: Int? = null) =
    GetUserProfilePhotosAction(userId, offset, limit)

@Suppress("NOTHING_TO_INLINE")
inline fun getUserProfilePhotos(user: User, offset: Int? = null, limit: Int? = null) =
    getUserProfilePhotos(user.id, offset, limit)
