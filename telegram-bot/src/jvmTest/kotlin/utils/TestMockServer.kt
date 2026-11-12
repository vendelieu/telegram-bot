package utils

import eu.vendeli.tgbot.mock.MockServerConfig
import eu.vendeli.tgbot.mock.MockTelegramServer
import eu.vendeli.tgbot.mock.UnknownChatPolicy

/** Mock servers of the test JVM: one shared by default, private ones for specs that configure their own. */
object TestMockServer {
    private val shared: MockTelegramServer by lazy {
        start(baseConfig()).also { started ->
            Runtime.getRuntime().addShutdownHook(Thread { started.close() })
        }
    }

    /** The server a spec runs against, [private] is true when the caller has to close it after the spec. */
    fun serverFor(backend: TestBackend.Mock): Pair<MockTelegramServer, Boolean> {
        val configure = backend.configure ?: return shared to false
        return start(configure(baseConfig()).copy(port = 0)) to true
    }

    /** Strict like Telegram: chats and users that were never registered are unknown. See [TestWorld]. */
    private fun baseConfig() = MockServerConfig(port = 0, unknownChats = UnknownChatPolicy.REJECT, strictUsers = true)

    private fun start(config: MockServerConfig) = MockTelegramServer(config).start()
}
