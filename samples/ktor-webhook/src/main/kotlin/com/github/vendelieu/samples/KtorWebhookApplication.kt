package eu.vendeli.samples

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.botactions.setWebhook
import eu.vendeli.tgbot.types.Update
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", "eu.vendeli.samples.controller")

    setWebhook("https://0.0.0.0/BOT_TOKEN").send(bot)

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        routing {
            post("/BOT_TOKEN") {
                bot.update.apply {
                    parseUpdate(call.receive())?.handle()
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }.start(wait = true)
}
