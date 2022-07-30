package eu.vendeli.samples

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message

suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN")

    bot.update.setListener {
        message(it.message?.text ?: "").send(it.message?.from?.id ?: 0, bot)
//        handle(it) { // same in manual handling mode
//            onMessage {
//                message { data.message?.text ?: "" }.send(data.message!!.from!!, bot)
//            }
//        }
    }
}
