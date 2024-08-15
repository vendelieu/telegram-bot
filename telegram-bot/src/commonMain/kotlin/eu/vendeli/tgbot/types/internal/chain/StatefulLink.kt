package eu.vendeli.tgbot.types.internal.chain

import eu.vendeli.tgbot.types.internal.Action
import eu.vendeli.tgbot.types.internal.BreakCondition

abstract class StatefulLink<T> : Link<T> {
    open val state: LinkStateManager<T> = BaseLinkStateManager()
    override val retryAfterBreak = true
    override val breakCondition: BreakCondition? = null
    override val beforeAction: Action? = null
    override val afterAction: Action? = null
}
