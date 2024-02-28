package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.ImplicitFile
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
 * Api reference: https://core.telegram.org/bots/api#inputmedia
*/
@Serializable
sealed class InputMedia(val type: String) {
    abstract var media: ImplicitFile

    @Serializable
    @SerialName("audio")
    data class Audio(
        override var media: ImplicitFile,
        val thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val duration: Int? = null,
        val performer: String? = null,
        val title: String? = null,
    ) : InputMedia(type = "audio")

    @Serializable
    @SerialName("document")
    data class Document(
        override var media: ImplicitFile,
        val thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val disableTypeDetection: Boolean? = null,
    ) : InputMedia(type = "document")

    @Serializable
    @SerialName("photo")
    data class Photo(
        override var media: ImplicitFile,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val hasSpoiler: Boolean? = null,
    ) : InputMedia(type = "photo")

    @Serializable
    @SerialName("video")
    data class Video(
        override var media: ImplicitFile,
        val thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val supportsStreaming: Boolean? = null,
        val hasSpoiler: Boolean? = null,
    ) : InputMedia(type = "video")

    @Serializable
    @SerialName("animation")
    data class Animation(
        override var media: ImplicitFile,
        val thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val hasSpoiler: Boolean? = null,
    ) : InputMedia(type = "animation")
}
