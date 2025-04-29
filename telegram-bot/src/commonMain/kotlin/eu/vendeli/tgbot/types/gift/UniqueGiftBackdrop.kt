package eu.vendeli.tgbot.types.gift

import kotlinx.serialization.Serializable

/**
 * This object describes the backdrop of a unique gift.
 *
 * [Api reference](https://core.telegram.org/bots/api#uniquegiftbackdrop)
 * @property name Name of the backdrop
 * @property colors Colors of the backdrop
 * @property rarityPerMille The number of unique gifts that receive this backdrop for every 1000 gifts upgraded
 */
@Serializable
data class UniqueGiftBackdrop(
    val name: String,
    val colors: UniqueGiftBackdropColors,
    val rarityPerMille: Int,
)
