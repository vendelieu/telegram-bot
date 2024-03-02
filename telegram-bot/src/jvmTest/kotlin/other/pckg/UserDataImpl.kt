package other.pckg

import eu.vendeli.tgbot.interfaces.UserData
import kotlinx.coroutines.Deferred

class UserDataImpl : UserData {
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
}
