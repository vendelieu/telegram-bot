package eu.vendeli.tgbot.types.ton

import eu.vendeli.tgbot.types.gift.UniqueGift
import kotlinx.serialization.Serializable

/**
 * This object represents an NFT in the TON blockchain.
 *
 * [Api reference](https://core.telegram.org/bots/api#tonnft)
 * @property address Address of the NFT
 * @property collectionAddress Optional. Address of the collection to which the NFT belongs; omitted if unknown
 * @property index Index of the NFT among all NFTs from the collection as a decimal number
 * @property name Name of the NFT
 * @property description Description of the NFT
 * @property attributes Optional. Attributes of the NFT
 * @property gift Optional. The unique gift corresponding to the NFT
 */
@Serializable
data class TonNft(
    val address: String,
    val index: String,
    val name: String,
    val description: String,
    val collectionAddress: String? = null,
    val attributes: List<TonNftAttribute>? = null,
    val gift: UniqueGift? = null,
)
