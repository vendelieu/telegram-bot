@file:Suppress("EmptyFunctionBlock")

package eu.vendeli

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

    @UnprocessedHandler
    fun test3() {
    }
}

@CommandHandler(["test2"])
suspend fun testMethod() {
}

@InputHandler(["testInp2"])
suspend fun testMethod2() {
}

@UpdateHandler([UpdateType.MESSAGE, UpdateType.CALLBACK_QUERY])
fun updateHandler() {
}

object TestObj {
    @CommandHandler(["test3"])
    fun test() {
    }

    @InputHandler(["testInp3"])
    fun test2() {
    }
}
