package eu.vendeli.tgbot.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import eu.vendeli.tgbot.types.internal.ImplicitFile

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = InputMedia.Audio::class, name = "audio"),
    JsonSubTypes.Type(value = InputMedia.Document::class, name = "document"),
    JsonSubTypes.Type(value = InputMedia.Photo::class, name = "photo"),
    JsonSubTypes.Type(value = InputMedia.Video::class, name = "video"),
    JsonSubTypes.Type(value = InputMedia.Animation::class, name = "animation"),
)
sealed class InputMedia(val type: String) {
    data class Audio(
        val media: String,
        val thumb: ImplicitFile<*>? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val duration: Int? = null,
        val performer: String? = null,
        val title: String? = null,
    ) : InputMedia(type = "audio")

    data class Document(
        val media: String,
        val thumb: ImplicitFile<*>? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val disableTypeDetection: Boolean? = null,
    ) : InputMedia(type = "document")

    data class Photo(
        val media: String,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
    ) : InputMedia(type = "photo")

    data class Video(
        val media: String,
        val thumb: ImplicitFile<*>? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val supportsStreaming: Boolean? = null,
    ) : InputMedia(type = "video")

    data class Animation(
        val media: String,
        val thumb: ImplicitFile<*>? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
    ) : InputMedia(type = "animation")
}
