package eu.vendeli.quarkus.extension.deployment

import eu.vendeli.quarkus.extension.BotConfiguration
import eu.vendeli.quarkus.extension.QuarkusClassManager
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.helper.ContextLoader
import eu.vendeli.tgbot.utils.common.BotConfigurator
import io.quarkus.arc.Arc
import io.quarkus.test.QuarkusExtensionTest
import jakarta.enterprise.inject.Instance
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.jboss.shrinkwrap.api.asset.StringAsset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class KtGramExtensionTest {
    @Inject
    lateinit var bots: List<@JvmSuppressWildcards TelegramBot>

    @Inject
    lateinit var classManager: QuarkusClassManager

    @Inject
    lateinit var singleBot: Instance<TelegramBot>

    @Test
    fun `bots are created from config`() {
        assertEquals(listOf("first", "second"), bots.map { it.identifier })
    }

    @Test
    fun `single bot injection explains itself when several bots are configured`() {
        val error = assertThrows(IllegalStateException::class.java) { singleBot.get() }
        assertTrue(error.message!!.contains("2 bots are configured"))
    }

    @Test
    fun `bot configuration bean is applied to the matching bot`() {
        assertEquals(1, FirstBotConfiguration.applied)
    }

    @Test
    fun `class manager resolves handlers listed in ktgram metadata from CDI`() {
        val handler = classManager.getInstance(SampleHandler::class) as SampleHandler

        assertTrue(handler.isCdiManaged(), "handler should be created by CDI, not by the reflection fallback")
        assertSame(handler, classManager.getInstance(SampleHandler::class))
    }

    @Test
    fun `beans that ktgram metadata doesn't list are still removed`() {
        assertFalse(Arc.container().instance(UnlistedBean::class.java).isAvailable)
    }

    @Test
    fun `class manager falls back to reflection for non-beans`() {
        assertTrue(classManager.getInstance(PlainHandler::class) is PlainHandler)
    }

    @Singleton
    class FirstBotConfiguration : BotConfiguration() {
        override val identifier = "first"
        override val autostartLongPolling = false

        override fun applyCfg(): BotConfigurator = { applied++ }

        companion object {
            var applied = 0
        }
    }

    @Singleton
    class SampleHandler {
        @Inject
        lateinit var classManager: QuarkusClassManager

        fun isCdiManaged() = ::classManager.isInitialized
    }

    class PlainHandler

    @Singleton
    class UnlistedBean

    /** Stands in for the KSP-generated handler registry. */
    class EmptyContextLoader : ContextLoader {
        override fun load(bot: TelegramBot) = Unit
    }

    companion object {
        @JvmField
        @RegisterExtension
        val test: QuarkusExtensionTest = QuarkusExtensionTest()
            .withApplicationRoot { jar ->
                jar
                    .addClasses(
                        FirstBotConfiguration::class.java,
                        SampleHandler::class.java,
                        PlainHandler::class.java,
                        UnlistedBean::class.java,
                    ).addAsServiceProvider(ContextLoader::class.java, EmptyContextLoader::class.java)
                    // What ktnip generates for a handler class (only SampleHandler, not PlainHandler).
                    .addAsResource(
                        StringAsset("""{ "reflection": [ { "type": "${SampleHandler::class.java.name}" } ] }"""),
                        "META-INF/native-image/eu.vendeli.tgbot.generated/ktgram/reachability-metadata.json",
                    )
            }
            // Normally provided by the app's Quarkus platform BOM, which an extension test doesn't have.
            .overrideConfigKey("platform.quarkus.native.builder-image", "mandrel")
            .overrideConfigKey("ktgram.auto-start-polling", "false")
            .overrideConfigKey("ktgram.bot[0].token", "1:first")
            .overrideConfigKey("ktgram.bot[0].identifier", "first")
            .overrideConfigKey("ktgram.bot[1].token", "2:second")
            .overrideConfigKey("ktgram.bot[1].identifier", "second")
    }
}
