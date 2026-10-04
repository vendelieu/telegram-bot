package utils.replay

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import utils.TestEnv

/** Stable identifiers that replace the real ones in fixtures and are used by the tests in replay mode. */
object FakeIds {
    const val TG_ID = 100_000_001L
    const val CHAT_ID = -1_000_000_000_002L
    const val CHANNEL_ID = -1_000_000_000_003L
    const val BOT_ID = 700_000_001L
    const val BOT_TOKEN = "$BOT_ID:replay-token"
    const val PAYMENT_PROVIDER_TOKEN = "replay-provider-token"
}

/**
 * Removes credentials and personal data from recorded responses.
 * Real ids are mapped to [FakeIds] so that replayed responses stay consistent with the ids the tests use.
 */
class Redactor(
    private val idReplacements: Map<String, Long>,
    private val tokens: List<String>,
) {
    fun redact(element: JsonElement): JsonElement = when (element) {
        is JsonObject -> redactObject(element)
        is JsonArray -> JsonArray(element.map(::redact))
        is JsonPrimitive -> if (element.isString) JsonPrimitive(redact(element.content)) else redactNumber(element)
    }

    /** Names are personal only on user objects, values that a test sent itself (e.g. a contact) must stay intact. */
    private fun redactObject(obj: JsonObject): JsonObject {
        val isUser = "is_bot" in obj || (obj["id"] as? JsonPrimitive)?.content in idReplacements
        return JsonObject(
            obj.mapValues { (key, value) ->
                if (isUser && key in PERSONAL_KEYS && value is JsonPrimitive && value.isString) {
                    JsonPrimitive(PERSONAL_PLACEHOLDER)
                } else {
                    redact(value)
                }
            },
        )
    }

    fun redact(text: String): String {
        var result = tokens.fold(text) { acc, token -> acc.replace(token, FakeIds.BOT_TOKEN) }
        result = TOKEN_REGEX.replace(result, FakeIds.BOT_TOKEN)
        result = STICKER_OWNER_REGEX.replace(result, PERSONAL_PLACEHOLDER)
        result = INVITE_LINK_REGEX.replace(result, INVITE_LINK_PLACEHOLDER)
        return idReplacements.entries.fold(result) { acc, (real, fake) ->
            Regex("(?<!\\d)${Regex.escape(real)}(?!\\d)").replace(acc, fake.toString())
        }
    }

    private fun redactNumber(primitive: JsonPrimitive): JsonPrimitive {
        val fake = idReplacements[primitive.content] ?: return primitive
        return JsonPrimitive(fake)
    }

    companion object {
        private const val PERSONAL_PLACEHOLDER = "redacted"
        private const val INVITE_LINK_PLACEHOLDER = "https://t.me/+redacted"
        private val PERSONAL_KEYS = setOf("first_name", "last_name", "username")
        private val TOKEN_REGEX = Regex("\\d{6,}:[A-Za-z0-9_-]{30,}")
        private val STICKER_OWNER_REGEX = Regex("(?<=_by_)[A-Za-z0-9_]+")
        private val INVITE_LINK_REGEX = Regex("https://t\\.me/\\+[A-Za-z0-9_-]+")

        /** Builds a redactor from the credentials currently configured in `.env`. */
        fun fromEnv(): Redactor {
            val ids = buildMap {
                TestEnv.get("TELEGRAM_ID")?.let { put(it, FakeIds.TG_ID) }
                TestEnv.get("CHAT_ID")?.let { put(it, FakeIds.CHAT_ID) }
                TestEnv.get("CHANNEL_ID")?.let { put(it, FakeIds.CHANNEL_ID) }
            }
            val tokens = listOfNotNull(TestEnv.get("BOT_TOKEN"), TestEnv.get("BOT_TOKEN_2"))
            val botIds = tokens.mapNotNull { it.substringBefore(':').takeIf { id -> id.all(Char::isDigit) } }
            return Redactor(ids + botIds.associateWith { FakeIds.BOT_ID }, tokens)
        }
    }
}
