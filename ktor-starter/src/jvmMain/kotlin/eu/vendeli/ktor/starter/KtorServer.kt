package eu.vendeli.ktor.starter

import eu.vendeli.tgbot.TelegramBot
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
    val serverConfiguration = ServerBuilder().apply(serverBuilder)
    val bot = TelegramBot(Configuration.TOKEN, Configuration.PACKAGE, serverConfiguration.botCfg)
    bot.update.setBehaviour(serverConfiguration.handlingBehav)

    val keystoreFile = File(Configuration.KEYSTORE_PATH)

    val keystore = KeyStore.getInstance("JKS")
    if (keystoreFile.exists()) {
        keystore.load(keystoreFile.inputStream(), Configuration.KEYSTORE_PASSWORD)
    } else {
        val pemPrivateKeyFile = File(Configuration.PEM_PRIVATE_KEY_PATH).also {
            if (!it.exists()) throw IllegalStateException("PEM private key not found")
        }
        val chainFile = File(Configuration.PEM_CHAIN_PATH).also {
            if (!it.exists()) throw IllegalStateException("Chain certificate not found")
        }

        val pemPrivateKey: PrivateKey = PemUtils.loadPrivateKey(pemPrivateKeyFile.inputStream())
        val pemChain = PemUtils.loadCertificate(chainFile.inputStream()).toTypedArray()

        keystore.setKeyEntry(Configuration.KEY_ALIAS, pemPrivateKey, Configuration.PEM_PRIVATE_KEY, pemChain)
        keystore.store(keystoreFile.outputStream(), Configuration.KEYSTORE_PASSWORD)
    }

    val environment = applicationEngineEnvironment {
        connector {
            host = Configuration.HOST
            port = Configuration.PORT
        }
        sslConnector(
            keyStore = keystore,
            keyAlias = Configuration.KEY_ALIAS,
            keyStorePassword = { Configuration.KEYSTORE_PASSWORD },
            privateKeyPassword = { Configuration.PEM_PRIVATE_KEY },
        ) {
            host = Configuration.HOST
            port = Configuration.SSL_PORT
            keyStorePath = keystoreFile
        }
        module {
            routing {
                post(Configuration.WEBHOOK_URL) {
                    bot.update.parseAndHandle(call.receiveText())
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }

    return embeddedServer(Netty, environment, serverConfiguration.serverCfg).start(wait)
}

