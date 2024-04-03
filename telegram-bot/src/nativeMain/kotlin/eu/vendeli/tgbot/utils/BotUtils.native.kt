package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ChainLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal actual val PROCESSING_DISPATCHER = Dispatchers.IO
actual inline fun <T : ChainLink> InputListener.setChain(
    user: User,
    firstLink: T,
) = set(user, firstLink::class.qualifiedName!!)

fun TelegramBot.defineActivities(input: Map<String, List<Any?>>) {
    activities = input
}
private var activities: Map<String, List<Any?>> = emptyMap()

@Suppress("ObjectPropertyName", "ktlint:standard:backing-property-naming")
actual val _OperatingActivities: Map<String, List<Any?>>
    get() = activities
