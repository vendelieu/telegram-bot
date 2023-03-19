package other.pckg

import eu.vendeli.tgbot.interfaces.BotChatData
import kotlinx.coroutines.Deferred

class BotChatDataImpl : BotChatData {
    override fun set(telegramId: Long, key: String, value: Any?) {
        TODO("Not yet implemented")
    }

    override suspend fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean> {
        TODO("Not yet implemented")
    }

    override fun <T> get(telegramId: Long, key: String): T? {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getAsync(telegramId: Long, key: String): Deferred<T?> {
        TODO("Not yet implemented")
    }

    override fun del(telegramId: Long, key: String) {
        TODO("Not yet implemented")
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> {
        TODO("Not yet implemented")
    }

    override fun delPrevChatSection(telegramId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun delPrevChatSectionAsync(telegramId: Long): Deferred<Boolean> {
        TODO("Not yet implemented")
    }
}
