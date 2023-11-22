package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.BreakCondition
import eu.vendeli.tgbot.utils.DEFAULT_COMMAND_SCOPE

fun interface Action {
    suspend fun invoke(user: User, update: ProcessedUpdate, bot: TelegramBot)
}

abstract class ChainLink(private val action: Action, internal val nextLink: ChainLink? = null) {
    open val scope: Set<CommandScope> = DEFAULT_COMMAND_SCOPE
    open val rateLimits: RateLimits = RateLimits.NOT_LIMITED
    open val retryAfterBreak: Boolean = false

    @Suppress("unused")
    protected val inputPoint: String by lazy { this::class.objectInstance!!::class.java.canonicalName }

    open fun breakCondition(user: User, update: ProcessedUpdate, bot: TelegramBot): BreakCondition? = null
    internal suspend fun invoke(user: User, update: ProcessedUpdate, bot: TelegramBot) {
        val breakPoint = breakCondition(user, update, bot)
        if (breakPoint?.first == true && retryAfterBreak) {
            bot.inputListener[user] = this::class.objectInstance!!::class.java.canonicalName
        }
        if (breakPoint?.first == true) {
            breakPoint.second.invoke()
            return
        }

        action.invoke(user, update, bot)

        if (nextLink != null && nextLink::class.objectInstance != null) {
            bot.inputListener[user] = nextLink::class.objectInstance!!::class.java.canonicalName
        }
    }
}

@ExperimentalFeature
abstract class InputChain(internal val firstLink: ChainLink) {
    internal val wholeChain: MutableList<ChainLink> = mutableListOf()

    init {
        var link: ChainLink? = firstLink
        while (link != null) {
            wholeChain.add(link)
            link = link.nextLink
        }
    }
}
