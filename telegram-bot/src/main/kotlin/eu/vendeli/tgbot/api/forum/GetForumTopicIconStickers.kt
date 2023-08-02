@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.Sticker
import eu.vendeli.tgbot.utils.getInnerType
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user.
 * Requires no parameters. Returns an Array of Sticker objects.
 */
class GetForumTopicIconStickersAction : Action<List<Sticker>>, ActionState() {
    override val TgAction<List<Sticker>>.method: TgMethod
        get() = TgMethod("getForumTopicIconStickers")
    override val TgAction<List<Sticker>>.returnType: Class<List<Sticker>>
        get() = getReturnType()
    override val TgAction<List<Sticker>>.wrappedDataType: Class<Sticker>
        get() = getInnerType()
}

/**
 * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user.
 * Requires no parameters. Returns an Array of Sticker objects.
 */
fun getForumTopicIconStickers() = GetForumTopicIconStickersAction()
