@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.gift

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GiftPremiumSubscriptionAction(
    userId: Long,
    monthCount: Int,
    starCount: Int,
    text: String? = null,
    textParseMode: ParseMode? = null,
) : SimpleAction<Boolean>(),
    @TgAPI.Name("textEntities")
    EntitiesFeature<GiftPremiumSubscriptionAction> {
    @TgAPI.Name("giftPremiumSubscription")
    override val method = "giftPremiumSubscription"
    override val returnType = getReturnType()
    override val entitiesFieldName = "text_entities"

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["month_count"] = monthCount.toJsonElement()
        parameters["star_count"] = starCount.toJsonElement()
        if (text != null) parameters["text"] = text.toJsonElement()
        if (textParseMode != null) parameters["text_parse_mode"] = textParseMode.encodeWith(ParseMode.serializer())
    }
}

/**
 * Gifts a Telegram Premium subscription to the given user. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#giftpremiumsubscription)
 * @param userId Unique identifier of the target user who will receive a Telegram Premium subscription
 * @param monthCount Number of months the Telegram Premium subscription will be active for the user; must be one of 3, 6, or 12
 * @param starCount Number of Telegram Stars to pay for the Telegram Premium subscription; must be 1000 for 3 months, 1500 for 6 months, and 2500 for 12 months
 * @param text Text that will be shown along with the service message about the subscription; 0-128 characters
 * @param textParseMode Mode for parsing entities in the text. See formatting options for more details. Entities other than "bold", "italic", "underline", "strikethrough", "spoiler", and "custom_emoji" are ignored.
 * @param textEntities A JSON-serialized list of special entities that appear in the gift text. It can be specified instead of text_parse_mode. Entities other than "bold", "italic", "underline", "strikethrough", "spoiler", and "custom_emoji" are ignored.
 * @returns [Boolean]
 */
@TgAPI
@Suppress("NOTHING_TO_INLINE")
fun giftPremiumSubscription(
    userId: Long,
    monthCount: Int,
    starCount: Int,
    textParseMode: ParseMode? = null,
    text: (() -> String)? = null,
) = GiftPremiumSubscriptionAction(userId, monthCount, starCount, text?.invoke(), textParseMode)
