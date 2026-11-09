package eu.vendeli.tgbot.types.ton

import kotlinx.serialization.Serializable

/**
 * This object describes a TON wallet card, which is shown on the profile page of a user to everyone.
 *
 * [Api reference](https://core.telegram.org/bots/api#tonwalletcard)
 * @property address Address of the wallet
 * @property amount Balance of the wallet, in the smallest units of the cryptocurrency, as a decimal number
 * @property stickers Stickers placed on the card
 * @property backgroundNftAddress Optional. Address of the NFT of one of the stickers, whose colors must be used for the card background; omitted if the default background must be used
 */
@Serializable
data class TonWalletCard(
    val address: String,
    val amount: String,
    val stickers: List<TonWalletCardSticker>,
    val backgroundNftAddress: String? = null,
)
