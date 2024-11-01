package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import kotlinx.coroutines.Dispatchers
import kotlin.reflect.KClass

internal actual val PROCESSING_DISPATCHER = Dispatchers.IO

@KtGramInternal
@Suppress("UnusedReceiverParameter")
fun TelegramBot.defineActivities(input: Map<String, List<Any?>>) {
    activities = input
}

@Suppress("UNCHECKED_CAST")
private var activities: Map<String, List<Any?>> = runCatching {
    Class
        .forName("eu.vendeli.tgbot.generated.ActivitiesDataKt")
        .getDeclaredMethod("get__ACTIVITIES")
        .invoke(null) as Map<String, List<Any?>>
}.getOrNull() ?: emptyMap()

@Suppress("ObjectPropertyName", "ktlint:standard:backing-property-naming")
actual val _OperatingActivities: Map<String, List<Any?>>
    get() = activities

actual val KClass<*>.fqName: String
    get() = qualifiedName ?: simpleName ?: "Unknown"
