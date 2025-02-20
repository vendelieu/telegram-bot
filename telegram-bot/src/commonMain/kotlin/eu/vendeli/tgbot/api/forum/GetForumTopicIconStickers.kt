@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.media.Sticker
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class GetForumTopicIconStickersAction : Action<List<Sticker>>() {
    @TgAPI.Name("getForumTopicIconStickers")
    override val method = "getForumTopicIconStickers"
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
@TgAPI
inline fun getForumTopicIconStickers() = GetForumTopicIconStickersAction()
