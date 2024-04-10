package eu.vendeli.ktor.starter

import io.ktor.server.config.ApplicationConfigurationException

internal object Configuration {
    private val env = System.getenv().filter { it.key.startsWith("TGBOT_") }
    private fun getRequired(name: String, missingExCause: () -> String): String =
        env[name] ?: missingExCause().also { throw ApplicationConfigurationException(it) }

    val TOKEN = getRequired("TOKEN") { "TOKEN is not set" }
    val PACKAGE = env["PACKAGE"]
    val HOST = env["HOST"] ?: "0.0.0.0"
    val PORT = env["PORT"]?.toInt() ?: 8080
    val SSL_PORT = env["SSL_PORT"]?.toInt() ?: 8443
    val WEBHOOK_URL = env["WEBHOOK_URL"] ?: "/$TOKEN"

    val PEM_PRIVATE_KEY_PATH = getRequired("PEM_PRIVATE_KEY_PATH") { "PEM_PRIVATE_KEY_PATH is not set" }
    val PEM_CHAIN_PATH = getRequired("CHAIN_PATH") { "PEM_CHAIN_PATH is not set" }
    val PEM_PRIVATE_KEY = getRequired("PRIVATE_KEY") { "PEM_PRIVATE_key is not set" }.toCharArray()

    val KEYSTORE_PATH = getRequired("KEYSTORE_PATH") { "KEYSTORE_PATH is not set" }
    val KEYSTORE_PASSWORD = getRequired("KEYSTORE_PASSWORD") { "KEYSTORE_PASSWORD is not set" }.toCharArray()
    val KEY_ALIAS = env["KEY_ALIAS"] ?: "serverSsl"
}
