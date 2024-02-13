package eu.vendeli.tgbot.utils

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
