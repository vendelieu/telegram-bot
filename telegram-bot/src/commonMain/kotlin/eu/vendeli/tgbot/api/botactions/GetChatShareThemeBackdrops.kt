@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.common.ChatShareThemeBackdrops
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class GetChatShareThemeBackdropsAction : SimpleAction<ChatShareThemeBackdrops>() {
    @TgAPI.Name("getChatShareThemeBackdrops")
    override val method = "getChatShareThemeBackdrops"
    override val returnType = getReturnType()
}

/**
 * Use this method to get the list of backdrops, which can be used as the background of a theme of the screen for sharing a link to a user or a chat. Requires no parameters. Returns a ChatShareThemeBackdrops object.
 *
 * [Api reference](https://core.telegram.org/bots/api#getchatsharethemebackdrops)
 *
 * @returns [ChatShareThemeBackdrops]
 */
@TgAPI
inline fun getChatShareThemeBackdrops() = GetChatShareThemeBackdropsAction()
