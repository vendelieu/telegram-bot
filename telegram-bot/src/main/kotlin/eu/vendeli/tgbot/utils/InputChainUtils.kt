package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ActionType
import eu.vendeli.tgbot.types.internal.ChainLink
import eu.vendeli.tgbot.types.internal.InputChain
import eu.vendeli.tgbot.types.internal.Invocation
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaMethod

@ExperimentalFeature
fun <T : InputChain> TelegramBot.registerChain(chain: KClass<T>) {
    val input = (update.actions?.inputs as MutableMap?) ?: return
    logger.trace { "Registering $chain as input chain." }
    chain.takeIf {
        it.isSubclassOf(InputChain::class) && it.objectInstance != null
    }?.objectInstance?.wholeChain?.forEach {
        val currChain = (it::class.objectInstance as ChainLink)
        input[currChain::class.java.canonicalName] = Invocation(
            it::class.java,
            currChain::invoke.javaMethod ?: return,
            scope = currChain.scope,
            rateLimits = currChain.rateLimits,
            type = ActionType.INPUT,
        )
        logger.debug { "Registered $currChain as input chain with key ${currChain::class.java.canonicalName}" }
    }
}

@ExperimentalFeature
fun <T : InputChain> InputListener.setChain(user: User, chain: T) {
    val firstLinkInst = chain.firstLink::class.objectInstance ?: return
    set(user, firstLinkInst::class.java.canonicalName)
}
