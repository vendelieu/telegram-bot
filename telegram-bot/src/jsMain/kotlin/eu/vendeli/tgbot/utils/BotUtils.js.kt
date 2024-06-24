package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import kotlinx.coroutines.Dispatchers
import kotlin.reflect.KClass

internal actual val PROCESSING_DISPATCHER = Dispatchers.Unconfined

fun TelegramBot.defineActivities(input: Map<String, List<Any?>>) {
    activities = input
}
private var activities: Map<String, List<Any?>> = emptyMap()

@Suppress("ObjectPropertyName", "ktlint:standard:backing-property-naming")
actual val _OperatingActivities: Map<String, List<Any?>>
    get() = activities

@Suppress("unused")
internal actual inline val KClass<*>.fullName: String get() = simpleName ?: "Unknown"
