package utils

import utils.replay.FakeIds
import utils.replay.TestMode

data class BotData(
    val id: Long,
    val token: String,
)

object BotResource : ResourcePicker<BotData>(
    if (TestMode.current.isReplay) listOf(BotData(FakeIds.BOT_ID, FakeIds.BOT_TOKEN)) else listOf(
        (TestEnv.get("BOT_TOKEN") ?: "1:token").let {
            BotData(it.substringBefore(':').toLongOrNull() ?: 0L, it)
        },
        (TestEnv.get("BOT_TOKEN_2") ?: "2:token2").let {
            BotData(it.substringBefore(':').toLongOrNull() ?: 0L, it)
        },
    ),
)
