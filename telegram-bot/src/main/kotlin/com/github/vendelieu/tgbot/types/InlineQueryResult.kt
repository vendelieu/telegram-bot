package com.github.vendelieu.tgbot.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(value = InlineQueryResult.Article::class),
    JsonSubTypes.Type(value = InlineQueryResult.Photo::class),
    JsonSubTypes.Type(value = InlineQueryResult.Gif::class),
    JsonSubTypes.Type(value = InlineQueryResult.Mpeg4Gif::class),
    JsonSubTypes.Type(value = InlineQueryResult.Video::class),
    JsonSubTypes.Type(value = InlineQueryResult.Audio::class),
    JsonSubTypes.Type(value = InlineQueryResult.Voice::class),
    JsonSubTypes.Type(value = InlineQueryResult.Document::class),
    JsonSubTypes.Type(value = InlineQueryResult.Location::class),
    JsonSubTypes.Type(value = InlineQueryResult.Venue::class),
    JsonSubTypes.Type(value = InlineQueryResult.Contact::class),
    JsonSubTypes.Type(value = InlineQueryResult.Game::class),
    JsonSubTypes.Type(value = InlineQueryResult.CachedPhoto::class),
    JsonSubTypes.Type(value = InlineQueryResult.CachedGif::class),
    JsonSubTypes.Type(value = InlineQueryResult.CachedMpeg4Gif::class),
    JsonSubTypes.Type(value = InlineQueryResult.CachedSticker::class),
    JsonSubTypes.Type(value = InlineQueryResult.CachedDocument::class),
    JsonSubTypes.Type(value = InlineQueryResult.CachedVideo::class),
    JsonSubTypes.Type(value = InlineQueryResult.CachedVoice::class),
    JsonSubTypes.Type(value = InlineQueryResult.CachedAudio::class),
)
sealed class InlineQueryResult(val type: String) {
    data class Article(
        val id: String,
        val title: String,
        val inputMessageContent: InputMessageContent,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val url: String? = null,
        val hideUrl: Boolean? = null,
        val description: String? = null,
        val thumbUrl: String? = null,
        val thumbWidth: Int? = null,
        val thumbHeight: Int? = null,
    ) : InlineQueryResult("article")

    data class Photo(
        val id: String,
        val photoUrl: String,
        val thumbUrl: String,
        val photoWidth: Int? = null,
        val title: String? = null,
        val description: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("photo")

    data class Gif(
        val id: String,
        val gifUrl: String,
        val gifWidth: Int? = null,
        val gifHeight: Int? = null,
        val gifDuration: Int? = null,
        val thumbUrl: String,
        val thumbMimeType: String,
        val title: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("gif")

    data class Mpeg4Gif(
        val id: String,
        val mpeg4Url: String,
        val mpeg4Width: Int? = null,
        val mpeg4Height: Int? = null,
        val mpeg4Duration: Int? = null,
        val thumbUrl: String,
        val thumbMimeType: String,
        val title: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("mpeg4_gif")

    data class Video(
        val id: String,
        val videoUrl: String,
        val mimeType: String,
        val thumbUrl: String,
        val title: String,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val videoWidth: Int? = null,
        val videoHeight: Int? = null,
        val videoDuration: Int? = null,
        val description: String? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("video")

    data class Audio(
        val id: String,
        val audioUrl: String,
        val title: String,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val performer: String? = null,
        val audioDuration: Int? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("audio")

    data class Voice(
        val id: String,
        val voiceUrl: String,
        val title: String,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val voiceDuration: Int? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("voice")

    data class Document(
        val id: String,
        val title: String,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val documentUrl: String,
        val mimeType: String,
        val description: String? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
        val thumbUrl: String? = null,
        val thumbWidth: Int? = null,
        val thumbHeight: Int? = null,
    ) : InlineQueryResult("document")

    data class Location(
        val id: String,
        val longitude: Float,
        val latitude: Float,
        val title: String,
        val horizontalAccuracy: Float? = null,
        val livePeriod: Int? = null,
        val heading: Int? = null,
        val proximityAlertRadius: Int? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
        val thumbUrl: String? = null,
        val thumbWidth: Int? = null,
        val thumbHeight: Int? = null,
    ) : InlineQueryResult("location")

    data class Venue(
        val id: String,
        val longitude: Float,
        val latitude: Float,
        val title: String,
        val address: String,
        val foursquareId: String? = null,
        val fourSquareType: String? = null,
        val googlePlaceId: String? = null,
        val googlePlaceType: String? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
        val thumbUrl: String? = null,
        val thumbWidth: Int? = null,
        val thumbHeight: Int? = null,
    ) : InlineQueryResult("venue")

    data class Contact(
        val id: String,
        val title: String,
        val phoneNumber: String,
        val firstName: String,
        val lastName: String? = null,
        val userId: Long? = null,
        val vcard: String? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
        val thumbUrl: String? = null,
        val thumbWidth: Int? = null,
        val thumbHeight: Int? = null,
    ) : InlineQueryResult("contact")

    data class Game(
        val id: String,
        val gameShortName: String,
        val replyMarkup: InlineKeyboardMarkup? = null,
    ) : InlineQueryResult("game")

    data class CachedPhoto(
        val id: String,
        val photoFileId: String,
        val title: String? = null,
        val description: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("photo")

    data class CachedGif(
        val id: String,
        val gifFileId: String,
        val title: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("gif")

    data class CachedMpeg4Gif(
        val id: String,
        val mpeg4FileId: String,
        val title: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("mpeg4_gif")

    data class CachedSticker(
        val id: String,
        val stickerFileId: String,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("sticker")

    data class CachedDocument(
        val id: String,
        val title: String,
        val documentFileId: String,
        val description: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("document")

    data class CachedVideo(
        val id: String,
        val videoFileId: String,
        val title: String,
        val description: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("video")

    data class CachedVoice(
        val id: String,
        val voiceFileId: String,
        val title: String,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("voice")

    data class CachedAudio(
        val id: String,
        val audioFileId: String,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: InputMessageContent? = null,
    ) : InlineQueryResult("audio")
}