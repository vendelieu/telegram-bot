@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.Sticker
import eu.vendeli.tgbot.utils.getReturnType

class GetForumTopicIconStickersAction : Action<List<Sticker>>() {
    override val method = TgMethod("getForumTopicIconStickers")
    override val returnType = getReturnType()
}

/**
 * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user. Requires no parameters. Returns an Array of Sticker objects.
 *
 * [Api reference](https://core.telegram.org/bots/api#getforumtopiciconstickers)
 *
 * @returns [Array of Sticker]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun getForumTopicIconStickers() = GetForumTopicIconStickersAction()
