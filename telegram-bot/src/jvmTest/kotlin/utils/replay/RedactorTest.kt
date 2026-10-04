package utils.replay

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotContain
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class RedactorTest : AnnotationSpec() {
    private val realToken = "123456789:AAH-realSecretTokenValue_1234567890abcd"
    private val redactor = Redactor(
        idReplacements = mapOf(
            "5550001" to FakeIds.TG_ID,
            "-1005550002" to FakeIds.CHAT_ID,
            "123456789" to FakeIds.BOT_ID,
        ),
        tokens = listOf(realToken),
    )

    private fun parse(text: String): JsonObject = Json.parseToJsonElement(text).jsonObject

    @Test
    fun `replaces configured tokens and anything token shaped`() {
        val text = redactor.redact("url /bot$realToken/getMe and 999999:ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")

        text shouldNotContain "AAH-real"
        text shouldNotContain "ABCDEFGHIJ"
        text shouldBe "url /bot${FakeIds.BOT_TOKEN}/getMe and ${FakeIds.BOT_TOKEN}"
    }

    @Test
    fun `maps real ids to fake ones only on whole numbers`() {
        redactor.redact("chat -1005550002 user 5550001 other 15550001 and 55500012") shouldBe
            "chat ${FakeIds.CHAT_ID} user ${FakeIds.TG_ID} other 15550001 and 55500012"
    }

    @Test
    fun `maps numeric json ids`() {
        val result = redactor.redact(parse("""{"chat":{"id":-1005550002},"user_id":5550001}""")).jsonObject

        result["chat"]!!.jsonObject["id"]!!.jsonPrimitive.content shouldBe FakeIds.CHAT_ID.toString()
        result["user_id"]!!.jsonPrimitive.content shouldBe FakeIds.TG_ID.toString()
    }

    @Test
    fun `redacts names of users but keeps values a test sent itself`() {
        val result = redactor
            .redact(
                parse(
                    """{"from":{"id":5550001,"is_bot":false,"first_name":"Real","username":"real_name"},
                    "contact":{"phone_number":"1","first_name":"test"}}""",
                ),
            ).jsonObject

        val from = result["from"]!!.jsonObject
        from["first_name"] shouldBe JsonPrimitive("redacted")
        from["username"] shouldBe JsonPrimitive("redacted")
        result["contact"]!!.jsonObject["first_name"] shouldBe JsonPrimitive("test")
    }

    @Test
    fun `redacts invite links and sticker set owners`() {
        redactor.redact("see https://t.me/+AbCdEf123_- now") shouldBe "see https://t.me/+redacted now"
        redactor.redact("Test_1_by_some_real_bot") shouldBe "Test_1_by_redacted"
    }
}
