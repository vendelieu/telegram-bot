package com.github.vendelieu.tgbot.types.internal.options

data class GetUpdatesOptions(
    var offset: Int? = null,
    var limit: Int? = null,
    var timeout: Int? = null,
    var allowedUpdates: List<String>? = null,
) : OptionsInterface<GetUpdatesOptions>
