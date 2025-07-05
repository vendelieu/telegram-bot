@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.business

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SetBusinessAccountBioAction(
    businessConnectionId: String,
    bio: String? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setBusinessAccountBio")
    override val method = "setBusinessAccountBio"
    override val returnType = getReturnType()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        bio?.let { parameters["bio"] = it.toJsonElement() }
    }
}

/**
 * Changes the bio of a managed business account. Requires the can_change_bio business bot right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setbusinessaccountbio)
 * @param businessConnectionId Unique identifier of the business connection
 * @param bio The new value of the bio for the business account; 0-140 characters
 * @returns [Boolean]
 */
@TgAPI
inline fun setBusinessAccountBio(
    businessConnectionId: String,
    bio: String? = null,
) = SetBusinessAccountBioAction(businessConnectionId, bio)
