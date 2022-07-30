package eu.vendeli.samples.controller

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.TelegramCommand
import eu.vendeli.tgbot.annotations.TelegramUnhandled
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.api.poll
import eu.vendeli.tgbot.types.PollType
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.utils.inlineKeyboardMarkup

class PollController {
    @CommandHandler(["/start"])
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

    @CommandHandler(["morePolls"])
    suspend fun anotherPoll(user: User, bot: TelegramBot) {
        poll("What color is the dress?", "Blue-black", "White-gold").options {
            allowsMultipleAnswers = true
            isClosed = true
            explanation = "Who gave this man a time machine?"
        }.send(user, bot)
    }
}