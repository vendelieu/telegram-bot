package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val MAX_RANK = 16
private const val MAX_PAGE = 100
private const val MAX_DRAFT = 4096
private const val SECONDS_PER_DAY = 86_400L
private val PREMIUM_MONTHS = setOf(3L, 6L, 12L)
private const val QUERY_INVALID = "query is too old and response timeout expired or query ID is invalid"

/** Member decorations, emoji status, boosts, profile media, drafts, ephemeral messages, widgets and one-shot queries. */
internal object SocialHandlers {
    private fun CallContext.userId() =
        params.long("user_id") ?: throw ApiException.badRequest("invalid user_id specified")

    private fun CallContext.group(): ChatState {
        val chat = chat()
        if (chat.isPrivate) throw ApiException.badRequest("method is not available in private chats")
        return chat
    }

    // --- members and chats ----------------------------------------------------------------------------------

    val setCustomTitle: Handler = {
        val chat = group()
        val id = userId()
        if (chat.type == "channel") throw ApiException.badRequest("method is available only in groups and supergroups")
        val status = ChatHandlers.statusOf(this, chat, id)
        if (status == "creator") throw ApiException.badRequest("only the owner can edit their custom title")
        if (status != "administrator") throw ApiException.badRequest("user is not an administrator")
        val title = params.str("custom_title").orEmpty()
        if (title.length > MAX_RANK) throw ApiException.badRequest("ADMIN_RANK_TOO_LONG")
        if (title.isEmpty()) chat.customTitles.remove(id) else chat.customTitles[id] = title
        TRUE
    }

    val setMemberTag: Handler = {
        val chat = group()
        val id = userId()
        val status = ChatHandlers.statusOf(this, chat, id)
        if (status == "left" || status == "kicked") throw ApiException.badRequest("member status can't have a tag")
        val tag = params.str("tag").orEmpty()
        if (tag.length > MAX_RANK) throw ApiException.badRequest("TAG_TOO_LONG")
        if (tag.isEmpty()) chat.tags.remove(id) else chat.tags[id] = tag
        TRUE
    }

    private fun senderChatBan(isBan: Boolean): Handler = {
        val chat = group()
        val sender = params.long("sender_chat_id") ?: throw ApiException.badRequest("sender_chat_id is empty")
        if (sender == chat.id) throw ApiException.badRequest("can't ban the chat itself")
        if (isBan && !bot.chats.containsKey(sender) && !bot.users.containsKey(sender)) {
            throw ApiException.badRequest("PARTICIPANT_ID_INVALID")
        }
        if (isBan) chat.bannedSenderChats += sender else chat.bannedSenderChats -= sender
        TRUE
    }

    val banSenderChat = senderChatBan(true)
    val unbanSenderChat = senderChatBan(false)

    val setEmojiStatus: Handler = {
        val id = userId()
        if (id !in bot.extra.emojiStatusGrants) {
            throw ApiException.badRequest("not enough rights to change the user's emoji status")
        }
        val emoji = params.str("emoji_status_custom_emoji_id")
        if (emoji == null) {
            bot.extra.emojiStatus.remove(id)
        } else {
            bot.extra.emojiStatus[id] = JsonObject(
                buildMap {
                    put("emoji_status_custom_emoji_id", JsonPrimitive(emoji))
                    params["emoji_status_expiration_date"]?.let { put("emoji_status_expiration_date", it) }
                },
            )
        }
        TRUE
    }

    private fun verification(isUser: Boolean, isAdd: Boolean): Handler = {
        if (!bot.extra.canVerify) throw ApiException.badRequest("BOT_VERIFIER_FORBIDDEN")
        val key = (if (isUser) params.long("user_id") else params.long("chat_id"))
            ?: throw ApiException.badRequest("id is empty")
        val store = if (isUser) bot.extra.verifiedUsers else bot.extra.verifiedChats
        if (isAdd) store[key] = params.str("custom_description").orEmpty() else store.remove(key)
        TRUE
    }

    val verifyUser = verification(isUser = true, isAdd = true)
    val verifyChat = verification(isUser = false, isAdd = true)
    val removeUserVerification = verification(isUser = true, isAdd = false)
    val removeChatVerification = verification(isUser = false, isAdd = false)

