data class BotData(
    val id: Long,
    val token: String,
)

object BotInstance {
    private val pool = listOf(
        System.getenv("BOT_TOKEN").let {
            BotData(it.substringBefore(':').toLong(), it)
        },
        System.getenv("BOT_TOKEN_2").let {
            BotData(it.substringBefore(':').toLong(), it)
        },
    )

    @Volatile
    var isMain = false

    val current: BotData
        get() {
            isMain = isMain.not()
            return if (isMain) main else secondary
        }

    val main: BotData get() = pool.first()
    val secondary: BotData get() = pool.last()
}
