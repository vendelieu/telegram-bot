package com.github.vendelieu.tgbot.types.internal.options

import com.github.vendelieu.tgbot.types.ParseMode

class EditMessageOptions(
    var disableWebPagePreview: Boolean? = null,
    override var parseMode: ParseMode? = null,
) : OptionsInterface<EditMessageOptions>, OptionsParseMode
