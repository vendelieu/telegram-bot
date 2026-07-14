package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * An item of a list to be sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputrichblocklistitem)
 * @property blocks The content of the item
 * @property hasCheckbox Optional. Pass True if the item has a checkbox
 * @property isChecked Optional. Pass True if the item has a checked checkbox
 * @property value Optional. For ordered lists, the numeric value of the item label
 * @property type Optional. For ordered lists, the type of the item label; must be one of "a" for lowercase letters, "A" for uppercase letters, "i" for lowercase Roman numerals, "I" for uppercase Roman numerals, or "1" for decimal numbers
 */
@Serializable
data class InputRichBlockListItem(
    val blocks: List<InputRichBlock>,
    val hasCheckbox: Boolean? = null,
    val isChecked: Boolean? = null,
    val value: Int? = null,
    val type: String? = null,
)
