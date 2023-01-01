@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod

/**
 * A simple method for testing your bot's authentication token.
 * Requires no parameters. Returns basic information about the bot in form of a [User](User) object.
 *
 */
class GetMeAction : SimpleAction<User> {
    override val method: TgMethod = TgMethod("getMe")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

/**
 * A simple method for testing your bot's authentication token. Requires no parameters.
 * Returns basic information about the bot in form of a [User](User) object.
 *
 */
fun getMe() = GetMeAction()
