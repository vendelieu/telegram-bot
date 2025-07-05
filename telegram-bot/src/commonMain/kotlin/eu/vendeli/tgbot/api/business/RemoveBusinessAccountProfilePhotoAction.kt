@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.business

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class RemoveBusinessAccountProfilePhotoAction(
    businessConnectionId: String,
    isPublic: Boolean? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("removeBusinessAccountProfilePhoto")
    override val method = "removeBusinessAccountProfilePhoto"
    override val returnType = getReturnType()
    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        isPublic?.let { parameters["is_public"] = it.toJsonElement() }
    }
}

/**
 * Removes the current profile photo of a managed business account. Requires the can_edit_profile_photo business bot right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#removebusinessaccountprofilephoto)
 * @param businessConnectionId Unique identifier of the business connection
 * @param isPublic Pass True to remove the public photo, which is visible even if the main photo is hidden by the business account's privacy settings. After the main photo is removed, the previous profile photo (if present) becomes the main photo.
 * @returns [Boolean]
 */
@TgAPI
inline fun removeBusinessAccountProfilePhoto(
    businessConnectionId: String,
    isPublic: Boolean? = null,
) = RemoveBusinessAccountProfilePhotoAction(businessConnectionId, isPublic)
