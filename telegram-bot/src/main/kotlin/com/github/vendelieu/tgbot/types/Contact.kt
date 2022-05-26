package com.github.vendelieu.tgbot.types

data class Contact(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val userId: Long? = null,
    val vcard: String?,
)
