@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.user.UserProfileAudios
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetUserProfileAudiosAction(
    userId: Long,
    offset: Int? = null,
    limit: Int? = null,
) : SimpleAction<UserProfileAudios>() {
    @TgAPI.Name("getUserProfileAudios")
    override val method = "getUserProfileAudios"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        if (offset != null) parameters["offset"] = offset.toJsonElement()
        if (limit != null) parameters["limit"] = limit.toJsonElement()
    }
}

/**
 * Use this method to get a list of profile audios for a user. Returns a UserProfileAudios object.
 *
 * [Api reference](https://core.telegram.org/bots/api#getuserprofileaudios)
 * @param userId Unique identifier of the target user
 * @param offset Sequential number of the first audio to be returned. By default, all audios are returned.
 * @param limit Limits the number of audios to be retrieved. Values between 1-100 are accepted. Defaults to 100.
 * @returns [UserProfileAudios]
 */
@TgAPI
inline fun getUserProfileAudios(userId: Long, offset: Int? = null, limit: Int? = null) =
    GetUserProfileAudiosAction(userId, offset, limit)

@TgAPI
inline fun getUserProfileAudios(user: User, offset: Int? = null, limit: Int? = null) =
    getUserProfileAudios(user.id, offset, limit)
