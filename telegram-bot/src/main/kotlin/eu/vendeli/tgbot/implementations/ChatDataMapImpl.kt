package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ChatData
import eu.vendeli.tgbot.utils.asyncAction
import kotlinx.coroutines.Deferred

class ChatDataMapImpl : BotContextMapImpl(), ChatData {
    override fun clearAll(telegramId: Long) {
        storage.keys.forEach(storage::remove)
    }

    override suspend fun clearAllAsync(telegramId: Long): Deferred<Boolean> = asyncAction {
        storage.keys.forEach(storage::remove)
        true
    }
}
