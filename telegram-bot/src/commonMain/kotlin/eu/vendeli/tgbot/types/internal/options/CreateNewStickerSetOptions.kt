package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.media.StickerType
import kotlinx.serialization.Serializable

@Serializable
data class CreateNewStickerSetOptions(
    var stickerType: StickerType? = null,
    var needsRepainting: Boolean? = null,
) : Options
