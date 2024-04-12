package eu.vendeli.ktor.starter

import java.net.InetAddress

abstract class Configuration {
    abstract val TOKEN: String
    abstract val PACKAGE: String?
    abstract val HOST: String
    abstract val PORT: Int
    abstract val SSL_PORT: Int
    abstract val WEBHOOK_URL: String
    abstract val PEM_PRIVATE_KEY_PATH: String
    abstract val PEM_CHAIN_PATH: String
    abstract val PEM_PRIVATE_KEY: CharArray
    abstract val KEYSTORE_PATH: String
    abstract val KEYSTORE_PASSWORD: CharArray
    abstract val KEY_ALIAS: String

    open var WEBHOOK_PREFIX = InetAddress.getLocalHost().hostAddress
}
