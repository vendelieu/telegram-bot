package eu.vendeli.ktor.starter

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import nl.altindag.ssl.pem.util.PemUtils
import java.io.File
import java.security.KeyStore
import java.security.PrivateKey


fun serveWebhook(wait: Boolean = true, serverBuilder: ServerBuilder.() -> Unit = {}): NettyApplicationEngine {
    val cfg = ServerBuilder().apply(serverBuilder)
    val serverCfg = cfg.server ?: EnvConfiguration

    val environment = applicationEngineEnvironment {
        connector {
            host = serverCfg.HOST
            port = serverCfg.PORT
        }
        if (serverCfg.SSL_ON) {
            val keystoreFile = File(serverCfg.KEYSTORE_PATH)
            val keystore = KeyStore.getInstance("JKS")

            if (keystoreFile.exists()) {
                keystore.load(keystoreFile.inputStream(), serverCfg.KEYSTORE_PASSWORD)
            } else {
                keystore.load(null, null)
                val pemPrivateKeyFile = File(serverCfg.PEM_PRIVATE_KEY_PATH).also {
                    if (!it.exists()) throw IllegalStateException("PEM_PRIVATE_KEY file not found")
                }
                val chainFile = File(serverCfg.PEM_CHAIN_PATH).also {
                    if (!it.exists()) throw IllegalStateException("PEM_CHAIN_PATH file not found")
                }

                val pemPrivateKey: PrivateKey = PemUtils.loadPrivateKey(pemPrivateKeyFile.inputStream())
                val pemChain = PemUtils.loadCertificate(chainFile.inputStream()).toTypedArray()

                keystore.setKeyEntry(serverCfg.KEY_ALIAS, pemPrivateKey, serverCfg.PEM_PRIVATE_KEY, pemChain)
                keystore.store(keystoreFile.outputStream(), serverCfg.KEYSTORE_PASSWORD)
            }

            sslConnector(
                keyStore = keystore,
                keyAlias = serverCfg.KEY_ALIAS,
                keyStorePassword = { serverCfg.KEYSTORE_PASSWORD },
                privateKeyPassword = { serverCfg.PEM_PRIVATE_KEY },
            ) {
                host = serverCfg.HOST
                port = serverCfg.SSL_PORT
                keyStorePath = keystoreFile
            }
        }

        modules.addAll(cfg.ktorModules)
        module {
            routing {
                cfg.botInstances.forEach { (token, bot) ->
                    post("/$token") {
                        bot.update.parseAndHandle(call.receiveText())
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }
    }

    return embeddedServer(Netty, environment, cfg.engineCfg).start(wait)
}

