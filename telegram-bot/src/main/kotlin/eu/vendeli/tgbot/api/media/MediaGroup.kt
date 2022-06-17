package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.*
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.InputMedia
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.MediaGroupOptions

class SendMediaGroupAction :
    Action<List<Message>>,
    OptionAble,
    OptionsFeature<SendMediaGroupAction, MediaGroupOptions>,
    MultiResponseOf<Message> {
    override val method: TgMethod = TgMethod("sendMediaGroup")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
    override var options = MediaGroupOptions()

    constructor(vararg media: InputMedia.Audio) {
        parameters["media"] = media
    }

    constructor(vararg media: InputMedia.Document) {
        parameters["media"] = media
    }

    constructor(vararg media: InputMedia.Photo) {
        parameters["media"] = media
    }

    constructor(vararg media: InputMedia.Video) {
        parameters["media"] = media
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : MultipleResponse> TgAction.bunchResponseInnerType(): Class<T> = getInnerType() as Class<T>
}

fun mediaGroup(vararg media: InputMedia.Audio) = SendMediaGroupAction(*media)
fun mediaGroup(vararg media: InputMedia.Document) = SendMediaGroupAction(*media)
fun mediaGroup(vararg media: InputMedia.Photo) = SendMediaGroupAction(*media)
fun mediaGroup(vararg media: InputMedia.Video) = SendMediaGroupAction(*media)
