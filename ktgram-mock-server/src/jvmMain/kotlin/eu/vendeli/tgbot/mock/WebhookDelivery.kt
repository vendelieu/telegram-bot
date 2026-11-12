package eu.vendeli.tgbot.mock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

private const val SECRET_HEADER = "X-Telegram-Bot-Api-Secret-Token"
private const val REQUEST_TIMEOUT_SECONDS = 10L
private const val HTTP_SUCCESS_MAX = 299

/** Pushes queued updates to the registered webhook, retrying failed ones like Telegram does. */
class WebhookDelivery(
    private val core: MockCore,
) {
    private val client = HttpClient.newHttpClient()

    fun start(bot: BotState) {
        bot.webhookJob?.cancel()
        bot.webhookJob = core.scope.launch { run(bot) }
    }

    fun stop(bot: BotState) {
        bot.webhookJob?.cancel()
        bot.webhookJob = null
        bot.webhook = null
    }

    private suspend fun run(bot: BotState) {
        while (true) {
            currentCoroutineContext().ensureActive()
            val seen = bot.updates.currentVersion()
            val webhook = bot.webhook ?: return
            val pending = bot.updates.snapshot()
            if (pending.isEmpty()) {
                bot.updates.awaitChange(seen)
                continue
            }
            for (update in pending) {
                val error = post(webhook, update)
                if (error == null) {
                    bot.updates.remove(update.long("update_id")!!)
                } else {
                    webhook.lastErrorDate = core.config.clock.millis() / MILLIS
                    webhook.lastErrorMessage = error
                    delay(core.config.webhookRetryDelay)
                    break
                }
            }
        }
    }

    /** Returns null on success, otherwise the text Telegram would report in `last_error_message`. */
    private suspend fun post(webhook: WebhookState, update: JsonObject): String? = withContext(Dispatchers.IO) {
        try {
            val request = HttpRequest
                .newBuilder(URI.create(webhook.url))
                .timeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
                .header("Content-Type", "application/json")
                .apply { webhook.secretToken?.let { header(SECRET_HEADER, it) } }
                .POST(HttpRequest.BodyPublishers.ofString(update.toString()))
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.discarding())
            if (response.statusCode() <=
                HTTP_SUCCESS_MAX
            ) null else "Wrong response from the webhook: ${response.statusCode()}"
        } catch (e: java.io.IOException) {
            "Connection error: ${e.message}"
        } catch (e: IllegalArgumentException) {
            "Bad webhook URL: ${e.message}"
        }
    }

    private companion object {
        const val MILLIS = 1000L
    }
}
