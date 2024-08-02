@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.MediaAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.MediaGroupOptions
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFileGroup

class SendMediaGroupAction(
    inputMedia: List<InputMedia>,
) : MediaAction<List<Message>>(),
    BusinessActionExt<List<Message>>,
    OptionsFeature<SendMediaGroupAction, MediaGroupOptions> {
    override val method = TgMethod("sendMediaGroup")
    override val returnType = getReturnType()
    override val options = MediaGroupOptions()

    init {
        // check api restricts
        val mediaType = inputMedia.first().type
        require(inputMedia.all { it.type == mediaType && it.type != "animation" }) {
            "All elements must be of the same specific type and animation is not supported by telegram api"
        }

        handleImplicitFileGroup(inputMedia)
    }
}

/**
 * Use this method to send a group of photos, videos, documents or audios as an album. Documents and audio files can be only grouped in an album with messages of the same type. On success, an array of Messages that were sent is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendmediagroup)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param media A JSON-serialized array describing messages to be sent, must include 2-10 items
 * @param disableNotification Sends messages silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent messages from forwarding and saving
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @returns [Array of Message]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun sendMediaGroup(media: List<InputMedia>) = SendMediaGroupAction(media)

@Suppress("NOTHING_TO_INLINE")
inline fun sendMediaGroup(vararg media: InputMedia) = sendMediaGroup(media.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun mediaGroup(vararg media: InputMedia.Audio) = sendMediaGroup(media.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun mediaGroup(vararg media: InputMedia.Document) = sendMediaGroup(media.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun mediaGroup(vararg media: InputMedia.Photo) = sendMediaGroup(media.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun mediaGroup(vararg media: InputMedia.Video) = sendMediaGroup(media.asList())
