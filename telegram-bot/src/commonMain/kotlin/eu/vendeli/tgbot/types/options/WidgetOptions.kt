package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.common.ReplyParameters
import eu.vendeli.tgbot.types.component.ParseMode
import kotlinx.serialization.Serializable

@Serializable
data class WidgetOptions(
    var width: Int? = null,
    var height: Int? = null,
    var publicData: String? = null,
    var chatData: String? = null,
    var allowQueries: Boolean? = null,
    var isShared: Boolean? = null,
    override var showCaptionAboveMedia: Boolean? = null,
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Int? = null,
    override var messageEffectId: String? = null,
    override var allowPaidBroadcast: Boolean? = null,
) : OptionsCommon,
    ForumProps,
    OptionsParseMode,
    MessageEffectIdProp,
    ShowCaptionAboveMediaProp,
    AllowPaidBroadcastProp
