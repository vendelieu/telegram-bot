@file:Suppress("ktlint:standard:class-naming")

package eu.vendeli.utils

import eu.vendeli.tgbot.types.component.InputFile
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.runBlocking
import utils.replay.TestMode
import java.io.File
import kotlin.random.Random

private const val PLACEHOLDER_SIZE = 64

sealed class LOREM(
    val url: String,
    private val fileName: String,
    contentType: String,
) {
    val bytes: ByteArray by lazy {
        if (TestMode.current.isReplay) PLACEHOLDER_BYTES else cachedDownload()
    }
    val file: File by lazy {
        File.createTempFile("test-$rand", "").apply {
            deleteOnExit()
            writeBytes(bytes)
        }
    }
    val inputFile by lazy { InputFile(data = bytes, fileName = fileName, contentType = contentType) }

    data object AUDIO : LOREM(
        "https://github.com/malcomio/dummy-content/raw/master/audio/audio.mp3?raw=true",
        "audio.mp3",
        "audio/mpeg",
    )

    data object VIDEO : LOREM(
        "https://github.com/malcomio/dummy-content/raw/master/video/small.mp4?raw=true",
        "small.mp4",
        "video/mp4",
    )

    data object VIDEO_NOTE : LOREM(
        "https://rx.vendeli.eu/files/2bd7bfd3-6821-40f5-a1f9-5138f7b81d14_file_2.mp4",
        "file.mp4",
        "video/mp4",
    )
    data object VOICE : LOREM(
        "https://github.com/rafaelreis-hotmart/Audio-Sample-files/raw/master/sample.ogg?raw=true",
        "sample.ogg",
        "audio/ogg",
    )

    data object STICKER : LOREM(
        "https://github.com/kuronekowen/Telegram-Sticker-Sample/blob/main/tgs/1.tgs?raw=true",
        "sticker.tgs",
        "application/x-tgsticker",
    )

    data object ANIMATION :
        LOREM(
            "https://github.com/malcomio/dummy-content/blob/master/images/animated-parabola.gif?raw=true",
            "animated-parabola.gif",
            "image/gif",
        )

    data object DOCUMENT : LOREM(
        "https://github.com/malcomio/dummy-content/blob/master/Lorem_ipsum.pdf?raw=true",
        "ipsum.pdf",
        "application/pdf",
    )

    /** Downloads once per machine, later runs (and parallel CI jobs) read the copy from the build directory. */
    private fun cachedDownload(): ByteArray {
        val cached = File(CACHE_DIR, fileName)
        if (cached.exists() && cached.length() > 0) return cached.readBytes()
        val downloaded = runBlocking { httpClient.get(url).readRawBytes() }
        cached.parentFile.mkdirs()
        cached.writeBytes(downloaded)
        return downloaded
    }

    private companion object {
        val CACHE_DIR = File(System.getProperty("java.io.tmpdir"), "ktgram-test-assets")
        val PLACEHOLDER_BYTES = ByteArray(PLACEHOLDER_SIZE) { it.toByte() }
        val httpClient = HttpClient()
        val rand: String
            get() {
                return Random.nextBytes(10).toString()
            }
    }
}
