@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class RemoveMyProfilePhotoAction : SimpleAction<Boolean>() {
    @TgAPI.Name("removeMyProfilePhoto")
    override val method = "removeMyProfilePhoto"
    override val returnType = getReturnType()
}

/**
 * Removes the profile photo of the bot. Requires no parameters. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#removemyprofilephoto)
 *
 * @returns [Boolean]
 */
@TgAPI
inline fun removeMyProfilePhoto() = RemoveMyProfilePhotoAction()
