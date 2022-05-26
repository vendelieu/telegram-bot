package com.github.vendelieu.tgbot.types

sealed class InputMedia(val type: String) {
    data class Audio(
        val media: String,
        val thumb: String,
        val caption: String? = null,
        val parseMode: String? = null,
        val captionEntities: List<MessageEntity>? = null,
        val duration: Int? = null,
        val performer: String? = null,
        val title: String? = null,
    ) : InputMedia(type = "audio")

    data class Document(
        val media: String,
        val thumb: String,
        val caption: String? = null,
        val parseMode: String? = null,
        val captionEntities: List<MessageEntity>? = null,
        val disableTypeDetection: Boolean? = null,
    ) : InputMedia(type = "document")

    data class Photo(
        val media: String,
        val caption: String? = null,
        val parseMode: String? = null,
        val captionEntities: List<MessageEntity>? = null,
    ) : InputMedia(type = "photo")

    data class Video(
        val media: String,
        val thumb: String,
        val caption: String? = null,
        val parseMode: String? = null,
        val captionEntities: List<MessageEntity>? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val supportsStreaming: Boolean? = null,
    ) : InputMedia(type = "video")

    data class Animation(
        val media: String,
        val thumb: String,
        val caption: String? = null,
        val parseMode: String? = null,
        val captionEntities: List<MessageEntity>? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
    ) : InputMedia(type = "animation")
}
