package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.utils.processUpdate

data class FailedUpdate(
    val exception: Throwable,
    val update: Update,
) {
    val processedUpdate by lazy { update.processUpdate() }
}
