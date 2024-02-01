package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.ReplyParameters
import eu.vendeli.tgbot.types.internal.ImplicitFile
import kotlinx.serialization.Serializable

@Serializable
data class VideoOptions(
    var duration: Int? = null,
    var height: Int? = null,
    var width: Int? = null,
    var supportsStreaming: Boolean? = null,
    var thumbnail: ImplicitFile? = null,
    override var fileName: String? = null,
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Int? = null,
    override var hasSpoiler: Boolean? = null,
) : OptionsCommon, OptionsParseMode, FileOptions, MediaSpoiler
