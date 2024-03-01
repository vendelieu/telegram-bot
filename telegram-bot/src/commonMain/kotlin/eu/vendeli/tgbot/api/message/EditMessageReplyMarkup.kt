@file:Suppress("MatchingDeclarationName", "TooManyFunctions")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement


class EditMessageReplyMarkupAction() :
    InlinableAction<Message>(),
    MarkupFeature<EditMessageReplyMarkupAction> {
    override val method = TgMethod("editMessageReplyMarkup")
    override val returnType = getReturnType()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageReplyMarkup() = editMarkup()

@Suppress("NOTHING_TO_INLINE")
inline fun editMarkup(messageId: Long) = EditMessageReplyMarkupAction(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun editMarkup() = EditMessageReplyMarkupAction()

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageReplyMarkup(messageId: Long) = editMarkup(messageId)
