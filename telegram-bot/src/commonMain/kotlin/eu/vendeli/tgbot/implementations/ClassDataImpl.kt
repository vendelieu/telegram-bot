package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ctx.ClassData

class ClassDataImpl :
    BotContextMapImpl(),
    ClassData<String> {
    override suspend fun clearAll(telegramId: Long) {
        storage.entries.removeAll { it.key.startsWith("$telegramId-") }
    }
}
