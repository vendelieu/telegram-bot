package com.github.vendelieu.tgbot.types

data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials
)
