package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.types.media.StickerType

data class CreateNewStickerSetData(
    var name: String,
    var title: String,
    var stickers: List<InputSticker>,
    var stickerType: StickerType? = null,
    var needsRepainting: Boolean? = null,
) {
    internal fun validateFields(): CreateNewStickerSetData {
        require(name.isNotEmpty() && title.isNotEmpty() && stickers.isNotEmpty()) {
            "Field name/title/stickers must not be null or empty."
        }
        return this
    }
}
