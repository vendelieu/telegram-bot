@file:Suppress("EmptyFunctionBlock")

package other.pckg

import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.types.internal.CommandScope
import eu.vendeli.tgbot.types.internal.UpdateType

class TgAnnotationsModel {

    @CommandHandler(["test"], scope = [CommandScope.CALLBACK])
    fun test() {
    }

    @InputHandler(["testInp"])
    fun test2() {
    }

    @UnprocessedHandler
    fun test3() {
    }

    @UpdateHandler([UpdateType.MESSAGE, UpdateType.CALLBACK_QUERY])
    fun updateHandler() {
    }
}

@CommandHandler(["test2"])
suspend fun testMethod() {
}

@InputHandler(["testInp2"])
suspend fun testMethod2() {
}

object TestObj {
    @CommandHandler(["test3"])
    fun test() {
    }

    @InputHandler(["testInp3"])
    fun test2() {
    }
}
