@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.business

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SetBusinessAccountNameAction(
    businessConnectionId: String,
    firstName: String,
    lastName: String? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setBusinessAccountName")
    override val method = "setBusinessAccountName"
    override val returnType = getReturnType()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["first_name"] = firstName.toJsonElement()
        lastName?.let { parameters["last_name"] = it.toJsonElement() }
    }
}

/**
 * Changes the first and last name of a managed business account. Requires the can_change_name business bot right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setbusinessaccountname)
 * @param businessConnectionId Unique identifier of the business connection
 * @param firstName The new value of the first name for the business account; 1-64 characters
 * @param lastName The new value of the last name for the business account; 0-64 characters
 * @returns [Boolean]
 */
@TgAPI
inline fun setBusinessAccountName(
    businessConnectionId: String,
    firstName: String,
    lastName: String? = null,
) = SetBusinessAccountNameAction(businessConnectionId, firstName, lastName)
