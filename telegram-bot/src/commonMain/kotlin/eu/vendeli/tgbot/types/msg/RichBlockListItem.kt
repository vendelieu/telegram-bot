package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * An item of a list.
 *
 * [Api reference](https://core.telegram.org/bots/api#richblocklistitem)
 * @property label Label of the item
 * @property blocks The content of the item
 * @property hasCheckbox Optional. True, if the item has a checkbox
 * @property isChecked Optional. True, if the item has a checked checkbox
 * @property value Optional. For ordered lists, the numeric value of the item label
 * @property type Optional. For ordered lists, the type of the item label; must be one of "a" for lowercase letters, "A" for uppercase letters, "i" for lowercase Roman numerals, "I" for uppercase Roman numerals, or "1" for decimal numbers
 */
@Serializable
data class RichBlockListItem(
    val label: String,
    val blocks: List<RichBlock>,
    val hasCheckbox: Boolean? = null,
    val isChecked: Boolean? = null,
    val value: Int? = null,
    val type: String? = null,
)
