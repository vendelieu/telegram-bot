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

sealed class LOREM(val text: String) {
    val bytes: ByteArray = runBlocking { httpClient.get(text).readBytes() }
    val file: File = runBlocking {
        withContext(Dispatchers.IO) {
            val tempFile = File.createTempFile("test-$rand", "")
            tempFile.writeBytes(bytes)
            tempFile
        }
    }
    val inputFile = InputFile(bytes)

    data object AUDIO : LOREM("https://github.com/malcomio/dummy-content/raw/master/audio/audio.mp3")
    data object VIDEO : LOREM("https://github.com/malcomio/dummy-content/raw/master/video/small.mp4")
    data object VIDEO_NOTE : LOREM("https://rb.gy/y0egi")
    data object VOICE : LOREM("https://github.com/rafaelreis-hotmart/Audio-Sample-files/raw/master/sample.ogg?raw=true")
    data object STICKER : LOREM("CAACAgIAAxkBAAEJ389kyGaEy1pMz1FN_h6F5Eh6jvneNQACxBcAAtXH-EsCpevGYhPo5S8E")
    data object ANIMATION :
        LOREM("https://github.com/malcomio/dummy-content/blob/master/images/animated-parabola.gif?raw=true")

    data object DOCUMENT : LOREM("https://github.com/malcomio/dummy-content/blob/master/Lorem_ipsum.pdf?raw=true")

    private companion object {
        val httpClient = HttpClient()
        val rand: String
            get() {
                return Random.nextBytes(10).toString()
            }
    }
}
