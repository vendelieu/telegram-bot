package utils

data class BotData(
    val id: Long,
    val token: String,
)

/** Bots of the real Telegram account, used by [TestBackend.Live] specs. */
object BotResource : ResourcePicker<BotData>(
    listOf(
        (TestEnv.get("BOT_TOKEN") ?: "1:token").let {
            BotData(it.substringBefore(':').toLongOrNull() ?: 0L, it)
        },
        (TestEnv.get("BOT_TOKEN_2") ?: "2:token2").let {
            BotData(it.substringBefore(':').toLongOrNull() ?: 0L, it)
        },
    ),
)
