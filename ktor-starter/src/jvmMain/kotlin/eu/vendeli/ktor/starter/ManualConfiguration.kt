package eu.vendeli.ktor.starter

class ManualConfiguration : Configuration() {
    override var HOST: String = "0.0.0.0"
    override var PORT: Int = 8080
    override var SSL_PORT: Int = 8443
    override lateinit var PEM_PRIVATE_KEY_PATH: String
    override lateinit var PEM_CHAIN_PATH: String
    override lateinit var PEM_PRIVATE_KEY: CharArray
    override lateinit var KEYSTORE_PATH: String
    override lateinit var KEYSTORE_PASSWORD: CharArray
    override var KEY_ALIAS: String = "serverSsl"
}
