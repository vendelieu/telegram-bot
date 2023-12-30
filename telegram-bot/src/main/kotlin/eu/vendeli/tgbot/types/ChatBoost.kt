package eu.vendeli.tgbot.types

data class ChatBoost(
 val boostId: String,
 val addDate: Long,
 val expirationDate: Long,
 val source: ChatBoostSource
)