    // --- boosts, profile media, personal chat ---------------------------------------------------------------------

    val userBoosts: Handler = {
        val chat = group()
        val list = bot.extra.boosts["${chat.id}:${userId()}"].orEmpty()
        buildJsonObject { put("boosts", JsonArray(list.toList())) }
    }

    private fun CallContext.photoSizes(userId: Long, index: Int): JsonArray = JsonArray(
        listOf(
            generator.generateObject(
                "PhotoSize",
                overrides = mapOf(
                    "file_id" to JsonPrimitive("mock_profile_${userId}_$index"),
                    "file_unique_id" to JsonPrimitive("mock_profile_unique_${userId}_$index"),
                ),
            )!!,
        ),
    )

    private fun CallContext.page(total: Int): IntRange {
        val offset = (params.long("offset") ?: 0L).toInt().coerceAtLeast(0)
        val limit = (params.long("limit") ?: MAX_PAGE.toLong()).toInt().coerceIn(1, MAX_PAGE)
        return offset until minOf(total, offset + limit)
    }

    val profilePhotos: Handler = {
        val id = userId()
        val total = bot.extra.profilePhotos[id] ?: 0
        buildJsonObject {
            put("total_count", total)
            put("photos", JsonArray(page(total).map { photoSizes(id, it) }))
        }
    }

    val profileAudios: Handler = {
        val id = userId()
        val total = bot.extra.profileAudios[id] ?: 0
        buildJsonObject {
            put("total_count", total)
            put(
                "audios",
                JsonArray(
                    page(total).map {
                        generator.generateObject(
                            "Audio",
                            overrides = mapOf(
                                "file_id" to JsonPrimitive("mock_profile_audio_${id}_$it"),
                                "file_unique_id" to JsonPrimitive("mock_profile_audio_unique_${id}_$it"),
                            ),
                        )!!
                    },
                ),
            )
        }
    }

    val personalChatMessages: Handler = {
        val id = userId()
        val limit = (params.long("limit") ?: MAX_PAGE.toLong()).toInt().coerceIn(1, MAX_PAGE)
        JsonArray(
            bot.extra.personalChatMessages[id]
                .orEmpty()
                .take(limit),
        )
    }

    val setPassportErrors: Handler = {
        val id = userId()
        val errors = (params["errors"] as? JsonArray).orEmpty().map {
            it as? JsonObject
                ?: throw ApiException.badRequest("wrong passport error")
        }
        if (errors.any { it.str("source").isNullOrEmpty() || it.str("type").isNullOrEmpty() }) {
            throw ApiException.badRequest("wrong passport error: source and type are required")
        }
        bot.extra.passportErrors[id] = errors
        TRUE
    }

    // --- subscriptions --------------------------------------------------------------------------------------

    val giftPremium: Handler = {
        val months = params.long("month_count")
        if (months !in PREMIUM_MONTHS) throw ApiException.badRequest("invalid month_count")
        val price = params.long("star_count") ?: 0L
        if (price <= 0) throw ApiException.badRequest("invalid star_count")
        val balance = bot.extra.starBalance
        if (balance.addAndGet(-price) < 0) {
            balance.addAndGet(price)
            throw ApiException.badRequest("BALANCE_TOO_LOW")
        }
        bot.extra.premiumGifts += buildJsonObject {
            put("user_id", userId())
            put("month_count", months!!)
            put("star_count", price)
        }
        TRUE
    }

    val editSubscription: Handler = {
        val charge = params.str("telegram_payment_charge_id").orEmpty()
        val id = userId()
        bot.extra.ledger.firstOrNull { it.str("id") == charge && it.long("user_id") == id }
            ?: throw ApiException.badRequest("CHARGE_ID_INVALID")
        val canceled = params.bool("is_canceled") ?: throw ApiException.badRequest("is_canceled is empty")
        val previous = bot.extra.subscriptionCanceled.put(charge, canceled)
        if (previous == canceled ||
            (previous == null && !canceled)
        ) throw ApiException.badRequest("SUBSCRIPTION_NOT_MODIFIED")
        TRUE
    }

    // --- drafts ------------------------------------------------------------------------------------------------

