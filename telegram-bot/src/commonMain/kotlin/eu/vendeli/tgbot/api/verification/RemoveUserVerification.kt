@file:Suppress("MatchingDeclarationName", "ktlint:standard:filename")

package eu.vendeli.tgbot.api.verification

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class RemoveUserVerificationAction(
    userId: Long,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("removeUserVerification")
    override val method = "removeUserVerification"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Removes verification from a user who is currently verified on behalf of the organization represented by the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#removeuserverification)
 * @param userId Unique identifier of the target user
 * @returns [Boolean]
 */
@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun removeUserVerification(userId: Long): RemoveUserVerificationAction =
    RemoveUserVerificationAction(userId)

@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun removeUserVerification(user: User): RemoveUserVerificationAction =
    removeUserVerification(user.id)
