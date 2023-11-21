@file:Suppress("ktlint:standard:class-naming")

package eu.vendeli.utils

sealed class LOREM(val data: String) {
    data object AUDIO : LOREM("https://github.com/malcomio/dummy-content/raw/master/audio/audio.mp3")
    data object VIDEO : LOREM("https://github.com/malcomio/dummy-content/raw/master/video/small.mp4")
    data object VIDEO_NOTE : LOREM("https://rb.gy/y0egi")
    data object VOICE : LOREM("https://github.com/rafaelreis-hotmart/Audio-Sample-files/raw/master/sample.ogg?raw=true")
    data object STICKER : LOREM("CAACAgIAAxkBAAEJ389kyGaEy1pMz1FN_h6F5Eh6jvneNQACxBcAAtXH-EsCpevGYhPo5S8E")
    data object ANIMATION :
        LOREM("https://github.com/malcomio/dummy-content/blob/master/images/animated-parabola.gif?raw=true")
    data object DOCUMENT : LOREM("https://github.com/malcomio/dummy-content/blob/master/Lorem_ipsum.pdf?raw=true")

    companion object {
        fun values(): Array<LOREM> {
            return arrayOf(AUDIO, VIDEO, ANIMATION)
        }

        fun valueOf(value: String): LOREM {
            return when (value) {
                "AUDIO" -> AUDIO
                "VIDEO" -> VIDEO
                "ANIMATION" -> ANIMATION
                else -> throw IllegalArgumentException("No object eu.vendeli.fixtures.LoremData.$value")
            }
        }
    }
}
