package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ChatAction {
    @SerialName("typing")
    Typing,

    @SerialName("upload_photo")
    UploadPhoto,

    @SerialName("record_video")
    RecordVideo,

    @SerialName("upload_video")
    UploadVideo,

    @SerialName("record_voice")
    RecordVoice,

    @SerialName("upload_voice")
    UploadVoice,

    @SerialName("upload_document")
    UploadDocument,

    @SerialName("choose_sticker")
    ChooseSticker,

    @SerialName("find_location")
    FindLocation,

    @SerialName("record_video_note")
    RecordVideoNote,

    @SerialName("upload_video_note")
    UploadVideoNote,
}
