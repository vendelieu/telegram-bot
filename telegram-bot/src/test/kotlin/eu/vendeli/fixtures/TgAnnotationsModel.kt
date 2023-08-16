@file:Suppress("EmptyFunctionBlock", "FunctionOnlyReturningConstant")

package eu.vendeli.fixtures

import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.types.internal.UpdateType

class TgAnnotationsModel {

    @CommandHandler(["test"])
    fun test() {
        throw IllegalArgumentException("test2")
    }

    @InputHandler(["testInp"])
    fun test2() {
    }

    @UpdateHandler([UpdateType.MESSAGE, UpdateType.CALLBACK_QUERY])
    fun updateHandler() {
    }

    @UnprocessedHandler
    fun test3() {
    }
}

@CommandHandler(["test2"])
suspend fun testMethod(): Boolean {
    return true
}

@InputHandler(["testInp2"])
suspend fun testMethod2() {
}

object TestObj {
    @CommandHandler(["test3"])
    fun test(): Boolean {
        return false
    }

    @InputHandler(["testInp3"])
    fun test2() {
    }
}
