package utils

data class BotData(
    val id: Long,
    val token: String,
)

object BotResource : ResourcePicker<BotData>(
    listOf(
        System.getenv("BOT_TOKEN").let {
            BotData(it.substringBefore(':').toLong(), it)
        },
        System.getenv("BOT_TOKEN_2").let {
            BotData(it.substringBefore(':').toLong(), it)
        },
    ),
)
