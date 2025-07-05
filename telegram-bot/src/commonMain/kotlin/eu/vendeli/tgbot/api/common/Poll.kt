@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.common

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.options.PollOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.poll.InputPollOption
import eu.vendeli.tgbot.utils.builders.PollOptionsBuilder
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SendPollAction(
    question: String,
    options: List<InputPollOption>,
) : Action<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendPollAction, PollOptions>,
    @TgAPI.Name("questionEntities")
    EntitiesFeature<SendPollAction>,
    MarkupFeature<SendPollAction> {
    @TgAPI.Name("sendPoll")
    override val method = "sendPoll"
    override val returnType = getReturnType()
    override val options = PollOptions()
    override val entitiesFieldName: String = "question_entities"

    init {
        parameters["question"] = question.toJsonElement()
        parameters["options"] = options.encodeWith(InputPollOption.serializer())
    }
}

/**
 * Use this method to send a native poll. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendpoll)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param question Poll question, 1-300 characters
 * @param questionParseMode Mode for parsing entities in the question. See formatting options for more details. Currently, only custom emoji entities are allowed
 * @param questionEntities A JSON-serialized list of special entities that appear in the poll question. It can be specified instead of question_parse_mode
 * @param options A JSON-serialized list of 2-12 answer options
 * @param isAnonymous True, if the poll needs to be anonymous, defaults to True
 * @param type Poll type, "quiz" or "regular", defaults to "regular"
 * @param allowsMultipleAnswers True, if the poll allows multiple answers, ignored for polls in quiz mode, defaults to False
 * @param correctOptionId 0-based identifier of the correct answer option, required for polls in quiz mode
 * @param explanation Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters with at most 2 line feeds after entities parsing
 * @param explanationParseMode Mode for parsing entities in the explanation. See formatting options for more details.
 * @param explanationEntities A JSON-serialized list of special entities that appear in the poll explanation. It can be specified instead of explanation_parse_mode
 * @param openPeriod Amount of time in seconds the poll will be active after creation, 5-600. Can't be used together with close_date.
 * @param closeDate Point in time (Unix timestamp) when the poll will be automatically closed. Must be at least 5 and no more than 600 seconds in the future. Can't be used together with open_period.
 * @param isClosed Pass True if the poll needs to be immediately closed. This can be useful for poll preview.
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@TgAPI
inline fun poll(question: String, options: List<InputPollOption>) = SendPollAction(question, options)

@TgAPI
fun poll(
    question: String,
    options: PollOptionsBuilder.() -> Unit,
) = poll(question, PollOptionsBuilder.build(options))

@TgAPI
inline fun poll(question: String, vararg options: InputPollOption) = poll(question, options.toList())

@TgAPI
fun sendPoll(question: String, options: PollOptionsBuilder.() -> Unit) = poll(question, options)
