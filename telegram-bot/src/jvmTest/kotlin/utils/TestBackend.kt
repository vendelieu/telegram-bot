package utils

import eu.vendeli.tgbot.mock.MockServerConfig

/**
 * What a spec talks to. Chosen in code, per spec: `class MyTest : BotTestContext(backend = TestBackend.Live)`.
 * Specs that do not choose use [TestBackends.default].
 */
sealed interface TestBackend {
    val isMock: Boolean
    val isLive: Boolean get() = !isMock

    /**
     * The in-process Telegram mock (`ktgram-mock-server`). Without [configure] the specs share one server, a
     * configured mock gets a private server for the duration of the spec:
     * `TestBackend.Mock { copy(rateLimit = RateLimitConfig()) }`.
     */
    class Mock(
        internal val configure: ((MockServerConfig) -> MockServerConfig)? = null,
    ) : TestBackend {
        override val isMock get() = true
    }

    /** Real Telegram, needs the credentials from `.env` (see env.example). */
    data object Live : TestBackend {
        override val isMock get() = false
    }
}

/** The backend of specs that do not pick one; `TG_TEST_BACKEND=live` (or `mock`) only sets this default. */
object TestBackends {
    const val ENV_KEY = "TG_TEST_BACKEND"

    @Volatile
    var default: TestBackend = parse(TestEnv.get(ENV_KEY))

    fun parse(value: String?): TestBackend = when (value?.trim()?.lowercase()) {
        null, "", "mock" -> TestBackend.Mock()
        "live" -> TestBackend.Live
        else -> error("Unknown $ENV_KEY='$value', expected one of: mock, live")
    }
}

/** Stable ids and tokens the specs use on the mock, so nothing depends on `.env`. */
object FakeIds {
    const val TG_ID = 100_000_001L
    const val CHAT_ID = -1_000_000_000_002L
    const val CHANNEL_ID = -1_000_000_000_003L
    const val BOT_ID = 700_000_001L
    const val BOT_TOKEN = "$BOT_ID:MockServerToken_0123456789abcdefg"
    const val PAYMENT_PROVIDER_TOKEN = "mock-provider-token"
}
