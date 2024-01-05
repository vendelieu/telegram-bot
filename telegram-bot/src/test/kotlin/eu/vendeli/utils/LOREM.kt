@file:Suppress("ktlint:standard:class-naming")

package eu.vendeli.utils

import eu.vendeli.tgbot.types.internal.InputFile
import io.kotest.common.runBlocking
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.random.Random

sealed class LOREM(val url: String, fileName: String, contentType: String) {
    val bytes: ByteArray = runBlocking { httpClient.get(url).readBytes() }
    val file: File = runBlocking {
        withContext(Dispatchers.IO) {
            val tempFile = File.createTempFile("test-$rand", "")
            tempFile.writeBytes(bytes)
            tempFile
        }
    }
    val inputFile = InputFile(data = bytes, fileName = fileName, contentType = contentType)

    data object AUDIO : LOREM(
        "https://github.com/malcomio/dummy-content/raw/master/audio/audio.mp3",
        "audio.mp3",
        "audio/mpeg",
    )
    data object VIDEO : LOREM(
        "https://github.com/malcomio/dummy-content/raw/master/video/small.mp4",
        "small.mp4",
        "video/mp4",
    )
    data object VIDEO_NOTE : LOREM("https://rb.gy/y0egi", "golden-ratio-240px.mp4", "video/mp4")
    data object VOICE : LOREM(
        "https://github.com/rafaelreis-hotmart/Audio-Sample-files/raw/master/sample.ogg",
        "sample.ogg",
        "audio/ogg",
    )
    data object STICKER : LOREM(
        "CAACAgIAAxkBAAEJ389kyGaEy1pMz1FN_h6F5Eh6jvneNQACxBcAAtXH-EsCpevGYhPo5S8E",
        "sticker.png",
        "image/png",
    )
    data object ANIMATION :
        LOREM(
            "https://github.com/malcomio/dummy-content/blob/master/images/animated-parabola.gif?raw=true",
            "parabola.gif",
            "image/gif",
        )

    data object DOCUMENT : LOREM(
        "https://github.com/malcomio/dummy-content/blob/master/Lorem_ipsum.pdf?raw=true",
        "ipsum.pdf",
        "application/pdf",
    )

    private companion object {
        val httpClient = HttpClient()
        val rand: String
            get() {
                return Random.nextBytes(10).toString()
            }
    }
}
