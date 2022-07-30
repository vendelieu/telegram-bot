package eu.vendeli.samples.controller

import eu.vendeli.tgbot.annotations.TelegramCommand

class ExceptionThrowingController {
    @CommandHandler(["/start"])
    fun start() {
        throw RuntimeException()
    }
}