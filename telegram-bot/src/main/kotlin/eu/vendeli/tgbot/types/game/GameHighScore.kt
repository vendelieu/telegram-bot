package eu.vendeli.tgbot.types.game

import eu.vendeli.tgbot.types.User

data class GameHighScore(
    val position: Int,
    val user: User,
    val score: Int,
)
