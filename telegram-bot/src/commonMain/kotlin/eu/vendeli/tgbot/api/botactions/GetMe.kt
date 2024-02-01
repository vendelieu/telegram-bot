@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

/**
 * A simple method for testing your bot's authentication token.
 * Requires no parameters. Returns basic information about the bot in form of a [User](User) object.
 *
 */
class GetMeAction : SimpleAction<User>() {
    override val method = TgMethod("getMe")
    override val returnType = getReturnType()
}

/**
 * A simple method for testing your bot's authentication token. Requires no parameters.
 * Returns basic information about the bot in form of a [User](User) object.
 *
 */

inline fun getMe() = GetMeAction()
