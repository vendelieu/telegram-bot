package other.pckg2

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.types.User

@FunctionalInterface
class AnnotatedController(
    private val test: List<String>
) {

    fun testString(): String {
        return "someString"
    }

    @CommandHandler(["testCommandActionThatShouldBeFound"])
    suspend fun testCommandActionThatShouldBeFound(user: User, bot: TelegramBot) {
    }

    @InputHandler(["testInputActionThatShouldBeFound"])
    suspend fun testInputActionThatShouldBeFound(user: User, bot: TelegramBot) {
    }
}
