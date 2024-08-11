@file:Suppress("EmptyFunctionBlock", "FunctionOnlyReturningConstant")

package eu.vendeli.fixtures

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.CommonHandler
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.types.internal.UpdateType

class TgAnnotationsModel {
    @CommandHandler(["test"])
    fun test(bot: TelegramBot) {
        bot.classData[1, "test"] = "value"
        throw IllegalArgumentException("test2")
    }

    @InputHandler(["testInp"])
    fun test2(): Unit = throw IllegalArgumentException("test3")

    @CommandHandler(["STOP"])
    suspend fun stopHandling(bot: TelegramBot) {
        bot.update.stopListener().await()
    }

    @UpdateHandler([UpdateType.MESSAGE, UpdateType.CALLBACK_QUERY])
    fun updateHandler() {
    }

    @CommonHandler.Text(["common", "common2"])
    suspend fun common() {
    }

    @UnprocessedHandler
    fun test3() {
    }
}

@CommandHandler(["test2"])
suspend fun testMethod(): Boolean = true

@InputHandler(["testInp2"])
suspend fun testMethod2() {
}

object TestObj {
    @CommandHandler(["test3"])
    fun test(): Boolean = false

    @InputHandler(["testInp3"])
    fun test2() {
    }
}
