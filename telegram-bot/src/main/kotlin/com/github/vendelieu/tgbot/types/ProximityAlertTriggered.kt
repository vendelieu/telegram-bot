package com.github.vendelieu.tgbot.types

data class ProximityAlertTriggered(
    val traveler: User,
    val watcher: User,
    val distance: Int
)
