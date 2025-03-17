import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.ArgParser
import eu.vendeli.tgbot.annotations.CommonHandler
import eu.vendeli.tgbot.annotations.Guard
import eu.vendeli.tgbot.annotations.RateLimits
import eu.vendeli.tgbot.annotations.ParamMapping
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.types.component.UpdateType

@CommandHandler(["/test"])
suspend fun testGlobal(user: User, bot: TelegramBot) {
}

class Test {
    @CommandHandler(["/test2"])
    suspend fun inClass(user: User, bot: TelegramBot) {}

    @CommandHandler(["/test3"])
    suspend fun inClass2(bot: TelegramBot) {}

    @CommandHandler(["/test4"])
    suspend fun inClass3() {}

    @CommandHandler(["/test5"])
    suspend fun inClass4(user: User?, bot: TelegramBot) {}

    @CommandHandler(["/test6"])
    suspend fun inClass5(user: User, bot: TelegramBot?) {}

    @CommandHandler(["/test7"])
    suspend fun inClass6(
        @ParamMapping("test") testParam: String?,
        user: User?,
        bot: TelegramBot?,
    ) {}

    @CommandHandler(["/"])
    fun cls() {}
}

object Test2 {
    @CommandHandler(["/obj"])
    suspend fun obj(user: User, bot: TelegramBot) {}

    @CommandHandler(["/obj2"])
    @Guard(DefaultGuard::class)
    suspend fun obj2(test: Int, user: User, bot: TelegramBot) {}

    @InputHandler(["/obj3"])
    @Guard(DefaultGuard::class)
    @RateLimits(period = 1L, rate = 200L)
    suspend fun obj3(user: User, update: ProcessedUpdate, bot: TelegramBot) {}

    @UpdateHandler([UpdateType.MESSAGE, UpdateType.CALLBACK_QUERY])
    fun updateHandler() {
    }

    @CommandHandler.CallbackQuery(["/obj4"])
    @Guard(DefaultGuard::class)
    @ArgParser(DefaultArgParser::class)
    suspend fun obj4(test: Int, user: User, bot: TelegramBot) {}

    @CommonHandler.Text(["/obj5"])
    suspend fun obj5(test: Int, user: User, bot: TelegramBot) {}

    @CommonHandler.Regex("(.*)")
    suspend fun obj6(test: Int, user: User, bot: TelegramBot) {}
}

@UnprocessedHandler
suspend fun testGlobal(update: ProcessedUpdate, bot: TelegramBot) {
}
