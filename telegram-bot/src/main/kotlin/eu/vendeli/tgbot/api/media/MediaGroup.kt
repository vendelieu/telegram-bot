@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.ImplicitFile.FromString
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.Recipient
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.MediaGroupOptions
import eu.vendeli.tgbot.types.internal.toContentType
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.makeBunchMediaRequestAsync
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentBunchMediaRequest
import eu.vendeli.tgbot.utils.makeSilentRequest
import io.ktor.util.reflect.instanceOf
import kotlin.collections.set
import kotlinx.coroutines.Deferred

class SendMediaGroupAction(private vararg val inputMedia: InputMedia) :
    MediaAction<List<Message>>,
    ActionState(),
    OptionsFeature<SendMediaGroupAction, MediaGroupOptions> {
    private val isAllMediaFromString = inputMedia.all { it.media.instanceOf(FromString::class) }
    private val files by lazy { mutableMapOf<String, ByteArray>() }
    override val method: TgMethod = TgMethod("sendMediaGroup")
    override val returnType = getReturnType()
    override val OptionsFeature<SendMediaGroupAction, MediaGroupOptions>.options: MediaGroupOptions
        get() = MediaGroupOptions()

    init {
        // check api restricts
        val mediaType = inputMedia.first().type
        require(inputMedia.all { it.type == mediaType && it.type != "animation" }) {
            "All elements must be of the same specific type and animation is not supported by telegram api"
        }

        // reorganize the media following appropriate approaches
        if (isAllMediaFromString) parameters[dataField] = inputMedia
        else parameters[dataField] = buildList {
            inputMedia.forEach {
                if (it.media.instanceOf(FromString::class)) {
                    add(it)
                    return@forEach
                }
                val fileName = it.media.name ?: "$dataField.$defaultType"
                files[fileName] = it.media.bytes
                it.media = FromString("attach://$fileName")

                add(it)
            }
        }
    }

    override val MediaAction<List<Message>>.defaultType: MediaContentType
        get() = when (inputMedia.first()) {
            is InputMedia.Audio -> MediaContentType.Audio
            is InputMedia.Document -> MediaContentType.Text
            is InputMedia.Photo -> MediaContentType.ImageJpeg
            is InputMedia.Video -> MediaContentType.VideoMp4
            else -> throw IllegalArgumentException("Only Audio/Document/Photo/Video is possible.")
        }
    override val MediaAction<List<Message>>.media: ImplicitFile<*>
        get() = media
    override val MediaAction<List<Message>>.dataField: String
        get() = "media"

    override suspend fun sendAsync(
        to: String,
        via: TelegramBot,
    ): Deferred<Response<out List<Message>>> = internalSendAsync(returnType, Recipient.String(to), via)

    override suspend fun sendAsync(
        to: User,
        via: TelegramBot,
    ): Deferred<Response<out List<Message>>> = internalSendAsync(returnType, Recipient.Long(to.id), via)

    override suspend fun sendAsync(
        to: Long,
        via: TelegramBot,
    ): Deferred<Response<out List<Message>>> = internalSendAsync(returnType, Recipient.Long(to), via)

    override suspend fun MediaAction<List<Message>>.internalSend(
        to: Recipient,
        via: TelegramBot,
    ) {
        parameters["chat_id"] = to.get()

        if (isAllMediaFromString) {
            via.makeSilentRequest(method, parameters)
            return
        }

        return via.makeSilentBunchMediaRequest(method, files, parameters, defaultType.toContentType())
    }

    private suspend inline fun internalSendAsync(
        returnType: Class<List<Message>>,
        to: Recipient,
        via: TelegramBot,
    ): Deferred<Response<out List<Message>>> {
        parameters["chat_id"] = to.get()

        if (isAllMediaFromString)
            return via.makeRequestAsync(method, parameters, returnType, wrappedDataType)

        return via.makeBunchMediaRequestAsync(
            method,
            files,
            parameters = parameters,
            defaultType.toContentType(),
            returnType,
            Message::class.java,
        )
    }
}

fun mediaGroup(vararg media: InputMedia.Audio) = SendMediaGroupAction(*media)
fun mediaGroup(vararg media: InputMedia.Document) = SendMediaGroupAction(*media)
fun mediaGroup(vararg media: InputMedia.Photo) = SendMediaGroupAction(*media)
fun mediaGroup(vararg media: InputMedia.Video) = SendMediaGroupAction(*media)
