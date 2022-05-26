package com.github.vendelieu.tgbot.types.internal.options

import com.github.vendelieu.tgbot.types.ParseMode

data class EditCaptionOptions(
    override var parseMode: ParseMode? = null,
) : OptionsInterface<EditCaptionOptions>, OptionsParseMode
