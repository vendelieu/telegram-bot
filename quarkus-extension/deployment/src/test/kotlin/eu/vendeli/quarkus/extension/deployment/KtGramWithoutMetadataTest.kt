package eu.vendeli.quarkus.extension.deployment

import eu.vendeli.quarkus.extension.QuarkusClassManager
import eu.vendeli.tgbot.interfaces.helper.ContextLoader
import io.quarkus.test.QuarkusExtensionTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

/** An app built with a ktnip version that doesn't generate handler metadata. */
class KtGramWithoutMetadataTest {
    @Inject
    lateinit var classManager: QuarkusClassManager

    @Test
    fun `handlers are still resolved from CDI`() {
        val handler = classManager.getInstance(InjectedHandler::class) as InjectedHandler

        assertSame(classManager, handler.classManager)
    }

    /** Only CDI can create it: KtGram's reflection fallback can't supply constructor arguments. */
    @Singleton
    class InjectedHandler(
        val classManager: QuarkusClassManager,
    )

    companion object {
        @JvmField
        @RegisterExtension
        val test: QuarkusExtensionTest = QuarkusExtensionTest()
            .withApplicationRoot { jar ->
                jar
                    .addClasses(InjectedHandler::class.java, KtGramExtensionTest.EmptyContextLoader::class.java)
                    .addAsServiceProvider(ContextLoader::class.java, KtGramExtensionTest.EmptyContextLoader::class.java)
            }.overrideConfigKey("platform.quarkus.native.builder-image", "mandrel")
            .overrideConfigKey("ktgram.auto-start-polling", "false")
            .overrideConfigKey("ktgram.bot[0].token", "1:only")
    }
}
