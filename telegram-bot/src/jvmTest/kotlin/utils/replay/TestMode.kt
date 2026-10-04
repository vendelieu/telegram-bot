package utils.replay

import utils.TestEnv

/**
 * How the jvm tests talk to Telegram. Selected with the `TG_TEST_MODE` variable.
 *
 * - [REPLAY] (default): no network and no credentials, recorded responses from `tg-fixtures` are served.
 * - [RECORD]: real Telegram is called and every exchange is stored (redacted) in the fixtures directory.
 * - [LIVE]: real Telegram is called, nothing is stored.
 */
enum class TestMode {
    REPLAY,
    RECORD,
    LIVE,
    ;

    val isReplay get() = this == REPLAY
    val isRecord get() = this == RECORD

    companion object {
        const val ENV_KEY = "TG_TEST_MODE"

        val current: TestMode by lazy { parse(TestEnv.get(ENV_KEY)) }

        fun parse(value: String?): TestMode = when (value?.trim()?.lowercase()) {
            null, "", "replay" -> REPLAY
            "record" -> RECORD
            "live" -> LIVE
            else -> error("Unknown $ENV_KEY='$value', expected one of: replay, record, live")
        }
    }
}
