package com.github.vendelieu.tgbot.api.media

import com.github.vendelieu.tgbot.interfaces.*
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.InputMedia
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.MediaGroupOptions

class SendMediaGroupAction :
    Action<List<Message>>,
    OptionAble,
    OptionsFeature<SendMediaGroupAction, MediaGroupOptions>,
    MultiResponseOf<Message> {
    override val method: TgMethod = TgMethod("sendMediaGroup")
    override val parameters: MutableMap<String, Any> = mutableMapOf()
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