    private fun draft(isRich: Boolean): Handler = {
        val chat = chat()
        bot.requireDeliverable(chat)
        val id = params.long("draft_id")?.takeIf { it != 0L } ?: throw ApiException.badRequest("DRAFT_ID_INVALID")
        val stored = if (isRich) {
            buildJsonObject {
                put("draft_id", id)
                put("rich_message", params.getValue("rich_message"))
            }
        } else {
            val text = Messages.formatText(
                core.config.textLength,
                params,
                "text",
                "entities",
                MAX_DRAFT,
                null,
                "message is too long",
            )
            buildJsonObject {
                put("draft_id", id)
                put("text", text.text)
                if (text.entities.isNotEmpty()) put("entities", JsonArray(text.entities))
            }
        }
        if (!isRich && stored.str("text").isNullOrEmpty()) chat.drafts.remove(id) else chat.drafts[id] = stored
        TRUE
    }

    val messageDraft = draft(isRich = false)
    val richDraft = draft(isRich = true)

    // --- ephemeral messages -------------------------------------------------------------------------------------

    /** Stores an ephemeral message (sent with `receiver_user_id`) beside the chat's regular ones. */
    fun sendEphemeral(ctx: CallContext, chat: ChatState, message: JsonObject, receiver: Long): JsonObject {
        val id = chat.ephemeralCounter.incrementAndGet()
        val user = ctx.bot.users[receiver] ?: buildJsonObject {
            put("id", receiver)
            put("is_bot", false)
            put("first_name", "Mock")
        }
        val stored = message
            .with("message_id" to JsonPrimitive(chat.nextMessageId()), "date" to JsonPrimitive(ctx.nowSeconds))
            .with("receiver_user" to user, "ephemeral_message_id" to JsonPrimitive(id))
        chat.ephemerals["$receiver:$id"] = stored
        return stored
    }

    private fun CallContext.ephemeral(): Pair<ChatState, JsonObject> {
        val chat = chat()
        val key = "${params.long("receiver_user_id")}:${params.long("ephemeral_message_id")}"
        return chat to (chat.ephemerals[key] ?: throw ApiException.badRequest("ephemeral message not found"))
    }

    private fun CallContext.updateEphemeral(change: (JsonObject) -> JsonObject): JsonElement {
        val (chat, message) = ephemeral()
        val key = "${params.long("receiver_user_id")}:${params.long("ephemeral_message_id")}"
        chat.ephemerals[key] = change(message)
        return TRUE
    }

    val editEphemeralText: Handler = {
        val text = Messages.formatText(
            core.config.textLength,
            params,
            "text",
            "entities",
            MAX_DRAFT,
            "message text is empty",
            "message is too long",
        )
        updateEphemeral { m ->
            var updated = m.without("entities").with("text" to JsonPrimitive(text.text))
            if (text.entities.isNotEmpty()) updated = updated.with("entities" to JsonArray(text.entities))
            updated
        }
    }

    val editEphemeralCaption: Handler = {
        val caption = Messages.formatText(
            core.config.textLength,
            params,
            "caption",
            "caption_entities",
            1024,
            null,
            "message caption is too long",
        )
        updateEphemeral { m ->
            var updated = m.without("caption", "caption_entities").with("caption" to JsonPrimitive(caption.text))
            if (caption.entities.isNotEmpty()) updated = updated.with("caption_entities" to JsonArray(caption.entities))
            updated
        }
    }

    val editEphemeralMarkup: Handler = {
        val markup = params["reply_markup"] as? JsonObject
        updateEphemeral { m ->
            m.without("reply_markup").let { base ->
                markup?.let { base.with("reply_markup" to it) }
                    ?: base
            }
        }
    }

    val editEphemeralMedia: Handler = {
        val input = params["media"] as? JsonObject ?: throw ApiException.badRequest("media is empty")
        updateEphemeral { m -> Messages.replaceMedia(this, m, input) }
    }

    val deleteEphemeral: Handler = {
        val (chat, _) = ephemeral()
        chat.ephemerals.remove("${params.long("receiver_user_id")}:${params.long("ephemeral_message_id")}")
        TRUE
    }

    // --- widgets, queries ---------------------------------------------------------------------------------------

    private fun CallContext.consume(
        map: java.util.concurrent.ConcurrentMap<String, Boolean>,
        key: String,
        error: String = QUERY_INVALID,
    ) {
        if (!map.replace(params.str(key).orEmpty(), false, true)) throw ApiException.badRequest(error)
    }

