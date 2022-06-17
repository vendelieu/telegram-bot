package eu.vendeli.tgbot.types

enum class ChatAction(private val literal: String) {
    Typing("typing"), UploadPhoto("upload_photo"), RecordVideo("record_video"),
    UploadVideo("upload_video"), RecordVoice("record_voice"), UploadVoice("upload_voice"),
    UploadDocument("upload_document"), ChooseSticker("choose_sticker"), FindLocation("find_location"),
    RecordVideoNote("record_video_note"), UploadVideoNote("upload_video_note");

    override fun toString(): String = literal
}
