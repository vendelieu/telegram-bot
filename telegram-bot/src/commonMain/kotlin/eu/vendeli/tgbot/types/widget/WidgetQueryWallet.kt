package eu.vendeli.tgbot.types.widget

import kotlinx.serialization.Serializable

/**
 * This object describes a TON wallet of the user, which is used in a query from a widget.
 *
 * [Api reference](https://core.telegram.org/bots/api#widgetquerywallet)
 * @property address Address of the wallet
 * @property amount Balance of the wallet, in the smallest units of the cryptocurrency, as a decimal number
 * @property publicKey Optional. Hex-encoded public key of the wallet; omitted if unknown
 * @property stateInit Optional. Base64-encoded serialized StateInit of the wallet contract; omitted if and only if public_key is omitted
 */
@Serializable
data class WidgetQueryWallet(
    val address: String,
    val amount: String,
    val publicKey: String? = null,
    val stateInit: String? = null,
)
