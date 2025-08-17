package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.common.LinkPreviewOptions
import eu.vendeli.tgbot.types.common.ReplyParameters
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.msg.SuggestedPostParameters
import eu.vendeli.tgbot.types.story.StoryArea
import kotlinx.serialization.Serializable

@Serializable
sealed interface Options

interface OptionsParseMode : Options {
    var parseMode: ParseMode?
}

interface MediaSpoiler : Options {
    var hasSpoiler: Boolean?
}

interface LinkPreviewProp : Options {
    var linkPreviewOptions: LinkPreviewOptions?

    fun disableWebPagePreview() {
        linkPreviewOptions = LinkPreviewOptions(isDisabled = true)
    }

    fun linkPreviewOptions(block: LinkPreviewOptions.() -> Unit) {
        linkPreviewOptions = LinkPreviewOptions().apply(block)
    }
}

interface ThumbnailProp : Options {
    var thumbnail: ImplicitFile?
}

interface MessageEffectIdProp : Options {
    var messageEffectId: String?
}

interface ShowCaptionAboveMediaProp : Options {
    var showCaptionAboveMedia: Boolean?
}

interface ForumProps : Options {
    var messageThreadId: Int?
}

interface ProtectContentProp : Options {
    var protectContent: Boolean?
}

interface AreasProp : Options {
    var areas: List<StoryArea>?
}

interface AllowPaidBroadcastProp : Options {
    var allowPaidBroadcast: Boolean?
}

interface DirectMessagesTopicProp : Options {
    var directMessagesTopicId: Int?
}

interface SuggestedPostParametersProp : Options {
    var suggestedPostParameters: SuggestedPostParameters?
}

@Serializable
sealed interface OptionsCommon :
    Options,
    ProtectContentProp {
    var disableNotification: Boolean?
    var replyParameters: ReplyParameters?
    override var protectContent: Boolean?

    var replyToMessageId: Long?
        get() = replyParameters?.messageId
        set(value) {
            value?.also { replyParameters(it) }
        }

    fun replyParameters(messageId: Long, block: ReplyParameters.() -> Unit = {}) {
        replyParameters = ReplyParameters(messageId).apply(block)
    }
}
