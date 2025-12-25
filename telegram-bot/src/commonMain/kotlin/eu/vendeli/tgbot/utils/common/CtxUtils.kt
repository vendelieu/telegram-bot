package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.annotations.internal.KtGramInternal

@KtGramInternal
interface CtxUtils {
    val isClassDataInitialized: Lazy<Unit>

    suspend fun clearClassData(tgId: Long): Unit
}
