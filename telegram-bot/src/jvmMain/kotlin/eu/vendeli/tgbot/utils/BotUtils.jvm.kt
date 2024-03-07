package eu.vendeli.tgbot.utils

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

private val activities =
    Class.forName("eu.vendeli.tgbot.generated.ActivitiesDataKt")
        .getDeclaredMethod("get__ACTIVITIES")
        .invoke(null)

@Suppress("UNCHECKED_CAST", "ObjectPropertyName")
actual val _OperatingActivities: Map<String, List<Any?>>
    get() = activities as Map<String, List<Any?>>
