package eu.vendeli.tgbot.mock

import kotlin.system.exitProcess

private const val USAGE = """
KtGram Telegram Bot API mock server

  --host <host>            bind address (default 127.0.0.1)
  --port <port>            port (default 8081, 0 = random)
  --token <token>          only accept this token, repeatable (default: any well-formed token)
  --reject-unknown-chats   answer "chat not found" for chats the mock has not seen
  --rate-limit             enable Bot API flood limits (429 with retry_after)
  --require-https-webhook  reject http webhook urls like Telegram does
  --no-validate            skip request validation against the spec
"""

private fun fail(message: String): Nothing {
    System.err.println(message)
    System.err.println(USAGE)
    exitProcess(2)
}

internal fun parseArgs(args: List<String>): MockServerConfig {
    var config = MockServerConfig()
    val tokens = mutableSetOf<String>()
    var i = 0
    fun value(name: String) = args.getOrNull(++i) ?: fail("$name needs a value")
    while (i < args.size) {
        when (val arg = args[i]) {
            "--host" -> {
                config = config.copy(host = value(arg))
            }

            "--port" -> {
                config = config.copy(port = value(arg).toIntOrNull() ?: fail("--port must be a number"))
            }

            "--token" -> {
                tokens += value(arg)
            }

            "--reject-unknown-chats" -> {
                config = config.copy(unknownChats = UnknownChatPolicy.REJECT)
            }

            "--rate-limit" -> {
                config = config.copy(rateLimit = RateLimitConfig())
            }

            "--require-https-webhook" -> {
                config = config.copy(requireHttpsWebhook = true)
            }

            "--no-validate" -> {
                config = config.copy(validateRequests = false)
            }

            "--help", "-h" -> {
                println(USAGE)
                exitProcess(0)
            }

            else -> {
                fail("Unknown argument $arg")
            }
        }
        i++
    }
    return if (tokens.isEmpty()) config else config.copy(allowedTokens = tokens)
}

fun main(args: Array<String>) {
    val server = MockTelegramServer(parseArgs(args.toList())).start()
    println("Mock Telegram Bot API (${server.spec.version}) listening on ${server.baseUrl}")
    println("  bot config:   apiHost = \"${server.baseUrl}\"")
    println("  default token ${MockServerConfig.DEFAULT_TOKEN}")
    Runtime.getRuntime().addShutdownHook(Thread { server.close() })
    Thread.currentThread().join()
}
