package eu.vendeli.samples.controller

import eu.vendeli.tgbot.annotations.TelegramCommand

class ExceptionThrowingController {

    @TelegramCommand(["/start"])
    fun start() {
        throw RuntimeException()
    }
}