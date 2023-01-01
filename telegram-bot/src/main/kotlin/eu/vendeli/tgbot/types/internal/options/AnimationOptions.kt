package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.ImplicitFile

data class AnimationOptions(
    var duration: Int? = null,
    var width: Int? = null,
    var height: Int? = null,
    var thumb: ImplicitFile<*>? = null,
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Long? = null,
    override var hasSpoiler: Boolean? = null,
) : OptionsInterface<AnimationOptions>, OptionsCommon, OptionsParseMode, MediaSpoiler
