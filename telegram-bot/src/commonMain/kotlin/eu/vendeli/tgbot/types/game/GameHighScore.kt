package eu.vendeli.tgbot.types.game

import eu.vendeli.tgbot.interfaces.marker.MultipleResponse
import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * This object represents one row of the high scores table for a game.
 *
 * [Api reference](https://core.telegram.org/bots/api#gamehighscore)
 * @property position Position in high score table for the game
 * @property user User
 * @property score Score
 */
@Serializable
data class GameHighScore(
    val position: Int,
    val user: User,
    val score: Long,
) : MultipleResponse
