package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.MaskPosition
import eu.vendeli.tgbot.types.StickerType
import eu.vendeli.tgbot.types.internal.StickerFile

class CreateNewStickerSetData {
    lateinit var name: String
    lateinit var title: String
    lateinit var emojis: String
    lateinit var sticker: StickerFile
    var maskPosition: MaskPosition? = null
    var stickerType: StickerType? = null
    var needsRepainting: Boolean? = null

    internal fun checkIsAllFieldsPresent() {
        require(
            ::name.isInitialized && ::title.isInitialized && ::emojis.isInitialized && ::sticker.isInitialized,
        ) { "All fields must be initialized" }
    }
}
