package eu.vendeli.tgbot.interfaces.helper

/**
 * Custom argument parsing mechanism defining interface,
 * you can use [eu.vendeli.utils.defaultArgParser] with specific configuration.
 */
fun interface ArgumentParser {
    fun parse(text: String): Map<String, String>
}