    val answerWidget: Handler = {
        consume(bot.extra.widgetQueries, "widget_query_id")
        params.str("data")?.let { bot.extra.widgetAnswers[params.str("widget_query_id")!!] = it }
        TRUE
    }

    val editWidgetData: Handler = {
        if (params.str("public_data") == null &&
            params.str("chat_data") == null
        ) throw ApiException.badRequest("widget data is empty")
        val key = params.str("inline_message_id")?.let { "inline:$it" } ?: run {
            val chat = chat()
            val id = params.long("message_id") ?: throw ApiException.badRequest("message_id is empty")
            if (chat.messages[id] == null) throw ApiException.badRequest("message to edit not found")
            "${chat.id}:$id"
        }
        bot.extra.widgetData[key] =
            JsonObject(listOf("public_data", "chat_data").mapNotNull { k -> params[k]?.let { k to it } }.toMap())
        TRUE
    }

    val editSharedWidget: Handler = {
        val id = params.str("widget_id").orEmpty()
        if (id.isEmpty()) throw ApiException.badRequest("widget_id is empty")
        val cache = params.long("cache_time")
        if (cache != null && cache < 0) throw ApiException.badRequest("invalid cache_time")
        bot.extra.widgetData["shared:$id"] =
            JsonObject(
                listOfNotNull(
                    params["public_data"]?.let { "public_data" to it },
                    cache?.let {
                        "cache_time" to
                            JsonPrimitive(it)
                    },
                ).toMap(),
            )
        TRUE
    }

    val answerInline: Handler = {
        consume(bot.extra.inlineQueries, "inline_query_id")
        TRUE
    }

    private fun CallContext.newInlineMessage(): JsonObject {
        val id = "mock_inline_${bot.extra.next()}"
        bot.extra.inlineMessages += id
        return buildJsonObject { put("inline_message_id", id) }
    }

    val answerWebApp: Handler = {
        consume(bot.extra.webAppQueries, "web_app_query_id")
        newInlineMessage()
    }

    val answerGuest: Handler = {
        consume(bot.extra.guestQueries, "guest_query_id")
        newInlineMessage()
    }

    val answerJoinQuery: Handler = {
        consume(bot.extra.joinQueries, "chat_join_request_query_id")
        TRUE
    }

    val joinWebApp: Handler = {
        val id = params.str("chat_join_request_query_id").orEmpty()
        if (!bot.extra.joinQueries.containsKey(id)) throw ApiException.badRequest(QUERY_INVALID)
        if (params.str("web_app_url")?.startsWith("https://") !=
            true
        ) throw ApiException.badRequest("web_app_url must be https")
        TRUE
    }

    // --- suggested posts --------------------------------------------------------------------------------------

    private fun suggestion(approve: Boolean): Handler = {
        val chat = chat()
        val id = params.long("message_id") ?: throw ApiException.badRequest("message_id is empty")
        val message = chat.messages[id] ?: throw ApiException.badRequest("message not found")
        val info =
            message["suggested_post_info"] as? JsonObject
                ?: throw ApiException.badRequest("message is not a suggested post")
        if (info.str("state") != "pending") throw ApiException.badRequest("SUGGESTED_POST_NOT_PENDING")
        val changed = info
            .with("state" to JsonPrimitive(if (approve) "approved" else "declined"))
            .let { i -> if (approve) i.with("send_date" to (params["send_date"] ?: JsonPrimitive(nowSeconds))) else i }
        chat.messages[id] = message.with("suggested_post_info" to changed)
        TRUE
    }

    val approveSuggested = suggestion(true)
    val declineSuggested = suggestion(false)

    fun boostJson(core: MockCore, user: JsonObject, extra: ExtraState): JsonObject {
        val now = core.config.clock.millis() / 1000
        return core.generator.generateObject(
            "ChatBoost",
            overrides = mapOf(
                "boost_id" to JsonPrimitive("mock-boost-${extra.next()}"),
                "add_date" to JsonPrimitive(now),
                "expiration_date" to JsonPrimitive(now + 30 * SECONDS_PER_DAY),
                "source" to buildJsonObject {
                    put("source", "premium")
                    put("user", user)
                },
            ),
        )!!
    }
}
