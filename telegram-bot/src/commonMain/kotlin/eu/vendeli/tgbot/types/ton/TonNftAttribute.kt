package eu.vendeli.tgbot.types.ton

import kotlinx.serialization.Serializable

/**
 * This object represents an attribute of an NFT in the TON blockchain.
 *
 * [Api reference](https://core.telegram.org/bots/api#tonnftattribute)
 * @property traitType Trait type of the attribute
 * @property value Value of the attribute
 */
@Serializable
data class TonNftAttribute(
    val traitType: String,
    val value: String,
)
