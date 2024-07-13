@file:Suppress("MatchingDeclarationName", "TooManyFunctions")

package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.api.botactions.getUpdates
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.interfaces.Filter
import eu.vendeli.tgbot.interfaces.Guard
import eu.vendeli.tgbot.interfaces.ImplicitMediaData
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ChainLink
import eu.vendeli.tgbot.types.internal.FailedUpdate
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.http.escapeIfNeeded
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import kotlin.jvm.JvmName
import kotlin.reflect.KClass

internal suspend inline fun TgUpdateHandler.coHandle(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    crossinline block: suspend CoroutineScope.() -> Unit,
) = (handlerScope + Job(handlerScope.coroutineContext[Job])).launch(dispatcher) { block() }

internal suspend inline fun TgUpdateHandler.checkIsLimited(
    limits: RateLimits,
    telegramId: Long? = null,
    actionId: String? = null,
): Boolean = bot.config.rateLimiter.run {
    if (telegramId == null || limits.period == 0L && limits.rate == 0L) return false

    logger.debug { "Checking the request for exceeding the limits${if (actionId != null) " for $actionId}" else ""}." }
    if (mechanism.isLimited(limits, telegramId, actionId)) {
        val loggingTail = if (actionId != null) " for $actionId}" else ""
        logger.info { "User #$telegramId has exceeded the request limit$loggingTail." }
        exceededAction.invoke(telegramId, bot)
        return true
    }
    return false
}

@OptIn(InternalSerializationApi::class)
@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type : Any> TgAction<Type>.getReturnType(): KSerializer<Type> = Type::class.serializer()

@Suppress("UnusedReceiverParameter")
@OptIn(InternalSerializationApi::class)
@JvmName("listReturnType")
internal inline fun <reified Type : MultipleResponse> TgAction<List<Type>>.getReturnType(): KSerializer<List<Type>> =
    ListSerializer(Type::class.serializer())

@Suppress("NOTHING_TO_INLINE")
internal inline fun <R> TgAction<R>.handleImplicitFile(input: ImplicitFile, fieldName: String) {
    parameters[fieldName] = input.transform(multipartData).file.toJsonElement()
}

@Suppress("DEPRECATION_ERROR", "NOTHING_TO_INLINE")
internal inline fun <T : ImplicitMediaData, R> MediaAction<R>.handleImplicitFileGroup(
    input: List<T>,
    fieldName: String = "media",
) {
    parameters[fieldName] = buildList {
        input.forEach {
            if (it.media is ImplicitFile.Str && it.thumbnail is ImplicitFile.Str) {
                add(it.encodeWith(DynamicLookupSerializer))
                return@forEach
            }
            it.media = it.media.transform(multipartData)
            it.thumbnail = it.thumbnail?.transform(multipartData)
            add(it.encodeWith(DynamicLookupSerializer))
        }
    }.encodeWith(JsonElement.serializer())
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun ImplicitFile.transform(multiParts: MutableList<PartData.BinaryItem>): ImplicitFile.Str {
    if (this is ImplicitFile.Str) return file.toImplicitFile()
    val media = file as InputFile
    multiParts += media.toPartData(media.fileName)

    return "attach://${media.fileName}".toImplicitFile()
}

internal fun InputFile.toPartData(name: String) = PartData.BinaryItem(
    { ByteReadPacket(data) },
    {},
    Headers.build {
        append(HttpHeaders.ContentDisposition, "form-data; name=${name.escapeIfNeeded()}")
        append(HttpHeaders.ContentDisposition, "filename=$fileName")
        append(HttpHeaders.ContentType, contentType)
        append(HttpHeaders.ContentLength, data.size.toString())
    },
)

object DefaultGuard : Guard {
    override suspend fun condition(user: User?, update: ProcessedUpdate, bot: TelegramBot): Boolean = true
}

object DefaultFilter : Filter {
    override suspend fun match(user: User?, update: ProcessedUpdate, bot: TelegramBot): Boolean = true
}

val DEFAULT_HANDLING_BEHAVIOUR: HandlingBehaviourBlock = { handle(it) }
internal val GET_UPDATES_ACTION = getUpdates()
internal val DEFAULT_COMMAND_SCOPE = setOf(UpdateType.MESSAGE)
internal expect val PROCESSING_DISPATCHER: CoroutineDispatcher

internal suspend inline fun <T> asyncAction(crossinline block: suspend () -> T): Deferred<T> = coroutineScope {
    async { block() }
}

@Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")
internal inline fun <T> Any?.cast() = this as T

/**
 * Set chain for input listening.
 *
 * Basically uses chain full qualified name as a chain id.
 *
 * @param T given ChainLink
 * @param user The user for whom it will be set.
 * @param firstLink The First link that will be processed (it doesn't have to be the first link in the chain, feel free to set up any of).
 */
fun <T : ChainLink> InputListener.setChain(user: User, firstLink: T) = set(user, firstLink::class.fullName)

@InternalApi
@Suppress("ObjectPropertyName", "ktlint:standard:backing-property-naming")
expect val _OperatingActivities: Map<String, List<Any?>>

internal expect val KClass<*>.fullName: String

/**
 * Runs exception handler loop.
 *
 * @param dispatcher Dispatcher used for running handler.
 * @param delay Delay after each handling iteration.
 * @param block Handling action.
 */
suspend fun TgUpdateHandler.runExceptionHandler(
    dispatcher: CoroutineDispatcher? = null,
    delay: Long = 100,
    block: suspend FailedUpdate.() -> Unit,
) = coroutineScope {
    launch(dispatcher ?: PROCESSING_DISPATCHER) {
        for (e in caughtExceptions) {
            block(e)
            delay(delay)
        }
    }
}
