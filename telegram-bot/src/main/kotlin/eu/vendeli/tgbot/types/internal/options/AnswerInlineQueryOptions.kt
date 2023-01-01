package eu.vendeli.tgbot.types.internal.options

data class AnswerInlineQueryOptions(
    var cacheTime: Int? = null,
    var isPersonal: Boolean? = null,
    var nextOffset: String? = null,
    var switchPmText: String? = null,
    var switchPmParameter: String? = null,
) : OptionsInterface<AnswerInlineQueryOptions>
