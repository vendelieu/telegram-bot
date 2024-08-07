package eu.vendeli.tgbot.utils

class TgFailureException(val response: String) : RuntimeException(message = response)
