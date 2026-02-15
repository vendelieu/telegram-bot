// @generated ActivitiesData.kt: Activity
// @generated KtGramCtxLoader.kt: registerCommand, registerInput, registerCommonHandler, registerUpdate, registerUnprocessed
// @generated BotCtx.kt: userData, classData

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
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.interfaces.helper.Guard as GuardInterface
import eu.vendeli.tgbot.types.component.UpdateType
import kotlin.text.RegexOption

object CustomTestGuard : GuardInterface {
    override suspend fun condition(user: User?, update: ProcessedUpdate, bot: TelegramBot): Boolean = true
}

object CustomTestArgParser : ArgumentParser {
    override fun parse(text: String): Map<String, String> = emptyMap()
}

object CustomTestFilter : Filter {
    override suspend fun match(user: User?, update: ProcessedUpdate, bot: TelegramBot): Boolean = true
}

@CommandHandler(["/test"], scope = [UpdateType.MESSAGE])
suspend fun testGlobal(user: User, bot: TelegramBot) {
}

class Test {
    @CommandHandler(["/test2"], scope = [UpdateType.MESSAGE])
    suspend fun inClass(user: User, bot: TelegramBot) {}

    @CommandHandler(["/test3"], scope = [UpdateType.MESSAGE])
    suspend fun inClass2(bot: TelegramBot) {}

    @CommandHandler(["/test4"], scope = [UpdateType.MESSAGE])
    suspend fun inClass3() {}

    @CommandHandler(["/test5"], scope = [UpdateType.MESSAGE])
    suspend fun inClass4(user: User?, bot: TelegramBot) {}

    @CommandHandler(["/test6"], scope = [UpdateType.MESSAGE])
    suspend fun inClass5(user: User, bot: TelegramBot?) {}

    @CommandHandler(["/test7"], scope = [UpdateType.MESSAGE])
    suspend fun inClass6(
        @ParamMapping("test") testParam: String?,
        user: User?,
        bot: TelegramBot?,
    ) {}

    @CommandHandler(["/"], scope = [UpdateType.MESSAGE])
    fun cls() {}
}

object Test2 {
    @CommandHandler(["/obj"], scope = [UpdateType.MESSAGE])
    suspend fun obj(user: User, bot: TelegramBot) {}

    @CommandHandler(["/obj2"], scope = [UpdateType.MESSAGE])
    @Guard(CustomTestGuard::class)
    suspend fun obj2(test: Int, user: User, bot: TelegramBot) {}

    @InputHandler(["/obj3"])
    @Guard(CustomTestGuard::class)
    @ArgParser(CustomTestArgParser::class)
    @RateLimits(period = 1L, rate = 200L)
    suspend fun obj3(user: User, update: ProcessedUpdate, bot: TelegramBot) {}

    @UpdateHandler([UpdateType.MESSAGE, UpdateType.CALLBACK_QUERY])
    fun updateHandler() {
    }

    @CommandHandler.CallbackQuery(["/obj4"], autoAnswer = true)
    @Guard(CustomTestGuard::class)
    @ArgParser(CustomTestArgParser::class)
    suspend fun obj4(test: Int, user: User, bot: TelegramBot) {}

    @CommonHandler.Text(
        value = ["/obj5"],
        filters = [CustomTestFilter::class],
        priority = 1,
        scope = [UpdateType.MESSAGE],
    )
    suspend fun obj5(test: Int, user: User, bot: TelegramBot) {}

    @CommonHandler.Regex(
        value = "(.*)",
        options = [RegexOption.IGNORE_CASE],
        filters = [CustomTestFilter::class],
        priority = 1,
        scope = [UpdateType.MESSAGE],
    )
    suspend fun obj6(test: Int, user: User, bot: TelegramBot) {}
}

@UnprocessedHandler
suspend fun testUnprocessed(update: ProcessedUpdate, bot: TelegramBot) {
}
