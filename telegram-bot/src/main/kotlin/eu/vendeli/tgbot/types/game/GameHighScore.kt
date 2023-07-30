package eu.vendeli.tgbot.types.game

import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.User

data class GameHighScore(
    val position: Int,
    val user: User,
    val score: Long,
) : MultipleResponse
