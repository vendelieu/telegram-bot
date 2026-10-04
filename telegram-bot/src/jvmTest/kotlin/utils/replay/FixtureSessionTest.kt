package utils.replay

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class FixtureSessionTest : AnnotationSpec() {
    private fun ok(method: String, keys: List<String>, value: String) = RecordedExchange(
        method = method,
        requestKeys = keys,
        status = 200,
        contentType = "application/json",
        json = buildJsonObject {
            put("ok", true)
            put("result", value)
        },
    )

    private fun session(vararg exchanges: RecordedExchange, test: String = "t") = FixtureSession(
        "FakeSpec",
        TestMode.REPLAY,
    ) { Cassette(mapOf(test to exchanges.toList())) }.also { it.currentTest = test }

    private suspend fun FixtureSession.call(method: String, body: String = "{}") = replayClient()
        .post("https://api.telegram.org/bot1:x/$method") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.bodyAsText()

    @Test
    suspend fun `serves recorded responses in order per api method`() {
        val s = session(
            ok("sendMessage", emptyList(), "first"),
            ok("getMe", emptyList(), "me"),
            ok("sendMessage", emptyList(), "second"),
        )

        s.call("sendMessage") shouldContain "first"
        s.call("getMe") shouldContain "me"
        s.call("sendMessage") shouldContain "second"
    }

    @Test
    suspend fun `fails with a re-record hint when nothing was recorded`() {
        val failure = shouldThrow<ReplayMissException> { session().call("sendMessage") }

        failure.message!! shouldContain "recordFixtures"
    }

    @Test
    suspend fun `fails when the same method is called more often than recorded`() {
        val s = session(ok("getMe", emptyList(), "me"))
        s.call("getMe")

        shouldThrow<ReplayMissException> { s.call("getMe") }
    }

    @Test
    suspend fun `fails when request parameters changed`() {
        val s = session(ok("sendMessage", listOf("chat_id", "text"), "x"))

        val failure = shouldThrow<ReplayMissException> { s.call("sendMessage", """{"chat_id":1}""") }

        failure.message!! shouldContain "changed"
    }

    @Test
    suspend fun `matches parameters regardless of their values`() {
        val s = session(ok("sendMessage", listOf("chat_id", "text"), "x"))

        s.call("sendMessage", """{"text":"other","chat_id":99}""") shouldContain "\"x\""
    }

    @Test
    suspend fun `keeps exchanges of different tests apart`() {
        val s = FixtureSession("FakeSpec", TestMode.REPLAY) {
            Cassette(
                mapOf(
                    "a" to listOf(ok("getMe", emptyList(), "from-a")),
                    "b" to listOf(ok("getMe", emptyList(), "from-b")),
                ),
            )
        }

        s.currentTest = "b"
        s.call("getMe") shouldContain "from-b"
        s.currentTest = "a"
        s.call("getMe") shouldContain "from-a"
    }

    @Test
    fun `builds exchange keys for api calls downloads and external urls`() {
        FixtureSession.exchangeKey(io.ktor.http.Url("https://api.telegram.org/bot1:x/sendMessage")) shouldBe
            "sendMessage"
        FixtureSession.exchangeKey(io.ktor.http.Url("https://api.telegram.org/bot1:x/test/sendMessage")) shouldBe
            "sendMessage"
        FixtureSession.exchangeKey(io.ktor.http.Url("https://api.telegram.org/file/bot1:x/photos/a.jpg")) shouldBe
            "file:photos/a.jpg"
        FixtureSession.exchangeKey(io.ktor.http.Url("https://picsum.photos/10")) shouldBe "ext:picsum.photos/10"
    }
}
