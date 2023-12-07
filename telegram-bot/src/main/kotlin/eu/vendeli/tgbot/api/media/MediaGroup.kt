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
import eu.vendeli.tgbot.utils.getInnerType
import eu.vendeli.tgbot.utils.getReturnType
import kotlin.collections.set

class SendMediaGroupAction(private vararg val inputMedia: InputMedia) :
    MediaAction<List<Message>>(),
    OptionsFeature<SendMediaGroupAction, MediaGroupOptions> {
    override val method = TgMethod("sendMediaGroup")
    override val returnType = getReturnType()
    override val wrappedDataType = getInnerType()
    override val OptionsFeature<SendMediaGroupAction, MediaGroupOptions>.options: MediaGroupOptions
        get() = MediaGroupOptions()
    override val inputFilePresence: Boolean
        get() = isInputFile
    private val isInputFile = inputMedia.any { it.media is InpFile }

    init {
        // check api restricts
        val mediaType = inputMedia.first().type
        require(inputMedia.all { it.type == mediaType && it.type != "animation" }) {
            "All elements must be of the same specific type and animation is not supported by telegram api"
        }

        // reorganize the media following appropriate approaches
        parameters["media"] = if (!inputFilePresence) inputMedia
        else buildList {
            inputMedia.forEach {
                if (it.media is Str) {
                    add(it)
                    return@forEach
                }
                val fileName = (it.media as InpFile).file.fileName
                parameters[fileName] = it.media
                it.media = Str("attach://$fileName")

                add(it)
            }
        }
    }
}

fun sendMediaGroup(media: InputMedia) = SendMediaGroupAction(media)
fun mediaGroup(vararg media: InputMedia.Audio) = SendMediaGroupAction(*media)
fun mediaGroup(vararg media: InputMedia.Document) = SendMediaGroupAction(*media)
fun mediaGroup(vararg media: InputMedia.Photo) = SendMediaGroupAction(*media)
fun mediaGroup(vararg media: InputMedia.Video) = SendMediaGroupAction(*media)
