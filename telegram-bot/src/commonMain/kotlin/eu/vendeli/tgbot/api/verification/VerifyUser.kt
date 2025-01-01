@file:Suppress("MatchingDeclarationName", "ktlint:standard:filename")

package eu.vendeli.tgbot.api.verification

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class VerifyUserAction(
    userId: Long,
    customDescription: String? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("verifyUser")
    override val method = "verifyUser"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        customDescription?.let { parameters["custom_description"] = it.toJsonElement() }
    }
}

/**
 * Verifies a user on behalf of the organization which is represented by the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#verifyuser)
 * @param userId Unique identifier of the target user
 * @param customDescription Custom description for the verification; 0-70 characters. Must be empty if the organization isn't allowed to provide a custom verification description.
 * @returns [Boolean]
 */
@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun verifyUser(userId: Long, customDescription: String? = null): VerifyUserAction =
    VerifyUserAction(userId, customDescription)

@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun verifyUser(userId: Long, customDescription: () -> String? = { null }): VerifyUserAction =
    verifyUser(userId, customDescription())

@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun verifyUser(user: User, customDescription: () -> String? = { null }): VerifyUserAction =
    verifyUser(user.id, customDescription())
