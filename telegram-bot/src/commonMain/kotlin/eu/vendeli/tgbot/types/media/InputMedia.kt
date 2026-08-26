package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.helper.ImplicitMediaData
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.utils.serde.DurationSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.time.Duration

/**
 * This object represents the content of a media message to be sent. It should be one of
 * - InputMediaAnimation
 * - InputMediaDocument
 * - InputMediaAudio
 * - InputMediaPhoto
 * - InputMediaVideo
 * - InputMediaLivePhoto
 *
 * [Api reference](https://core.telegram.org/bots/api#inputmedia)
 *
 */
@Serializable
@Suppress("OVERRIDE_DEPRECATION")
sealed class InputMedia : ImplicitMediaData {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("audio")
    @TgAPI.Name("InputMediaAudio")
    data class Audio(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val duration: Int? = null,
        val performer: String? = null,
        val title: String? = null,
    ) : InputMedia(),
        InputPollMedia,
        InputRichMedia

    @Serializable
    @SerialName("document")
    @TgAPI.Name("InputMediaDocument")
    data class Document(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val disableContentTypeDetection: Boolean? = null,
    ) : InputMedia(),
        InputPollMedia,
        InputRichMedia

    @Serializable
    @SerialName("photo")
    @TgAPI.Name("InputMediaPhoto")
    data class Photo(
        override var media: ImplicitFile,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val hasSpoiler: Boolean? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InputMedia(),
        InputPollMedia,
        InputPollOptionMedia,
        InputRichMedia

    @Serializable
    @SerialName("video")
    @TgAPI.Name("InputMediaVideo")
    data class Video(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val cover: ImplicitFile? = null,
        @Serializable(DurationSerializer::class)
        val startTimestamp: Duration? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val supportsStreaming: Boolean? = null,
        val hasSpoiler: Boolean? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InputMedia(),
        InputPollMedia,
        InputPollOptionMedia,
        InputRichMedia

    @Serializable
    @SerialName("animation")
    @TgAPI.Name("InputMediaAnimation")
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
    ) : InputMedia(),
        InputPollMedia,
        InputPollOptionMedia,
        InputRichMedia

    @Serializable
    @SerialName("live_photo")
    @TgAPI.Name("InputMediaLivePhoto")
    data class LivePhoto(
        override var media: ImplicitFile,
        var photo: ImplicitFile,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val showCaptionAboveMedia: Boolean? = null,
        val hasSpoiler: Boolean? = null,
    ) : InputMedia(),
        InputPollMedia,
        InputPollOptionMedia
}
