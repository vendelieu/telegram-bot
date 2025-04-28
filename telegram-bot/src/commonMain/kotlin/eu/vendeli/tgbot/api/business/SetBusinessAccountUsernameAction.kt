@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.business

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SetBusinessAccountUsernameAction(
    businessConnectionId: String,
    username: String? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setBusinessAccountUsername")
    override val method = "setBusinessAccountUsername"
    override val returnType = getReturnType()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        username?.let { parameters["username"] = it.toJsonElement() }
    }
}

/**
 * Changes the username of a managed business account. Requires the can_change_username business bot right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setbusinessaccountusername)
 * @param businessConnectionId Unique identifier of the business connection
 * @param username The new value of the username for the business account; 0-32 characters
 * @returns [Boolean]
 */
@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun setBusinessAccountUsername(
    businessConnectionId: String,
    username: String? = null,
) = SetBusinessAccountUsernameAction(businessConnectionId, username)
