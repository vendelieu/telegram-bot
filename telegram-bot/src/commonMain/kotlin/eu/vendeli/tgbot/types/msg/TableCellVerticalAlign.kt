package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TableCellVerticalAlign {
    @SerialName("top")
    Top,

    @SerialName("middle")
    Middle,

    @SerialName("bottom")
    Bottom,
}
