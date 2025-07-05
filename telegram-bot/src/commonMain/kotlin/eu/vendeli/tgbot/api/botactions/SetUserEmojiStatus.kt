@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement
import kotlin.time.Instant

@TgAPI
class SetUserEmojiStatusAction(
    userId: Long,
    emojiStatusCustomEmojiId: String? = null,
    emojiStatusExpirationDate: Instant? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setUserEmojiStatus")
    override val method = "setUserEmojiStatus"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        emojiStatusCustomEmojiId?.toJsonElement()?.let { parameters["emoji_status_custom_emoji_id"] = it }
        emojiStatusExpirationDate?.epochSeconds?.toJsonElement()?.let {
            parameters["emoji_status_expiration_date"] = it
        }
    }
}

/**
 * Changes the emoji status for a given user that previously allowed the bot to manage their emoji status via the Mini App method requestEmojiStatusAccess. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setuseremojistatus)
 * @param userId Unique identifier of the target user
 * @param emojiStatusCustomEmojiId Custom emoji identifier of the emoji status to set. Pass an empty string to remove the status.
 * @param emojiStatusExpirationDate Expiration date of the emoji status, if any
 * @returns [Boolean]
 */
@TgAPI
inline fun setUserEmojiStatus(
    userId: Long,
    emojiStatusCustomEmojiId: String? = null,
    emojiStatusExpirationDate: Instant? = null,
) = SetUserEmojiStatusAction(userId, emojiStatusCustomEmojiId, emojiStatusExpirationDate)
