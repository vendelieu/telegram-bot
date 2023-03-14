@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.Sticker
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user.
 * Requires no parameters. Returns an Array of Sticker objects.
 */
class GetForumTopicIconStickersAction : SimpleAction<Sticker>, ActionState() {
    override val TgAction<Sticker>.method: TgMethod
        get() = TgMethod("getForumTopicIconStickers")
    override val TgAction<Sticker>.returnType: Class<Sticker>
        get() = getReturnType()
}

/**
 * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user.
 * Requires no parameters. Returns an Array of Sticker objects.
 */
fun getForumTopicIconStickers() = GetForumTopicIconStickersAction()
