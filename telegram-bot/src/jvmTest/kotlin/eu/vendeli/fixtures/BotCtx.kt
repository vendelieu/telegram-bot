@file:Suppress("ObjectPropertyName", "TestFunctionName")

package eu.vendeli.fixtures

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.implementations.ClassDataImpl
import eu.vendeli.tgbot.implementations.UserDataMapImpl
import eu.vendeli.tgbot.types.User

private val _userData: UserDataMapImpl = eu.vendeli.tgbot.implementations
    .UserDataMapImpl()

private val _classData: ClassDataImpl = eu.vendeli.tgbot.implementations
    .ClassDataImpl()

public suspend fun ____clearClassData(tgId: Long): Unit = _classData.clearAll(tgId)

public val TelegramBot.userData: UserDataMapImpl
    get() = _userData

public val TelegramBot.classData: ClassDataImpl
    get() = _classData

public operator fun User.`get`(key: String): String? = _userData[id, key]

public operator fun User.`set`(key: String, `value`: String): Unit = _userData.set(id, key, value)
