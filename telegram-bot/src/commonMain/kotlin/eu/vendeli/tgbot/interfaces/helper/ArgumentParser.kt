package eu.vendeli.tgbot.interfaces.helper

fun interface ArgumentParser {
    fun parse(text: String): Map<String, String>
}
