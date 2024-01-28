@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile.InpFile
import eu.vendeli.tgbot.types.internal.ImplicitFile.Str
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.MediaGroupOptions
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.toPartData
import kotlinx.serialization.json.JsonElement
import kotlin.collections.set

class SendMediaGroupAction(private val inputMedia: List<InputMedia>) :
    MediaAction<List<Message>>(),
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

        // reorganize the media following appropriate approaches
        parameters["media"] = buildList {
            inputMedia.forEach {
                if (it.media is Str) {
                    add(it.encodeWith(DynamicLookupSerializer))
                    return@forEach
                }
                val media = it.media as InpFile
                it.media = Str("attach://${media.file.fileName}")

                multipartData += media.file.toPartData(media.file.fileName)
                add(it.encodeWith(DynamicLookupSerializer))
            }
        }.encodeWith(JsonElement.serializer())
    }
}

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
