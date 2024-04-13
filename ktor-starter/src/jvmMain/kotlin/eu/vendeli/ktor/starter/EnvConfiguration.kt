package eu.vendeli.ktor.starter

import io.ktor.server.config.ApplicationConfigurationException

internal object EnvConfiguration : Configuration() {
    private val PREFIX = "KTGRAM_"
    private val env = System.getenv().filter { it.key.startsWith(PREFIX) }
    private fun getEnv(name: String): String? = env[PREFIX + name]
    private fun getRequired(name: String, missingExCause: () -> String): String =
        env[name] ?: missingExCause().also { throw ApplicationConfigurationException(it) }

    override val HOST = getEnv("HOST") ?: "0.0.0.0"
    override val PORT = getEnv("PORT")?.toInt() ?: 8080
    override val SSL_PORT = getEnv("SSL_PORT")?.toInt() ?: 8443
    override val PEM_PRIVATE_KEY_PATH = getRequired("PEM_PRIVATE_KEY_PATH") { "PEM_PRIVATE_KEY_PATH is not set" }
    override val PEM_CHAIN_PATH = getRequired("CHAIN_PATH") { "PEM_CHAIN_PATH is not set" }
    override val PEM_PRIVATE_KEY = getRequired("PRIVATE_KEY") { "PEM_PRIVATE_key is not set" }.toCharArray()
    override val KEYSTORE_PATH = getRequired("KEYSTORE_PATH") { "KEYSTORE_PATH is not set" }
    override val KEYSTORE_PASSWORD = getRequired("KEYSTORE_PASSWORD") { "KEYSTORE_PASSWORD is not set" }.toCharArray()
    override val KEY_ALIAS = getEnv("KEY_ALIAS") ?: "serverSsl"
}
