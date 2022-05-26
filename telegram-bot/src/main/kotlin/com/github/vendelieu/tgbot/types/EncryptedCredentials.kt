package com.github.vendelieu.tgbot.types

data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String
)
