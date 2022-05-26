package com.github.vendelieu.tgbot.types.internal

data class Actions(
    val commands: Map<String, Invocation>,
    val inputs: Map<String, Invocation>,
    val unhandled: Invocation? = null,
)
