package eu.vendeli.tgbot.types.game

import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.types.media.Animation
import eu.vendeli.tgbot.types.media.PhotoSize
import kotlinx.serialization.Serializable

/**
 * This object represents a game. Use BotFather to create and edit games, their short names will act as unique identifiers.
 *
 * [Api reference](https://core.telegram.org/bots/api#game)
 * @property title Title of the game
 * @property description Description of the game
 * @property photo Photo that will be displayed in the game message in chats.
 * @property text Optional. Brief description of the game or high scores included in the game message. Can be automatically edited to include current high scores for the game when the bot calls setGameScore, or manually edited using editMessageText. 0-4096 characters.
 * @property textEntities Optional. Special entities that appear in text, such as usernames, URLs, bot commands, etc.
 * @property animation Optional. Animation that will be displayed in the game message in chats. Upload via BotFather
 */
@Serializable
data class Game(
    val title: String,
    val description: String,
    val photo: List<PhotoSize>,
    val text: String? = null,
    val textEntities: List<MessageEntity>? = null,
    val animation: Animation? = null,
)
