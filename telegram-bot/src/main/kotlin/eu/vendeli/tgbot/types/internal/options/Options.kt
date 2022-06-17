package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.utils.StringManipulations.camel2SnakeCase
import kotlin.reflect.full.memberProperties

interface Options {
    fun getParams(): Map<String, Any?>
}

interface OptionsInterface<O> : Options {
    @Suppress("LeakingThis", "UNCHECKED_CAST")
    private val thisAsO: O
        get() = this as O

    override fun getParams() =
        thisAsO!!::class.memberProperties.associate { camel2SnakeCase(it.name) to (it.getter.call(thisAsO)) }
}

interface OptionsParseMode : Options {
    var parseMode: ParseMode?
}

interface IFileOptions : Options {
    var fileName: String?
}

interface IOptionsCommon : Options {
    var disableNotification: Boolean?
    var replyToMessageId: Long?
    var allowSendingWithoutReply: Boolean?
    var protectContent: Boolean?
}
