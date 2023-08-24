package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.media.StickerType

data class CreateNewStickerSetOptions(
    var stickerType: StickerType? = null,
    var needsRepainting: Boolean? = null,
) : Options
