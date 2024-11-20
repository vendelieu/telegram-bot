@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SendGiftAction(
    userId: Long,
    giftId: String,
    textParseMode: ParseMode? = null,
    text: (() -> String)? = null,
) : SimpleAction<Boolean>(),
    EntitiesFeature<SendGiftAction> {
    @TgAPI.Name("sendGift")
    override val method = "sendGift"
    override val returnType = getReturnType()
    override val entitiesFieldName = "text_entities"

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["gift_id"] = giftId.toJsonElement()
        textParseMode?.let { parameters["text_parse_mode"] = it.name.toJsonElement() }
        text?.let { parameters["text"] = it().toJsonElement() }
    }
}

/**
 * Sends a gift to the given user. The gift can't be converted to Telegram Stars by the user. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendgift)
 * @param userId Unique identifier of the target user that will receive the gift
 * @param giftId Identifier of the gift
 * @param text Text that will be shown along with the gift; 0-255 characters
 * @param textParseMode Mode for parsing entities in the text. See formatting options for more details. Entities other than "bold", "italic", "underline", "strikethrough", "spoiler", and "custom_emoji" are ignored.
 * @param textEntities A JSON-serialized list of special entities that appear in the gift text. It can be specified instead of text_parse_mode. Entities other than "bold", "italic", "underline", "strikethrough", "spoiler", and "custom_emoji" are ignored.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun sendGift(
    userId: Long,
    giftId: String,
    textParseMode: ParseMode? = null,
    noinline text: (() -> String)? = null,
) = SendGiftAction(userId, giftId, textParseMode, text)
