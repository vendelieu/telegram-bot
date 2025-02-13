@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.Identifier
import eu.vendeli.tgbot.types.internal.options.CopyMessageOptions
import eu.vendeli.tgbot.types.msg.MessageId
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class CopyMessageAction(
    fromChatId: Identifier,
    messageId: Long,
) : Action<MessageId>(),
    OptionsFeature<CopyMessageAction, CopyMessageOptions>,
    MarkupFeature<CopyMessageAction>,
    CaptionFeature<CopyMessageAction> {
    @TgAPI.Name("copyMessage")
    override val method = "copyMessage"
    override val returnType = getReturnType()
    override val options = CopyMessageOptions()
    override val entitiesFieldName: String = "caption_entities"

    init {
        parameters["from_chat_id"] = fromChatId.encodeWith(DynamicLookupSerializer)
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to copy messages of any kind. Service messages, paid media messages, giveaway messages, giveaway winners messages, and invoice messages can't be copied. A quiz poll can be copied only if the value of the field correct_option_id is known to the bot. The method is analogous to the method forwardMessage, but the copied message doesn't have a link to the original message. Returns the MessageId of the sent message on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#copymessage)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername)
 * @param messageId Message identifier in the chat specified in from_chat_id
 * @param videoStartTimestamp New start timestamp for the copied video in the message
 * @param caption New caption for media, 0-1024 characters after entities parsing. If not specified, the original caption is kept
 * @param parseMode Mode for parsing entities in the new caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the new caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Pass True, if the caption must be shown above the message media. Ignored if a new caption isn't specified.
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [MessageId]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun copyMessage(fromChatId: Identifier, messageId: Long) = CopyMessageAction(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun copyMessage(fromChatId: Long, messageId: Long) = copyMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun copyMessage(fromChatId: String, messageId: Long) = copyMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun copyMessage(fromChatId: User, messageId: Long) = copyMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun copyMessage(fromChatId: Chat, messageId: Long) = copyMessage(Identifier.from(fromChatId.id), messageId)
