@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class GetMeAction : SimpleAction<User>() {
    @TgAPI.Name("getMe")
    override val method = "getMe"
    override val returnType = getReturnType()
}

/**
 * A simple method for testing your bot's authentication token. Requires no parameters. Returns basic information about the bot in form of a User object.
 *
 * [Api reference](https://core.telegram.org/bots/api#getme)
 *
 * @returns [User]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun getMe() = GetMeAction()
