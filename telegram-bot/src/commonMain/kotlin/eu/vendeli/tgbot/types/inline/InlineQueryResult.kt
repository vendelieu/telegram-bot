@file:UseContextualSerialization(
    forClasses = [
        InlineQueryResult.CachedAudio::class,
        InlineQueryResult.CachedDocument::class,
        InlineQueryResult.CachedGif::class,
        InlineQueryResult.CachedMpeg4Gif::class,
        InlineQueryResult.CachedPhoto::class,
        InlineQueryResult.CachedSticker::class,
        InlineQueryResult.CachedVideo::class,
        InlineQueryResult.CachedVoice::class,
    ],
)

package eu.vendeli.tgbot.types.inline

import eu.vendeli.tgbot.types.InputMessageContent
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.types.msg.MessageEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import kotlinx.serialization.serializer

/**
 * This object represents one result of an inline query. Telegram clients currently support results of the following 20 types:
 * - InlineQueryResultCachedAudio
 * - InlineQueryResultCachedDocument
 * - InlineQueryResultCachedGif
 * - InlineQueryResultCachedMpeg4Gif
 * - InlineQueryResultCachedPhoto
 * - InlineQueryResultCachedSticker
 * - InlineQueryResultCachedVideo
 * - InlineQueryResultCachedVoice
 * - InlineQueryResultArticle
 * - InlineQueryResultAudio
 * - InlineQueryResultContact
 * - InlineQueryResultGame
 * - InlineQueryResultDocument
 * - InlineQueryResultGif
 * - InlineQueryResultLocation
 * - InlineQueryResultMpeg4Gif
 * - InlineQueryResultPhoto
 * - InlineQueryResultVenue
 * - InlineQueryResultVideo
 * - InlineQueryResultVoice
 * Note: All URLs passed in inline query results will be available to end users and therefore must be assumed to be public.
 *
 * [Api reference](https://core.telegram.org/bots/api#inlinequeryresult)
 *
 */
@Serializable
sealed class InlineQueryResult {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("article")
    data class Article(
        val id: String,
        val title: String,
        val inputMessageContent: @Contextual InputMessageContent,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val url: String? = null,
        val description: String? = null,
        val thumbnailUrl: String? = null,
        val thumbnailWidth: Int? = null,
        val thumbnailHeight: Int? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("photo")
    data class Photo(
        val id: String,
        val photoUrl: String,
        val thumbnailUrl: String,
        val photoWidth: Int? = null,
        val photoHeight: Int? = null,
        val title: String? = null,
        val description: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("gif")
    data class Gif(
        val id: String,
        val gifUrl: String,
        val gifWidth: Int? = null,
        val gifHeight: Int? = null,
        val gifDuration: Int? = null,
        val thumbnailUrl: String,
        val thumbnailMimeType: String,
        val title: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("mpeg4_gif")
    data class Mpeg4Gif(
        val id: String,
        val mpeg4Url: String,
        val mpeg4Width: Int? = null,
        val mpeg4Height: Int? = null,
        val mpeg4Duration: Int? = null,
        val thumbnailUrl: String,
        val thumbnailMimeType: String,
        val title: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("video")
    data class Video(
        val id: String,
        val videoUrl: String,
        val mimeType: String,
        val thumbnailUrl: String,
        val title: String,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val videoWidth: Int? = null,
        val videoHeight: Int? = null,
        val videoDuration: Int? = null,
        val description: String? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("audio")
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
        val inputMessageContent: @Contextual InputMessageContent? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("voice")
    data class Voice(
        val id: String,
        val voiceUrl: String,
        val title: String,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val voiceDuration: Int? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("document")
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
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val thumbnailUrl: String? = null,
        val thumbnailWidth: Int? = null,
        val thumbnailHeight: Int? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("location")
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
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val thumbnailUrl: String? = null,
        val thumbnailWidth: Int? = null,
        val thumbnailHeight: Int? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("venue")
    data class Venue(
        val id: String,
        val longitude: Float,
        val latitude: Float,
        val title: String,
        val address: String,
        val foursquareId: String? = null,
        val foursquareType: String? = null,
        val googlePlaceId: String? = null,
        val googlePlaceType: String? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val thumbnailUrl: String? = null,
        val thumbnailWidth: Int? = null,
        val thumbnailHeight: Int? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("contact")
    data class Contact(
        val id: String,
        val phoneNumber: String,
        val firstName: String,
        val lastName: String? = null,
        val vcard: String? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val thumbnailUrl: String? = null,
        val thumbnailWidth: Int? = null,
        val thumbnailHeight: Int? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("game")
    data class Game(
        val id: String,
        val gameShortName: String,
        val replyMarkup: InlineKeyboardMarkup? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("photo")
    data class CachedPhoto(
        val id: String,
        val photoFileId: String,
        val title: String? = null,
        val description: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("gif")
    data class CachedGif(
        val id: String,
        val gifFileId: String,
        val title: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("mpeg4_gif")
    data class CachedMpeg4Gif(
        val id: String,
        val mpeg4FileId: String,
        val title: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("sticker")
    data class CachedSticker(
        val id: String,
        val stickerFileId: String,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("document")
    data class CachedDocument(
        val id: String,
        val title: String,
        val documentFileId: String,
        val description: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("video")
    data class CachedVideo(
        val id: String,
        val videoFileId: String,
        val title: String,
        val description: String? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("voice")
    data class CachedVoice(
        val id: String,
        val voiceFileId: String,
        val title: String,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
    ) : InlineQueryResult()

    @Serializable
    @SerialName("audio")
    data class CachedAudio(
        val id: String,
        val audioFileId: String,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: InlineKeyboardMarkup? = null,
        val inputMessageContent: @Contextual InputMessageContent? = null,
    ) : InlineQueryResult()
}
