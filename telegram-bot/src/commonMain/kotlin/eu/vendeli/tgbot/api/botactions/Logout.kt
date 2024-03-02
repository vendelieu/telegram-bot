@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class LogOutAction : SimpleAction<Boolean>() {
    override val method = TgMethod("logOut")
    override val returnType = getReturnType()
}

/**
 * Use this method to log out from the cloud Bot API server before launching the bot locally. You must log out the bot before running it locally, otherwise there is no guarantee that the bot will receive updates. After a successful call, you can immediately log in on a local server, but will not be able to log in back to the cloud Bot API server for 10 minutes. Returns True on success. Requires no parameters.
 * Api reference: https://core.telegram.org/bots/api#logout
 *
 * @returns [Boolean]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun logOut() = LogOutAction()
