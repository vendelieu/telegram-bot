package eu.vendeli.tgbot.utils.common

open class TgException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : RuntimeException()

class TgFailureException(
    val response: String,
) : TgException(message = response)
