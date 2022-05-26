package com.github.vendelieu.tgbot.types.internal

import java.lang.reflect.Method

data class Invocation(val clazz: Class<*>, val method: Method, val namedParameters: Map<String, String> = mapOf())
