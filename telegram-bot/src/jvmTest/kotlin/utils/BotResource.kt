package utils

data class BotData(
    val id: Long,
    val token: String,
)

object BotResource : ResourcePicker<BotData>(
    listOf(
        (System.getenv("BOT_TOKEN") ?: "1:token").let {
            BotData(it.substringBefore(':').toLongOrNull() ?: 0L, it)
        },
        (System.getenv("BOT_TOKEN_2") ?: "2:token2").let {
            BotData(it.substringBefore(':').toLongOrNull() ?: 0L, it)
        },
    ),
)
