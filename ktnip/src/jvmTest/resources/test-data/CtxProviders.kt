// @generated KtGramCtxLoader.kt: KtGramCtxLoader, load
// @generated BotCtx.kt: TestProviderUser, TestProviderClass

import eu.vendeli.tgbot.annotations.CtxProvider
import eu.vendeli.tgbot.interfaces.ctx.ClassData
import eu.vendeli.tgbot.interfaces.ctx.UserData
import kotlinx.coroutines.Deferred

@CtxProvider
class TestProviderUser : UserData<Int> {
    override fun get(telegramId: Long, key: String): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun getAsync(
        telegramId: Long,
        key: String,
    ): Deferred<Int?> {
        TODO("Not yet implemented")
    }

    override fun set(telegramId: Long, key: String, value: Int?) {
        TODO("Not yet implemented")
    }

    override suspend fun setAsync(
        telegramId: Long,
        key: String,
        value: Int?,
    ): Deferred<Boolean> {
        TODO("Not yet implemented")
    }

    override fun del(telegramId: Long, key: String) {
        TODO("Not yet implemented")
    }

    override suspend fun delAsync(
        telegramId: Long,
        key: String,
    ): Deferred<Boolean> {
        TODO("Not yet implemented")
    }
}

@CtxProvider
class TestProviderClass : ClassData<Long> {
    override suspend fun clearAll(telegramId: Long) {
        TODO("Not yet implemented")
    }

    override fun get(telegramId: Long, key: String): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun getAsync(
        telegramId: Long,
        key: String,
    ): Deferred<Long?> {
        TODO("Not yet implemented")
    }

    override fun set(telegramId: Long, key: String, value: Long?) {
        TODO("Not yet implemented")
    }

    override suspend fun setAsync(
        telegramId: Long,
        key: String,
        value: Long?,
    ): Deferred<Boolean> {
        TODO("Not yet implemented")
    }

    override fun del(telegramId: Long, key: String) {
        TODO("Not yet implemented")
    }

    override suspend fun delAsync(
        telegramId: Long,
        key: String,
    ): Deferred<Boolean> {
        TODO("Not yet implemented")
    }
}
