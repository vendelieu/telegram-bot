package eu.vendeli.samples.controller

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.TelegramCommand
import eu.vendeli.tgbot.annotations.TelegramInput
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.UpdateType

class ConversationTelegramController {
    @CommandHandler(["/start"])
    suspend fun start(user: User, bot: TelegramBot) {
        message { "Hello, my name is Adam, and you?" }.send(user, bot)

        bot.inputListener.set(user.id, "name")
    }

    @InputHandler(["name"])
    suspend fun name(update: ProcessedUpdate, bot: TelegramBot) {
        if (update.type != UpdateType.MESSAGE) {
            message { "Please say your name, because that's what well-mannered people do :)" }.send(update.user, bot)
            bot.inputListener.set(update.user.id, "name")
        }

        bot.userData?.set(update.user.id, "name", update.text)

        message { "Oh, ${update.text}, nice to meet you!" }
        message { "How old are you?" }.send(update.user, bot)

        bot.inputListener.set(update.user.id, "age")
    }

    @InputHandler(["age"])
    suspend fun age(update: ProcessedUpdate, bot: TelegramBot) {
        if (update.type != UpdateType.MESSAGE || update.text?.toIntOrNull() == null) {
            message { "Perhaps it's not nice to ask your age, but maybe you can tell me anyway." }
                .send(update.user, bot)
            bot.inputListener.set(update.user.id, "age")
        }

        val name = bot.userData?.get(update.user.id, "name").toString()
        message {
            "I'm not good at remembering, but I remembered you! You're $name and you're ${update.text} years old."
        }.send(update.user, bot)
    }
}