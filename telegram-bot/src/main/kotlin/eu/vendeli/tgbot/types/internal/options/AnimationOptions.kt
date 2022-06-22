package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import java.io.File

data class AnimationOptions(
    var duration: Int? = null,
    var width: Int? = null,
    var height: Int? = null,
    var thumb: File? = null,
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
) : OptionsInterface<AnimationOptions>, OptionsCommon, OptionsParseMode
