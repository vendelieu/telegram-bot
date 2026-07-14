package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TableCellAlign {
    @SerialName("left")
    Left,

    @SerialName("center")
    Center,

    @SerialName("right")
    Right,
}
