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
    giftId: String,
    userId: Long? = null,
    payForUpgrade: Boolean? = null,
    textParseMode: ParseMode? = null,
    text: String? = null,
) : SimpleAction<Boolean>(),
    @TgAPI.Name("textEntities")
    EntitiesFeature<SendGiftAction> {
    @TgAPI.Name("sendGift")
    override val method = "sendGift"
    override val returnType = getReturnType()
    override val entitiesFieldName = "text_entities"

    init {
        parameters["gift_id"] = giftId.toJsonElement()
        userId?.let { parameters["user_id"] = it.toJsonElement() }
        payForUpgrade?.let { parameters["pay_for_upgrade"] = it.toJsonElement() }
        textParseMode?.let { parameters["text_parse_mode"] = it.name.toJsonElement() }
        text?.let { parameters["text"] = it.toJsonElement() }
    }
}

/**
 * Sends a gift to the given user or channel chat. The gift can't be converted to Telegram Stars by the receiver. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendgift)
 * @param userId Required if chat_id is not specified. Unique identifier of the target user who will receive the gift.
 * @param chatId Required if user_id is not specified. Unique identifier for the chat or username of the channel (in the format @channelusername) that will receive the gift.
 * @param giftId Identifier of the gift
 * @param payForUpgrade Pass True to pay for the gift upgrade from the bot's balance, thereby making the upgrade free for the receiver
 * @param text Text that will be shown along with the gift; 0-128 characters
 * @param textParseMode Mode for parsing entities in the text. See formatting options for more details. Entities other than "bold", "italic", "underline", "strikethrough", "spoiler", and "custom_emoji" are ignored.
 * @param textEntities A JSON-serialized list of special entities that appear in the gift text. It can be specified instead of text_parse_mode. Entities other than "bold", "italic", "underline", "strikethrough", "spoiler", and "custom_emoji" are ignored.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun sendGift(
    giftId: String,
    userId: Long? = null,
    payForUpgrade: Boolean? = null,
    textParseMode: ParseMode? = null,
    text: () -> String? = { null },
) = SendGiftAction(giftId, userId, payForUpgrade, textParseMode, text())
