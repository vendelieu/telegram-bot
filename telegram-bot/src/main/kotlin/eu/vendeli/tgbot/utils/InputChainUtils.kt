package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.types.internal.ActionType
import eu.vendeli.tgbot.types.internal.InputChain
import eu.vendeli.tgbot.types.internal.Invocation
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaMethod

@ExperimentalFeature
fun <T : InputChain> TelegramBot.registerChain(chain: KClass<T>) {
    val input: MutableMap<String, Invocation> = (update.actions?.inputs as MutableMap?) ?: return
    chain.sealedSubclasses.filter { it.isSubclassOf(InputChain::class) && it.objectInstance != null }.forEach {
        val currChain = (it.objectInstance as InputChain)
        input[currChain::class.java.canonicalName] =
            Invocation(
                it.java,
                currChain::link.javaMethod ?: return,
                scope = currChain.scope,
                rateLimits = currChain.rateLimits,
                type = ActionType.INPUT,
            )
    }
}
