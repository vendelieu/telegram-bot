package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ClassData

class ClassDataImpl :
    BotContextMapImpl(),
    ClassData<String> {
    override suspend fun clearAll(telegramId: Long) {
        storage.keys.forEach(storage::remove)
    }
}
