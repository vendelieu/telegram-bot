package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * Cell in a table.
 *
 * [Api reference](https://core.telegram.org/bots/api#richblocktablecell)
 * @property text Optional. Text in the cell. If omitted, then the cell is invisible.
 * @property isHeader Optional. True, if the cell is a header cell
 * @property colspan Optional. The number of columns the cell spans if it is bigger than 1
 * @property rowspan Optional. The number of rows the cell spans if it is bigger than 1
 * @property align Horizontal cell content alignment. Currently, must be one of "left", "center", or "right".
 * @property valign Vertical cell content alignment. Currently, must be one of "top", "middle", or "bottom".
 */
@Serializable
data class RichBlockTableCell(
    val align: TableCellAlign,
    val valign: TableCellVerticalAlign,
    val text: RichText? = null,
    val isHeader: Boolean? = null,
    val colspan: Int? = null,
    val rowspan: Int? = null,
)
