package eu.vendeli.tgbot.types.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GiftOrigin {
    @SerialName("upgrade")
    Upgrade,

    @SerialName("transfer")
    Transfer,

    @SerialName("resale")
    Resale,
}
