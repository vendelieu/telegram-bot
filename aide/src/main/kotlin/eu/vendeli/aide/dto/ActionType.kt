package eu.vendeli.aide.dto

internal enum class ActionType {
    /**
     * Action is a simple action (subclass of [SimpleAction]).
     */
    SIMPLE_ACTION,

    /**
     * Action is a regular action (subclass of [Action]).
     */
    ACTION,
}
