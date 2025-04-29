package eu.vendeli.tgbot.types.gift

import eu.vendeli.tgbot.types.media.Sticker
import kotlinx.serialization.Serializable

/**
 * This object describes the model of a unique gift.
 *
 * [Api reference](https://core.telegram.org/bots/api#uniquegiftmodel)
 * @property name Name of the model
 * @property sticker The sticker that represents the unique gift
 * @property rarityPerMille The number of unique gifts that receive this model for every 1000 gifts upgraded
 */
@Serializable
data class UniqueGiftModel(
    val name: String,
    val sticker: Sticker,
    val rarityPerMille: Int,
)
