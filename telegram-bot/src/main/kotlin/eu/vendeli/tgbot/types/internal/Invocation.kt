package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.utils.EMPTY_MAP
import java.lang.reflect.Method

data class Invocation(val clazz: Class<*>, val method: Method, val namedParameters: Map<String, String> = EMPTY_MAP)
