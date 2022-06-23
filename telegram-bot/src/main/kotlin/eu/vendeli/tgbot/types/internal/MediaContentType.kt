package eu.vendeli.tgbot.types.internal

import io.ktor.http.*

enum class MediaContentType(private val literal: String) {
    Text("txt"), ImageJpeg("jpg"), ImagePng("png"), Audio("mp3"), Voice("ogg"),
    VideoMp4("mp4"), ImageGif("gif"), ImageTgs("tgs"), VideoWebm("webm");

    override fun toString(): String = literal
}

/**
 * Converts the type to the appropriate ContentType.
 */
fun MediaContentType.toContentType(): ContentType = when (this) {
    MediaContentType.Text -> ContentType.Text.Plain
    MediaContentType.ImageJpeg -> ContentType.Image.JPEG
    MediaContentType.ImagePng -> ContentType.Image.PNG
    MediaContentType.Audio -> ContentType.Audio.MPEG
    MediaContentType.Voice -> ContentType.Audio.OGG
    MediaContentType.VideoMp4 -> ContentType.Video.MP4
    MediaContentType.ImageGif -> ContentType.Image.GIF
    MediaContentType.ImageTgs -> ContentType.Application.Json
    MediaContentType.VideoWebm -> ContentType.parse("video/webm")
}
