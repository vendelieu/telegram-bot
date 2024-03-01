@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PollOptions
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.serialization.builtins.serializer

class SendPollAction(question: String, pollOptions: List<String>) :
    Action<Message>(),
    OptionsFeature<SendPollAction, PollOptions>,
    MarkupFeature<SendPollAction> {
    override val method = TgMethod("sendPoll")
    override val returnType = getReturnType()
    override val options = PollOptions()

    init {
        parameters["question"] = question.toJsonElement()
        parameters["options"] = pollOptions.encodeWith(String.serializer())
    }
}

/**
 * Use this method to send a native poll. On success, the sent Message is returned.
 * Api reference: https://core.telegram.org/bots/api#sendpoll
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param question Poll question, 1-300 characters
 * @param options A JSON-serialized list of answer options, 2-10 strings 1-100 characters each
 * @param isAnonymous True, if the poll needs to be anonymous, defaults to True
 * @param type Poll type, "quiz" or "regular", defaults to "regular"
 * @param allowsMultipleAnswers True, if the poll allows multiple answers, ignored for polls in quiz mode, defaults to False
 * @param correctOptionId 0-based identifier of the correct answer option, required for polls in quiz mode
 * @param explanation Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters with at most 2 line feeds after entities parsing
 * @param explanationParseMode Mode for parsing entities in the explanation. See formatting options for more details.
 * @param explanationEntities A JSON-serialized list of special entities that appear in the poll explanation, which can be specified instead of parse_mode
 * @param openPeriod Amount of time in seconds the poll will be active after creation, 5-600. Can't be used together with close_date.
 * @param closeDate Point in time (Unix timestamp) when the poll will be automatically closed. Must be at least 5 and no more than 600 seconds in the future. Can't be used together with open_period.
 * @param isClosed Pass True if the poll needs to be immediately closed. This can be useful for poll preview.
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
 * @returns [Message]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun poll(question: String, options: List<String>) = SendPollAction(question, options)
fun poll(question: String, options: ListingBuilder<String>.() -> Unit) = poll(question, ListingBuilder.build(options))

@Suppress("NOTHING_TO_INLINE")
inline fun poll(question: String, vararg options: String) = poll(question, options.toList())
fun sendPoll(question: String, options: ListingBuilder<String>.() -> Unit) = poll(question, options)
