package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.configuration.RateLimits

fun interface Action {
    suspend fun invoke(user: User, update: ProcessedUpdate, bot: TelegramBot)
}

fun interface BreakCondition {
    suspend fun invoke(user: User, update: ProcessedUpdate, bot: TelegramBot): Boolean
}

abstract class ChainLink(
    private val action: Action,
    internal val nextLink: ChainLink? = null,
    protected open val breakCondition: BreakCondition? = null,
    protected open val breakingAction: Action? = null,
) {
    open val rateLimits: RateLimits = RateLimits.NOT_LIMITED
    open val retryAfterBreak: Boolean = true

    @Suppress("unused")
    protected val inputPoint: String by lazy { this::class.objectInstance!!::class.java.canonicalName }

    internal suspend fun invoke(user: User, update: ProcessedUpdate, bot: TelegramBot) {
        val breakPoint = breakCondition?.invoke(user, update, bot) ?: false
        if (breakPoint && retryAfterBreak) {
            bot.inputListener[user] = this::class.objectInstance!!::class.java.canonicalName
        }
        if (breakPoint) {
            breakingAction?.invoke(user, update, bot)
            return
        }

        action.invoke(user, update, bot)

        if (nextLink != null && nextLink::class.objectInstance != null) {
            bot.inputListener[user] = nextLink::class.objectInstance!!::class.java.canonicalName
        }
    }
}

abstract class ChLink(val action: Action) {
    open val retryAfterBreak = false
    open val breakCondition: BreakCondition? = null

    open fun breakAction(user: User, update: ProcessedUpdate, bot: TelegramBot) {}
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
