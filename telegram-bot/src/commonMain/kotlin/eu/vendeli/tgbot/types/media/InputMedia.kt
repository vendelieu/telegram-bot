package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.interfaces.helper.ImplicitMediaData
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.utils.toImplicitFile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents the content of a media message to be sent. It should be one of
 * - InputMediaAnimation
 * - InputMediaDocument
 * - InputMediaAudio
 * - InputMediaPhoto
 * - InputMediaVideo
 *
 * [Api reference](https://core.telegram.org/bots/api#inputmedia)
 *
 */
@Serializable
@Suppress("OVERRIDE_DEPRECATION")
sealed class InputMedia(
    val type: String,
) : ImplicitMediaData {
    @Serializable
    @SerialName("audio")
    data class Audio(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val duration: Int? = null,
        val performer: String? = null,
        val title: String? = null,
    ) : InputMedia(type = "audio") {
        constructor(
            media: String,
            thumbnail: ImplicitFile? = null,
            caption: String? = null,
            parseMode: ParseMode? = null,
            captionEntities: List<MessageEntity>? = null,
            duration: Int? = null,
            performer: String? = null,
            title: String? = null,
        ) : this(media.toImplicitFile(), thumbnail, caption, parseMode, captionEntities, duration, performer, title)

        constructor(
            media: InputFile,
            thumbnail: ImplicitFile? = null,
            caption: String? = null,
            parseMode: ParseMode? = null,
            captionEntities: List<MessageEntity>? = null,
            duration: Int? = null,
            performer: String? = null,
            title: String? = null,
        ) : this(media.toImplicitFile(), thumbnail, caption, parseMode, captionEntities, duration, performer, title)
    }

    @Serializable
    @SerialName("document")
    data class Document(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val disableContentTypeDetection: Boolean? = null,
    ) : InputMedia(type = "document") {
        constructor(
            media: String,
            thumbnail: ImplicitFile? = null,
            caption: String? = null,
            parseMode: ParseMode? = null,
            captionEntities: List<MessageEntity>? = null,
            disableTypeDetection: Boolean? = null,
        ) : this(media.toImplicitFile(), thumbnail, caption, parseMode, captionEntities, disableTypeDetection)

        constructor(
            media: InputFile,
            thumbnail: ImplicitFile? = null,
            caption: String? = null,
            parseMode: ParseMode? = null,
            captionEntities: List<MessageEntity>? = null,
            disableTypeDetection: Boolean? = null,
        ) : this(media.toImplicitFile(), thumbnail, caption, parseMode, captionEntities, disableTypeDetection)
    }

    @Serializable
    @SerialName("photo")
    data class Photo(
        override var media: ImplicitFile,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val hasSpoiler: Boolean? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InputMedia(type = "photo") {
        constructor(
            media: String,
            caption: String? = null,
            parseMode: ParseMode? = null,
            captionEntities: List<MessageEntity>? = null,
            hasSpoiler: Boolean? = null,
            showCaptionAboveMedia: Boolean? = null,
        ) : this(media.toImplicitFile(), caption, parseMode, captionEntities, hasSpoiler, showCaptionAboveMedia)

        constructor(
            media: InputFile,
            caption: String? = null,
            parseMode: ParseMode? = null,
            captionEntities: List<MessageEntity>? = null,
            hasSpoiler: Boolean? = null,
            showCaptionAboveMedia: Boolean? = null,
        ) : this(media.toImplicitFile(), caption, parseMode, captionEntities, hasSpoiler, showCaptionAboveMedia)
    }

    @Serializable
    @SerialName("video")
    data class Video(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val supportsStreaming: Boolean? = null,
        val hasSpoiler: Boolean? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InputMedia(type = "video") {
        constructor(
            media: String,
            thumbnail: ImplicitFile? = null,
            caption: String? = null,
            parseMode: ParseMode? = null,
            captionEntities: List<MessageEntity>? = null,
            width: Int? = null,
            height: Int? = null,
            duration: Int? = null,
            supportsStreaming: Boolean? = null,
            hasSpoiler: Boolean? = null,
            showCaptionAboveMedia: Boolean? = null,
        ) : this(
            media.toImplicitFile(),
            thumbnail,
            caption,
            parseMode,
            captionEntities,
            width,
            height,
            duration,
            supportsStreaming,
            hasSpoiler,
            showCaptionAboveMedia,
        )

        constructor(
            media: InputFile,
            thumbnail: ImplicitFile? = null,
            caption: String? = null,
            parseMode: ParseMode? = null,
            captionEntities: List<MessageEntity>? = null,
            width: Int? = null,
            height: Int? = null,
            duration: Int? = null,
            supportsStreaming: Boolean? = null,
            hasSpoiler: Boolean? = null,
            showCaptionAboveMedia: Boolean? = null,
        ) : this(
            media.toImplicitFile(),
            thumbnail,
            caption,
            parseMode,
            captionEntities,
            width,
            height,
            duration,
            supportsStreaming,
            hasSpoiler,
            showCaptionAboveMedia,
        )
    }

    @Serializable
    @SerialName("animation")
    data class Animation(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val hasSpoiler: Boolean? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InputMedia(type = "animation") {
        constructor(
            media: String,
            thumbnail: ImplicitFile? = null,
            caption: String? = null,
            parseMode: ParseMode? = null,
            captionEntities: List<MessageEntity>? = null,
            width: Int? = null,
            height: Int? = null,
            duration: Int? = null,
            hasSpoiler: Boolean? = null,
            showCaptionAboveMedia: Boolean? = null,
        ) : this(
            media.toImplicitFile(),
            thumbnail,
            caption,
            parseMode,
            captionEntities,
            width,
            height,
            duration,
            hasSpoiler,
            showCaptionAboveMedia,
        )

        constructor(
            media: InputFile,
            thumbnail: ImplicitFile? = null,
            caption: String? = null,
            parseMode: ParseMode? = null,
            captionEntities: List<MessageEntity>? = null,
            width: Int? = null,
            height: Int? = null,
            duration: Int? = null,
            hasSpoiler: Boolean? = null,
            showCaptionAboveMedia: Boolean? = null,
        ) : this(
            media.toImplicitFile(),
            thumbnail,
            caption,
            parseMode,
            captionEntities,
            width,
            height,
            duration,
            hasSpoiler,
            showCaptionAboveMedia,
        )
    }
}
