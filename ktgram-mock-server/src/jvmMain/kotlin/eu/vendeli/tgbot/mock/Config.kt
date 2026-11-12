package eu.vendeli.tgbot.mock

import java.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/** What to do when a bot sends to a chat the mock has not seen yet. */
enum class UnknownChatPolicy {
    /** Create a private chat on the fly (convenient for tests with fixed ids). */
    CREATE,

    /** Answer `400 Bad Request: chat not found` like Telegram does. */
    REJECT,
}

/** How the 4096/1024 character limits of messages are counted. */
enum class TextLengthUnit {
    /** Unicode code points, what the Bot API documents as "characters". */
    CODE_POINTS,

    /** UTF-16 code units, the unit entity offsets use. */
    UTF16,
    ;

    fun length(text: String): Int = if (this == CODE_POINTS) text.codePointCount(0, text.length) else text.length
}

/**
 * Limits modelled after the documented Bot API flood limits. Exceeding one answers `429` with `retry_after`.
 */
data class RateLimitConfig(
    val perChatPerSecond: Int = 1,
    val perGroupPerMinute: Int = 20,
    val globalPerSecond: Int = 30,
)

data class MockServerConfig(
    val host: String = "127.0.0.1",
    /** `0` picks a free port, read it back with [MockTelegramServer.port]. */
    val port: Int = 8081,
    val clock: Clock = Clock.systemUTC(),
    val spec: ApiSpec? = null,
    val unknownChats: UnknownChatPolicy = UnknownChatPolicy.CREATE,
    /** Answer `user not found` / `PARTICIPANT_ID_INVALID` for user ids that were never registered with the bot. */
    val strictUsers: Boolean = false,
    val validateRequests: Boolean = true,
    val textLength: TextLengthUnit = TextLengthUnit.CODE_POINTS,
    /** Download http(s) urls given as files, so that `getFile` and downloads return the real bytes. */
    val fetchUrlFiles: Boolean = false,
    /** When set, only these tokens are accepted, everything else gets `401 Unauthorized`. */
    val allowedTokens: Set<String>? = null,
    val rateLimit: RateLimitConfig? = null,
    /** Telegram only delivers webhooks to https urls, the mock accepts http unless this is set. */
    val requireHttpsWebhook: Boolean = false,
    val webhookRetryDelay: Duration = 1.seconds,
) {
    companion object {
        const val DEFAULT_TOKEN = "123456789:AAMockServerToken_0123456789abcdefg"
    }
}
