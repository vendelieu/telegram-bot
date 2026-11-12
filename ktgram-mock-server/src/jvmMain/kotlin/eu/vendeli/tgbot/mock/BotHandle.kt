package eu.vendeli.tgbot.mock

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val POLL_INTERVAL_MS = 10L

/**
 * The test-side control surface of one bot: inject what users do, inspect what the bot did, and bend the server
 * (stubs, faults, blocks). Everything here is also reachable over HTTP under `/__mock`.
 */
class BotHandle internal constructor(
    private val core: MockCore,
    val state: BotState,
) {
    val token: String get() = state.token
    val id: Long get() = state.id
    val username: String get() = state.username

    /** Queues a raw Update (the `update_id` is assigned here). Returns null when the bot filtered the type out. */
    fun enqueue(update: JsonObject): JsonObject? {
        registerQueries(update)
        if (!Handlers.isAllowed(state, update)) return null
        return state.updates.enqueue { updateId ->
            JsonObject(
                linkedMapOf<String, JsonElement>("update_id" to JsonPrimitive(updateId)) + update.without("update_id"),
            )
        }
    }

    /** Remembers the ids of one-shot queries so the matching `answer*` call is accepted exactly once. */
    private fun registerQueries(update: JsonObject) {
        fun id(key: String) = (update[key] as? JsonObject)?.str("id")
        id("callback_query")?.let { state.callbackQueries.putIfAbsent(it, false) }
        id("shipping_query")?.let { state.extra.paymentQueries.putIfAbsent(it, false) }
        id("pre_checkout_query")?.let { state.extra.paymentQueries.putIfAbsent(it, false) }
        id("inline_query")?.let { state.extra.inlineQueries.putIfAbsent(it, false) }
        id("widget_query")?.let { state.extra.widgetQueries.putIfAbsent(it, false) }
        id("shared_widget_query")?.let { state.extra.widgetQueries.putIfAbsent(it, false) }
        (update["chat_join_request"] as? JsonObject)
            ?.str(
                "query_id",
            )?.let { state.extra.joinQueries.putIfAbsent(it, false) }
        listOf("guest_message", "message").forEach { key ->
            (update[key] as? JsonObject)?.str("guest_query_id")?.let { state.extra.guestQueries.putIfAbsent(it, false) }
        }
    }

    /** A Mini App asks the bot to answer a query, see `answerWebAppQuery`. Returns the query id. */
    fun webAppQuery(): String = "mock-webapp-${state.extra.next()}".also { state.extra.webAppQueries[it] = false }

    /** A widget on the user's side asks the bot for data, see `answerWidgetQuery`. */
    fun widgetQuery(userId: Long, data: String = ""): JsonObject {
        val user = state.users[userId] ?: addUser(userId)
        val query = buildJsonObject {
            put("id", "mock-widget-${state.extra.next()}")
            put("from", user)
            put("chat_instance", "mock-chat-instance")
            put("type", core.generator.generateObject("WidgetQueryTypeData")!!)
            put("data", data)
        }
        return enqueue(buildJsonObject { put("widget_query", query) }) ?: query
    }

    /** A user suggests a post to a channel, resolved with `approveSuggestedPost` / `declineSuggestedPost`. */
    fun suggestPost(chatId: Long, userId: Long, text: String): JsonObject {
        val user = state.users[userId] ?: addUser(userId)
        val chat = state.chats.computeIfAbsent(chatId) { ChatState(it, "channel", "Mock channel") }
        val message = Messages.store(
            chat,
            buildJsonObject {
                put("from", user)
                put("chat", chat.toJson())
                put("text", text)
                put("suggested_post_info", buildJsonObject { put("state", "pending") })
            },
            core.config.clock.millis() / MILLIS,
        )
        return enqueue(buildJsonObject { put("message", message) }) ?: message
    }

    /** The user boosts the chat, visible through `getUserChatBoosts`. */
    fun addBoost(chatId: Long, userId: Long) {
        val user = state.users[userId] ?: addUser(userId)
        state.extra.boosts.computeIfAbsent("$chatId:$userId") { java.util.concurrent.CopyOnWriteArrayList() } +=
            SocialHandlers.boostJson(core, user, state.extra)
    }

    fun setProfilePhotos(userId: Long, count: Int) {
        state.extra.profilePhotos[userId] = count
    }

    fun setProfileAudios(userId: Long, count: Int) {
        state.extra.profileAudios[userId] = count
    }

    fun setPersonalChatMessages(userId: Long, messages: List<JsonObject>) {
        state.extra.personalChatMessages[userId] = messages
    }

    /** A gift the business account received, manageable with convert/upgrade/transfer/delete. */
    fun addBusinessGift(connectionId: String, giftId: String = "mock_gift_1", starCount: Long = 25L): String {
        val ownedId = "og_${state.extra.next()}"
        val gift = core.generator.generateObject(
            "Gift",
            overrides = mapOf(
                "id" to JsonPrimitive(giftId),
                "star_count" to JsonPrimitive(starCount),
            ),
        )!!
        state.extra.businessGifts.computeIfAbsent(connectionId) { java.util.concurrent.CopyOnWriteArrayList() } +=
            core.generator.generateObject(
                "OwnedGiftRegular",
                overrides = mapOf(
                    "type" to JsonPrimitive("regular"),
                    "gift" to gift,
                    "owned_gift_id" to JsonPrimitive(ownedId),
                    "send_date" to JsonPrimitive(core.config.clock.millis() / MILLIS),
                    "convert_star_count" to JsonPrimitive(starCount),
                ),
            )!!
        return ownedId
    }

    fun businessGifts(connectionId: String): List<JsonObject> = state.extra.businessGifts[connectionId]
        .orEmpty()
        .toList()

    fun drafts(chatId: Long): List<JsonObject> = state.chats[chatId]
        ?.drafts
        ?.values
        ?.toList()
        .orEmpty()

    fun ephemeralMessages(chatId: Long): List<JsonObject> = state.chats[chatId]
        ?.ephemerals
        ?.values
        ?.toList()
        .orEmpty()

    fun passportErrors(userId: Long): List<JsonObject> = state.extra.passportErrors[userId].orEmpty()

    fun widgetData(key: String): JsonObject? = state.extra.widgetData[key]

    fun isVerified(userId: Long) = state.extra.verifiedUsers.containsKey(userId)

    fun isChatVerified(chatId: Long) = state.extra.verifiedChats.containsKey(chatId)

    fun addUser(id: Long, firstName: String = "Test", username: String? = null, lastName: String? = null): JsonObject =
        ChatHandlers.userJson(id, firstName, username, lastName).also(state::registerUser)

    fun addChat(
        id: Long,
        type: String = "supergroup",
        title: String? = "Mock chat",
        username: String? = null,
        isForum: Boolean = false,
    ): ChatState =
        state.chats.computeIfAbsent(id) { ChatState(it, type, title, username, isForum = isForum) }

    /** Registers a business connection, after which `business_connection_id` is accepted for it. */
    fun addBusinessConnection(
        id: String,
        userId: Long,
        stars: Long = 0L,
        rights: Set<String>? = null,
    ): JsonObject {
        val user = state.users[userId] ?: addUser(userId)
        val connection = core.generator.generateObject(
            "BusinessConnection",
            overrides = mapOf(
                "id" to JsonPrimitive(id),
                "user" to user,
                "user_chat_id" to JsonPrimitive(userId),
                "date" to JsonPrimitive(core.config.clock.millis() / MILLIS),
                "is_enabled" to JsonPrimitive(true),
            ) + (rights?.let { granted -> mapOf("rights" to rightsObject(granted)) } ?: emptyMap()),
        )!!
        state.extra.businessConnections[id] = connection
        state.extra.businessStars[id] = stars
        return connection
    }

    private fun rightsObject(granted: Set<String>): JsonObject = JsonObject(
        (
            core.spec.types["BusinessBotRights"]
                ?.fields
                .orEmpty()
                .map { it.name } + granted
        ).distinct()
            .associateWith { JsonPrimitive(it in granted) },
    )

    /** What `setBusinessAccount*` stored for the connection: names, username, bio, photo flags, gift settings. */
    fun businessAccount(connectionId: String): JsonObject =
        state.extra.businessAccounts[connectionId] ?: JsonObject(emptyMap())

    fun businessConnection(connectionId: String): JsonObject? = state.extra.businessConnections[connectionId]

    fun isRead(chatId: Long, messageId: Long) = state.chats[chatId]?.readMessages?.contains(messageId) == true

    fun stickerKeywords(fileId: String): List<String> = state.extra.stickerKeywords[fileId].orEmpty()

    fun hasProfilePhoto() = state.extra.profilePhotos.containsKey(state.id)

    /** Replaces what `getChatShareThemeBackdrops` returns (two generated backdrops otherwise). */
    fun setShareThemeBackdrops(backdrops: List<JsonObject>) {
        state.extra.shareBackdrops.clear()
        state.extra.shareBackdrops += backdrops
    }

    /** Adds a gift to the catalogue `getAvailableGifts` returns (it holds one default gift otherwise). */
    fun addGift(id: String, starCount: Long) {
        state.extra.gifts += core.generator.generateObject(
            "Gift",
            overrides = mapOf("id" to JsonPrimitive(id), "star_count" to JsonPrimitive(starCount)),
        )!!
    }

    /** Lets the bot act as a verification organisation (`verifyUser`, `verifyChat`, ...), refused otherwise. */
    fun allowVerification(isAllowed: Boolean = true) {
        state.extra.canVerify = isAllowed
    }

    /** The user granted the bot the right to change their emoji status. */
    fun allowEmojiStatus(userId: Long) {
        state.extra.emojiStatusGrants += userId
    }

    /** An inline message the bot may edit (`inline_message_id` of a chosen inline result). */
    fun addInlineMessage(id: String) {
        state.extra.inlineMessages += id
    }

    /** A game registered through BotFather, `sendGame` only accepts registered short names. */
    fun addGame(shortName: String, title: String, description: String) {
        state.extra.games[shortName] = JsonObject(
            mapOf("title" to JsonPrimitive(title), "description" to JsonPrimitive(description)),
        )
    }

    /** A sticker set owned by the bot (`getStickerSet`, `addStickerToSet`, ...) with [stickerCount] stickers. */
    fun addStickerSet(name: String, title: String, type: String = "regular", stickerCount: Int = 1) {
        val stickers = (1..stickerCount).map { n ->
            core.generator.generateObject(
                "Sticker",
                overrides = mapOf(
                    "file_id" to JsonPrimitive("mock_set_${name}_$n"),
                    "file_unique_id" to JsonPrimitive("mock_set_unique_${name}_$n"),
                    "type" to JsonPrimitive(type),
                    "set_name" to JsonPrimitive(name),
                ),
            )!!
        }
        state.extra.stickerSets[name] = JsonObject(
            mapOf(
                "name" to JsonPrimitive(name),
                "title" to JsonPrimitive(title),
                "sticker_type" to JsonPrimitive(type),
                "stickers" to JsonArray(stickers),
            ),
        )
    }

    /** A file the bot already knows by [fileId] (as if Telegram had it), so it can be sent or used by id. */
    fun addFile(fileId: String, name: String? = null, mimeType: String? = null, bytes: ByteArray? = null) {
        val n = state.fileCounter.incrementAndGet()
        state.files[fileId] = StoredFile(fileId, "mock_unique_$n", "known/file_$n", name, mimeType, bytes)
    }

    /** The name `getMyName` answers when no language-specific one is set. */
    fun setBotName(name: String) {
        state.extra.settings["name|"] = JsonPrimitive(name)
    }

    fun setStarBalance(amount: Long) = state.extra.starBalance.set(amount)

    fun starBalance(): Long = state.extra.starBalance.get()

    /** A user asks to join a chat that needs approval (`approveChatJoinRequest` / `declineChatJoinRequest`). */
    fun requestToJoin(chatId: Long, userId: Long, inviteLink: String? = null): JsonObject {
        val chat = state.chats[chatId] ?: error("Unknown chat $chatId, add it with addChat first")
        val user = state.users[userId] ?: addUser(userId)
        chat.joinRequests += userId
        val request = buildJsonObject {
            put("chat", chat.toJson())
            put("from", user)
            put("user_chat_id", userId)
            put("date", core.config.clock.millis() / MILLIS)
            put("query_id", "mock-join-${state.extra.next()}")
            inviteLink?.let { put("invite_link", chat.inviteLinks[it] ?: buildJsonObject { put("invite_link", it) }) }
        }
        return enqueue(buildJsonObject { put("chat_join_request", request) }) ?: request
    }

    /** A user picks a shipping address for a flexible invoice, answer with `answerShippingQuery`. */
    fun shippingQuery(userId: Long, payload: String): JsonObject {
        val user = state.users[userId] ?: addUser(userId)
        val id = "mock-ship-${state.extra.next()}"
        state.extra.paymentQueries[id] = false
        val query = buildJsonObject {
            put("id", id)
            put("from", user)
            put("invoice_payload", payload)
            put("shipping_address", core.generator.generateObject("ShippingAddress")!!)
        }
        return enqueue(buildJsonObject { put("shipping_query", query) }) ?: query
    }

    /** A user is about to pay, answer with `answerPreCheckoutQuery`. */
    fun preCheckoutQuery(userId: Long, payload: String, currency: String, total: Long): JsonObject {
        val user = state.users[userId] ?: addUser(userId)
        val id = "mock-pre-${state.extra.next()}"
        state.extra.paymentQueries[id] = false
        val query = buildJsonObject {
            put("id", id)
            put("from", user)
            put("currency", currency)
            put("total_amount", total)
            put("invoice_payload", payload)
        }
        return enqueue(buildJsonObject { put("pre_checkout_query", query) }) ?: query
    }

    /** The payment went through: a `successful_payment` message arrives and Stars payments fill the balance. */
    fun pay(chatId: Long, userId: Long, payload: String, currency: String, total: Long): JsonObject {
        val user = state.users[userId] ?: addUser(userId)
        val chat = state.chats.computeIfAbsent(chatId) { ChatState(it, "private", firstName = "Test") }
        val charge = "mock-charge-${state.extra.next()}"
        val payment = buildJsonObject {
            put("currency", currency)
            put("total_amount", total)
            put("invoice_payload", payload)
            put("telegram_payment_charge_id", charge)
            put("provider_payment_charge_id", "mock-provider-$charge")
        }
        if (currency == "XTR") {
            state.extra.starBalance.addAndGet(total)
            state.extra.ledger += buildJsonObject {
                put("id", charge)
                put("amount", total)
                put("date", core.config.clock.millis() / MILLIS)
                put("user_id", userId)
            }
        }
        val message = Messages.store(
            chat,
            buildJsonObject {
                put("from", user)
                put("chat", chat.toJson())
                put("successful_payment", payment)
            },
            core.config.clock.millis() / MILLIS,
        )
        return enqueue(buildJsonObject { put("message", message) }) ?: message
    }

    fun reactionsOf(chatId: Long, messageId: Long): JsonElement? = state.chats[chatId]?.reactions?.get(messageId)

    fun setMemberStatus(chatId: Long, userId: Long, status: String) {
        val chat = state.chats[chatId] ?: error("Unknown chat $chatId, add it with addChat first")
        chat.members[userId] = status
    }

    /** A user sends [text]. In a private chat leave [chatId] empty, for groups pass the group id. */
    fun userSays(
        text: String,
        fromId: Long = DEFAULT_USER_ID,
        chatId: Long = fromId,
        firstName: String = "Test",
        username: String? = null,
        chatType: String = "private",
        chatTitle: String? = null,
        replyToMessageId: Long? = null,
        businessConnectionId: String? = null,
    ): JsonObject {
        val user = state.users[fromId] ?: addUser(fromId, firstName, username)
        val chat = state.chats.computeIfAbsent(chatId) {
            if (chatType ==
                "private"
            ) ChatState(it, chatType, firstName = firstName, username = username) else ChatState(
                it,
                chatType,
                chatTitle ?: "Mock chat",
            )
        }
        if (!chat.isPrivate) chat.members.putIfAbsent(fromId, "member")
        state.blockedBy.remove(fromId)
        val formatted = TextFormatter.format(text, null, null)
        val fields = LinkedHashMap<String, JsonElement>()
        fields["from"] = user
        fields["chat"] = chat.toJson()
        fields["text"] = JsonPrimitive(formatted.text)
        if (formatted.entities.isNotEmpty()) fields["entities"] = JsonArray(formatted.entities)
        replyToMessageId?.let { id ->
            chat.messages[id]?.let { fields["reply_to_message"] = it.without("reply_to_message") }
        }
        businessConnectionId?.let { fields["business_connection_id"] = JsonPrimitive(it) }
        val message = Messages.store(chat, JsonObject(fields), core.config.clock.millis() / MILLIS)
        val key = if (businessConnectionId != null) "business_message" else "message"
        return enqueue(buildJsonObject { put(key, message) }) ?: message
    }

    /** A user presses an inline button of a message the bot sent. */
    fun pressButton(chatId: Long, messageId: Long, data: String, fromId: Long = chatId): JsonObject {
        val chat = state.chats[chatId] ?: error("Unknown chat $chatId")
        val message = chat.messages[messageId] ?: error("Unknown message $messageId in chat $chatId")
        val user = state.users[fromId] ?: addUser(fromId)
        val queryId = state.nextCallbackId()
        state.callbackQueries[queryId] = false
        val query = buildJsonObject {
            put("id", queryId)
            put("from", user)
            put("message", message)
            put("chat_instance", "mock-chat-instance")
            put("data", data)
        }
        return enqueue(buildJsonObject { put("callback_query", query) }) ?: query
    }

    fun inlineQuery(query: String, fromId: Long = DEFAULT_USER_ID, offset: String = ""): JsonObject {
        val user = state.users[fromId] ?: addUser(fromId)
        val inline = buildJsonObject {
            put("id", "mock-inline-${state.nextCallbackId()}")
            put("from", user)
            put("query", query)
            put("offset", offset)
        }
        return enqueue(buildJsonObject { put("inline_query", inline) }) ?: inline
    }

    fun calls(method: String? = null): List<RecordedCall> =
        state.calls.filter { method == null || it.method.equals(method, ignoreCase = true) }

    /** Suspends until the bot has made a call to [method] (the [nth] one), or fails after [timeout]. */
    suspend fun awaitCall(method: String, timeout: Duration = DEFAULT_TIMEOUT, nth: Int = 1): RecordedCall =
        withTimeout(timeout) {
            var found = calls(method).getOrNull(nth - 1)
            while (found == null) {
                delay(POLL_INTERVAL_MS)
                found = calls(method).getOrNull(nth - 1)
            }
            found
        }

    fun messages(chatId: Long): List<JsonObject> = state.chats[chatId]
        ?.messages
        ?.values
        ?.toList()
        .orEmpty()

    fun lastMessage(chatId: Long): JsonObject? = state.chats[chatId]
        ?.messages
        ?.lastEntry()
        ?.value

    fun fileBytes(fileId: String): ByteArray? = state.files[fileId]?.bytes

    /** Answers the next [times] matching calls with a successful [result]. */
    fun stub(
        method: String,
        result: JsonElement,
        match: JsonObject = JsonObject(emptyMap()),
        times: Int = Int.MAX_VALUE,
    ) {
        state.stubs += Stub(method, match, ApiResult.Ok(result), times)
    }

    /** Answers the next [times] matching calls with the given Bot API error. */
    fun stubError(
        method: String,
        error: ApiException,
        match: JsonObject = JsonObject(emptyMap()),
        times: Int = Int.MAX_VALUE,
    ) {
        state.stubs += Stub(method, match, ApiResult.Error(error), times)
    }

    /** Fails the next [times] calls of [method] (all methods when null) before anything else runs. */
    fun fail(method: String?, error: ApiException, times: Int = 1, delay: Duration = Duration.ZERO) {
        state.faults += Fault(method, error, times, delay)
    }

    fun rateLimit(method: String? = null, retryAfter: Int = 1, times: Int = 1) =
        fail(method, ApiException.tooManyRequests(retryAfter), times)

    fun blockedBy(userId: Long) {
        state.blockedBy += userId
    }

    fun unblockedBy(userId: Long) {
        state.blockedBy -= userId
    }

    fun reset() = state.reset()

    companion object {
        const val DEFAULT_USER_ID = 1000L
        private const val MILLIS = 1000L
        private val DEFAULT_TIMEOUT = 5.seconds
    }
}
