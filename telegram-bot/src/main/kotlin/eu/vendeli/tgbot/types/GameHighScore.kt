package eu.vendeli.tgbot.types

data class GameHighScore(
    val position: Int,
    val user: User,
    val score: Int
)
