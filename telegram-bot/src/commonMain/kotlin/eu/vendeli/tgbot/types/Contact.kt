package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val userId: Long? = null,
    val vcard: String? = null,
)
