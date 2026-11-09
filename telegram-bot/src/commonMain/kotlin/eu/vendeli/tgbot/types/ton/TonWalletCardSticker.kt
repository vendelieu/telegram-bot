package eu.vendeli.tgbot.types.ton

import eu.vendeli.tgbot.types.common.AreaPosition
import kotlinx.serialization.Serializable

/**
 * This object describes a sticker on a TON wallet card.
 *
 * [Api reference](https://core.telegram.org/bots/api#tonwalletcardsticker)
 * @property position Position of the sticker on the card
 * @property nft The NFT shown as the sticker
 */
@Serializable
data class TonWalletCardSticker(
    val position: AreaPosition,
    val nft: TonNft,
)
