package utils.replay

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import utils.TestEnv
import java.io.File

/** Guards the committed fixtures: nothing secret or personal may ever land in them. */
class FixtureHygieneTest : AnnotationSpec() {
    private val fixtures: List<File> by lazy {
        FixtureHygieneTest::class.java.classLoader
            .getResource("tg-fixtures")
            ?.let { File(it.toURI()).listFiles { f -> f.extension == "json" }?.toList() }
            .orEmpty()
    }

    @Test
    fun `fixtures exist`() {
        fixtures.shouldNotBeEmpty()
    }

    @Test
    fun `fixtures contain no bot tokens`() {
        fixtures
            .filter { TOKEN_REGEX.containsMatchIn(it.readText()) }
            .map { it.name }
            .shouldBeEmpty()
    }

    @Test
    fun `fixtures contain no configured credentials or real ids`() {
        val secrets = SECRET_KEYS.mapNotNull { TestEnv.get(it) }.filter { it.length >= MIN_SECRET_LENGTH }
        fixtures
            .filter { file -> file.readText().let { text -> secrets.any { it in text } } }
            .map { it.name }
            .shouldBeEmpty()
    }

    @Test
    fun `fixtures contain no invite links`() {
        fixtures
            .filter { INVITE_LINK_REGEX.containsMatchIn(it.readText()) }
            .map { it.name }
            .shouldBeEmpty()
    }

    private companion object {
        const val MIN_SECRET_LENGTH = 5
        val SECRET_KEYS = listOf(
            "BOT_TOKEN",
            "BOT_TOKEN_2",
            "PAYMENT_PROVIDER_TOKEN",
            "TELEGRAM_ID",
            "CHAT_ID",
            "CHANNEL_ID",
        )
        val TOKEN_REGEX = Regex("\\d{6,}:[A-Za-z0-9_-]{30,}")
        val INVITE_LINK_REGEX = Regex("https://t\\.me/\\+(?!redacted)")
    }
}
