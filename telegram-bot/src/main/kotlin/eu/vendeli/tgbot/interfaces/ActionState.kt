package eu.vendeli.tgbot.interfaces

/**
 * Parent Interface of Features and Actions
 */
interface IActionState {
    /**
     * Parameter for storing API data.
     */
    val IActionState.parameters: MutableMap<String, Any?>
}

/**
 * Abstract class inherited for storing action data.
 */
abstract class ActionState : IActionState {
    /**
     * Parameter for storing API data.
     */
    override val IActionState.parameters by lazy { mutableMapOf<String, Any?>() }
}
