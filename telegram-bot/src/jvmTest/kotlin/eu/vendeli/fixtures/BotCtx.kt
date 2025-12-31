@file:Suppress("ObjectPropertyName", "TestFunctionName", "ktlint:standard:function-naming")

package eu.vendeli.fixtures

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.implementations.ClassDataImpl
import eu.vendeli.tgbot.implementations.UserDataMapImpl
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.common.CtxUtils

private val _userData: UserDataMapImpl = eu.vendeli.tgbot.implementations
    .UserDataMapImpl()

@Suppress("ClassName")
@KtGramInternal
object __CtxUtils : CtxUtils {
    override val isClassDataInitialized: Lazy<Unit> = lazy { Unit }
    override suspend fun clearClassData(tgId: Long): Unit = _classData.clearAll(tgId)
}

@OptIn(KtGramInternal::class)
private val _classData: ClassDataImpl = ClassDataImpl().also {
    __CtxUtils.isClassDataInitialized.value
}

public val TelegramBot.userData: UserDataMapImpl
    get() = _userData

public val TelegramBot.classData: ClassDataImpl
    get() = _classData

public operator fun User.`get`(key: String): String? = _userData[id, key]

public operator fun User.`set`(key: String, `value`: String): Unit = _userData.set(id, key, value)
