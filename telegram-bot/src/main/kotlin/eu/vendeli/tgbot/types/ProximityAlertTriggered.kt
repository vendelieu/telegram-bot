package eu.vendeli.tgbot.types

data class ProximityAlertTriggered(
    val traveler: User,
    val watcher: User,
    val distance: Int
)
