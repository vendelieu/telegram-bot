package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class Birthdate(
    val day: Int,
    val month: Int,
    val year: Int? = null,
)
