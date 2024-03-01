@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.UserProfilePhotos
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetUserProfilePhotosAction(
    userId: Long,
    offset: Int? = null,
    limit: Int? = null,
) : SimpleAction<UserProfilePhotos>() {
    override val method = TgMethod("getUserProfilePhotos")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        if (offset != null) parameters["offset"] = offset.toJsonElement()
        if (limit != null) parameters["limit"] = limit.toJsonElement()
    }
}

/**
 * Use this method to get a list of profile pictures for a user. Returns a UserProfilePhotos object.
 * Api reference: https://core.telegram.org/bots/api#getuserprofilephotos
 * @param userId Unique identifier of the target user
 * @param offset Sequential number of the first photo to be returned. By default, all photos are returned.
 * @param limit Limits the number of photos to be retrieved. Values between 1-100 are accepted. Defaults to 100.
 * @returns [UserProfilePhotos]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun getUserProfilePhotos(userId: Long, offset: Int? = null, limit: Int? = null) =
    GetUserProfilePhotosAction(userId, offset, limit)

@Suppress("NOTHING_TO_INLINE")
inline fun getUserProfilePhotos(user: User, offset: Int? = null, limit: Int? = null) =
    getUserProfilePhotos(user.id, offset, limit)
