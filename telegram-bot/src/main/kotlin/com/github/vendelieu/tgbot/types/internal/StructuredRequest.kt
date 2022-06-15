package com.github.vendelieu.tgbot.types.internal

data class StructuredRequest(
    val command: String,
    val params: Map<String, String>
)
