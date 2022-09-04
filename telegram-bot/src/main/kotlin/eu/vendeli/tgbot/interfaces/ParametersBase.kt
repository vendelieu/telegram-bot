package eu.vendeli.tgbot.interfaces

/**
 * Parent Interface of Features and Actions
 */
interface ParametersBase {
    /**
     * Parameter for storing API data.
     */
    val parameters: MutableMap<String, Any?>
}