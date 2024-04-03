package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ChainLink
import kotlinx.coroutines.Dispatchers

internal actual val PROCESSING_DISPATCHER = Dispatchers.IO

@Suppress("NOTHING_TO_INLINE")
actual inline fun <T : ChainLink> InputListener.setChain(
    user: User,
    firstLink: T,
) = set(user, firstLink::class.qualifiedName!!)

@InternalApi
@Suppress("UnusedReceiverParameter")
fun TelegramBot.defineActivities(input: Map<String, List<Any?>>) {
    activities = input
}

@Suppress("UNCHECKED_CAST")
private var activities: Map<String, List<Any?>> = runCatching {
    Class.forName("eu.vendeli.tgbot.generated.ActivitiesDataKt")
        .getDeclaredMethod("get__ACTIVITIES")
        .invoke(null) as Map<String, List<Any?>>
}.getOrNull() ?: emptyMap()

@Suppress("ObjectPropertyName", "ktlint:standard:backing-property-naming")
actual val _OperatingActivities: Map<String, List<Any?>>
    get() = activities
