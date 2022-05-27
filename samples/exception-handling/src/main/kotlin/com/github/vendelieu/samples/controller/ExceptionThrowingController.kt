package com.github.vendelieu.samples.controller

import com.github.vendelieu.tgbot.annotations.TelegramCommand

class ExceptionThrowingController {

    @TelegramCommand(["/start"])
    fun start() {
        throw RuntimeException()
    }
}