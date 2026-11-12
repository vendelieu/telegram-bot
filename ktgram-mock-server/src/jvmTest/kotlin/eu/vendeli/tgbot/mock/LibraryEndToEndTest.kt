package eu.vendeli.tgbot.mock

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.deleteMessage
import eu.vendeli.tgbot.api.message.editText
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.component.getOrNull
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

/** The library under test talks to the mock exactly like it talks to api.telegram.org. */
class LibraryEndToEndTest :
    FunSpec({
        test("a KtGram bot answers a user command through long polling") {
            MockTelegramServer(MockServerConfig(port = 0)).start().use { server ->
                runBlocking {
                    val handle = server.bot()
                    val bot = TelegramBot(MockServerConfig.DEFAULT_TOKEN) { apiHost = server.baseUrl }
                    try {
                        bot.setFunctionality {
                            onCommand("/start") {
                                message { "Welcome ${user.firstName}" }.send(user, bot)
                            }
                        }
                        val listener = launch { bot.update.setListener { handle(it) } }

                        handle.userSays("/start", fromId = USER, firstName = "Ann")
                        val sent = handle.awaitCall("sendMessage", 10.seconds)

                        sent.status shouldBe 200
                        handle.lastMessage(USER)!!.str("text") shouldBe "Welcome Ann"
                        bot.update.stopListener()
                        listener.cancel()
                    } finally {
                        bot.update.stopListener()
                    }
                }
            }
        }

        test("actions of the library validate against the mock: send, edit, delete") {
            MockTelegramServer(MockServerConfig(port = 0)).start().use { server ->
                runBlocking {
                    val bot = TelegramBot(MockServerConfig.DEFAULT_TOKEN) { apiHost = server.baseUrl }
                    val sent = message { "one" }.sendReturning(USER, bot).getOrNull()!!
                    sent.messageId shouldBe 1L
                    editText(sent.messageId) { "two" }.send(USER, bot)
                    server.bot().lastMessage(USER)!!.str("text") shouldBe "two"
                    deleteMessage(sent.messageId).send(USER, bot)
                    server.bot().messages(USER).size shouldBe 0
                }
            }
        }
    })
