package eu.vendeli.tgbot.interfaces

/**
 * Parent Interface of Features and Actions
 */
interface ActionState {
    /**
     * Parameter for storing API data.
     */
    val ActionState.parameters: MutableMap<String, Any?>
}
