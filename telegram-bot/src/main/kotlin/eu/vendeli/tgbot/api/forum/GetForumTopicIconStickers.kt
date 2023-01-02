@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.Sticker
import eu.vendeli.tgbot.types.internal.TgMethod

/**
 * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user.
 * Requires no parameters. Returns an Array of Sticker objects.
 */
class GetForumTopicIconStickersAction : SimpleAction<Sticker> {
    override val method: TgMethod = TgMethod("getForumTopicIconStickers")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

/**
 * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user.
 * Requires no parameters. Returns an Array of Sticker objects.
 */
fun getForumTopicIconStickers() = GetForumTopicIconStickersAction()
