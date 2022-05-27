package com.github.vendelieu.samples.controller

import com.github.vendelieu.tgbot.TelegramBot
import com.github.vendelieu.tgbot.annotations.TelegramCommand
import com.github.vendelieu.tgbot.annotations.TelegramInput
import com.github.vendelieu.tgbot.api.message
import com.github.vendelieu.tgbot.types.User
import com.github.vendelieu.tgbot.types.internal.ProcessedUpdate
import com.github.vendelieu.tgbot.types.internal.UpdateType

class ConversationTelegramController {
    @TelegramCommand(["/start"])
    suspend fun start(user: User, bot: TelegramBot) {
        message { "Hello, my name is Adam, and you?" }.send(user, bot)

        bot.inputHandler.set(user.id, "name")
    }

    @TelegramInput(["name"])
    suspend fun name(update: ProcessedUpdate, bot: TelegramBot) {
        if (update.type != UpdateType.MESSAGE) {
            message { "Please say your name, because that's what well-mannered people do :)" }.send(update.user, bot)
            bot.inputHandler.set(update.user.id, "name")
        }

        bot.userData?.set(update.user.id, "name", update.text)

        message { "Oh, ${update.text}, nice to meet you!" }
        message { "How old are you?" }.send(update.user, bot)

        bot.inputHandler.set(update.user.id, "age")
    }

    @TelegramInput(["age"])
    suspend fun age(update: ProcessedUpdate, bot: TelegramBot) {
        if (update.type != UpdateType.MESSAGE || update.text?.toIntOrNull() == null) {
            message { "Perhaps it's not nice to ask your age, but maybe you can tell me anyway." }
                .send(update.user, bot)
            bot.inputHandler.set(update.user.id, "age")
        }

        val name = bot.userData?.get(update.user.id, "name").toString()
        message {
            "I'm not good at remembering, but I remembered you! You're $name and you're ${update.text} years old."
        }.send(update.user, bot)
    }
}