package other.pckg

import eu.vendeli.tgbot.interfaces.ClassData
import kotlinx.coroutines.Deferred

class ChatDataImpl : ClassData<String> {
    override fun set(telegramId: Long, key: String, value: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun setAsync(telegramId: Long, key: String, value: String?): Deferred<Boolean> {
        TODO("Not yet implemented")
    }

    override fun get(telegramId: Long, key: String): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<String?> {
        TODO("Not yet implemented")
    }

    override fun del(telegramId: Long, key: String) {
        TODO("Not yet implemented")
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun clearAll(telegramId: Long) {
        TODO("Not yet implemented")
    }
}
