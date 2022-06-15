package com.github.vendelieu.samples.controller

import com.github.vendelieu.tgbot.TelegramBot
import com.github.vendelieu.tgbot.annotations.TelegramCommand
import com.github.vendelieu.tgbot.annotations.TelegramUnhandled
import com.github.vendelieu.tgbot.api.message
import com.github.vendelieu.tgbot.api.poll
import com.github.vendelieu.tgbot.types.PollType
import com.github.vendelieu.tgbot.types.User
import com.github.vendelieu.tgbot.types.internal.ProcessedUpdate
import com.github.vendelieu.tgbot.utils.inlineKeyboardMarkup

class PollController {
    @TelegramCommand(["/start"])
    suspend fun start(user: User, bot: TelegramBot) {
        message { "Hello!" }.send(user, bot)

        poll("Zebra white with black stripes or black with white stripes") {
            arrayOf(
                "white with black stripes",
                "black with with white stripes"
            )
        }.options {
            type = PollType.Quiz
            correctOptionId = 1
        }.send(user, bot)

        message { "Want more polls?" }.markup {
            inlineKeyboardMarkup {
                "yes" callback "morePolls"
            }
        }.send(user.id, bot)
    }

    @TelegramCommand(["morePolls"])
    suspend fun anotherPoll(user: User, bot: TelegramBot) {
        poll("What color is the dress?", "Blue-black", "White-gold").options {
            allowsMultipleAnswers = true
            isClosed = true
            explanation = "Who gave this man a time machine?"
        }.send(user, bot)
    }
}