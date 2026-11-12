package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val MAX_NAME = 64
private const val MAX_DESCRIPTION = 512
private const val MAX_SHORT_DESCRIPTION = 120
private const val SECONDS_PER_HOUR = 3600L
private const val DEFAULT_GIFT_STARS = 25L
private const val MAX_STAR_PAGE = 100
private val STORY_PERIODS = setOf(21_600L, 43_200L, 86_400L, 172_800L)
private const val QUERY_EXPIRED = "query is too old and response timeout expired or query ID is invalid"

/** Bot profile, business accounts, stories, gifts, stars, payments, games and managed bots. */
internal object AccountHandlers {
    private fun CallContext.scope() = params.str("language_code").orEmpty()

    private fun textSetting(kind: String, field: String, max: Int, tooLong: String): Handler = {
        val key = "$kind|${scope()}"
        val value = params.str(field).orEmpty()
        if (value.length > max) throw ApiException.badRequest(tooLong)
        if (value.isEmpty()) bot.extra.settings.remove(key) else bot.extra.settings[key] = JsonPrimitive(value)
        TRUE
    }

    private fun textGetter(kind: String, field: String, default: String): Handler = {
        val stored = bot.extra.settings["$kind|${scope()}"] ?: bot.extra.settings["$kind|"]
        buildJsonObject { put(field, (stored as? JsonPrimitive)?.content ?: default) }
    }

    val setName = textSetting("name", "name", MAX_NAME, "BOT_NAME_TOO_LONG")
    val getName: Handler = { textGetter("name", "name", "Mock Bot")(this) }
    val setDescription = textSetting("description", "description", MAX_DESCRIPTION, "BOT_DESCRIPTION_TOO_LONG")
    val getDescription: Handler = { textGetter("description", "description", "")(this) }
    val setShortDescription =
        textSetting("short", "short_description", MAX_SHORT_DESCRIPTION, "BOT_SHORT_DESCRIPTION_TOO_LONG")
    val getShortDescription: Handler = { textGetter("short", "short_description", "")(this) }

    val setProfilePhoto: Handler = {
        BusinessAccountHandlers.profilePhotoRef(this)
        bot.extra.profilePhotos[bot.id] = 1
        TRUE
    }

    val removeProfilePhoto: Handler = {
        bot.extra.profilePhotos.remove(bot.id)
        TRUE
    }

    val shareThemeBackdrops: Handler = {
        val list = bot.extra.shareBackdrops.toList().ifEmpty {
            (1..2).map { n ->
                generator.generateObject(
                    "ChatShareThemeBackdrop",
                    overrides = mapOf("id" to JsonPrimitive(n), "name" to JsonPrimitive("Mock backdrop $n")),
                )!!
            }
        }
        buildJsonObject { put("backdrops", JsonArray(list)) }
    }

    val setMenuButton: Handler = {
        val key = "menu|${params.str("chat_id").orEmpty()}"
        val button = params["menu_button"] as? JsonObject
        if (button == null) bot.extra.settings.remove(key) else bot.extra.settings[key] = button
        TRUE
    }

    val getMenuButton: Handler = {
        bot.extra.settings["menu|${params.str("chat_id").orEmpty()}"]
            ?: buildJsonObject { put("type", "default") }
    }

    val setDefaultRights: Handler = {
        val key = "rights|${params.bool("for_channels") == true}"
        val rights = params["rights"] as? JsonObject
        if (rights == null) bot.extra.settings.remove(key) else bot.extra.settings[key] = rights
        TRUE
    }

    val getDefaultRights: Handler = {
        bot.extra.settings["rights|${params.bool("for_channels") == true}"]
            ?: generator.generateObject("ChatAdministratorRights")!!
    }

    // --- business ---------------------------------------------------------------------------------------------

    private fun CallContext.connection(): JsonObject {
        val id = params.str("business_connection_id").orEmpty()
        return bot.extra.businessConnections[id] ?: throw ApiException.badRequest("business connection not found")
    }

    val getConnection: Handler = { connection() }

    val businessAck: Handler = {
        connection()
        TRUE
    }

    val businessStarBalance: Handler = {
        val id = connection().str("id")!!
        buildJsonObject { put("amount", bot.extra.businessStars[id] ?: 0L) }
    }

    val transferStars: Handler = {
        val id = connection().str("id")!!
        val count = params.long("star_count") ?: throw ApiException.badRequest("star_count is empty")
        var isEnough = false
        bot.extra.businessStars.compute(id) { _, have ->
            val current = have ?: 0L
            if (current >= count) {
                isEnough = true
                current - count
            } else {
                current
            }
        }
        if (!isEnough) throw ApiException.badRequest("BALANCE_TOO_LOW")
        bot.extra.starBalance.addAndGet(count)
        TRUE
    }

    val postStory: Handler = {
        val connection = connection()
        val period = params.long("active_period")
        if (period !in STORY_PERIODS) throw ApiException.badRequest("invalid active_period")
        val id = bot.extra.next()
        val story = buildJsonObject {
            put(
                "chat",
                buildJsonObject {
                    put("id", connection.long("user_chat_id") ?: 0L)
                    put("type", "private")
                },
            )
            put("id", id)
        }
        bot.extra.stories[id] = story
        story
    }

    private fun CallContext.story(): JsonObject {
        connection()
        val id = params.long("story_id") ?: throw ApiException.badRequest("story_id is empty")
        return bot.extra.stories[id] ?: throw ApiException.badRequest("STORY_NOT_FOUND")
    }

    val editStory: Handler = { story() }

    val deleteStory: Handler = {
        bot.extra.stories.remove(story().long("id"))
        TRUE
    }

    val repostStory: Handler = {
        val connection = connection()
        val period = params.long("active_period")
        if (period !in STORY_PERIODS) throw ApiException.badRequest("invalid active_period")
        val id = bot.extra.next()
        val story = (
            bot.extra.stories[
                params.long(
                    "from_story_id",
                ),
            ] ?: throw ApiException.badRequest("STORY_NOT_FOUND")
        ).with("id" to JsonPrimitive(id))
            .with(
                "chat" to buildJsonObject {
                    put("id", connection.long("user_chat_id") ?: 0L)
                    put("type", "private")
                },
            )
        bot.extra.stories[id] = story
        story
    }

    // --- gifts and stars ---------------------------------------------------------------------------------------

    private fun CallContext.catalogue(): List<JsonObject> = bot.extra.gifts.toList().ifEmpty {
        listOf(
            generator.generateObject(
                "Gift",
                overrides = mapOf(
                    "id" to JsonPrimitive("mock_gift_1"),
                    "star_count" to JsonPrimitive(DEFAULT_GIFT_STARS),
                    "sticker" to
                        generator.generateObject("Sticker", overrides = mapOf("type" to JsonPrimitive("regular")))!!,
                ),
            )!!,
        )
    }

    val availableGifts: Handler = { buildJsonObject { put("gifts", JsonArray(catalogue())) } }

    val sendGift: Handler = {
        val gift = catalogue().firstOrNull { it.str("id") == params.str("gift_id") }
            ?: throw ApiException.badRequest("invalid gift_id")
        val target = params.long("user_id") ?: params.long("chat_id")
            ?: throw ApiException.badRequest("user_id or chat_id must be specified")
        val price = gift.long("star_count") ?: 0L
        val balance = bot.extra.starBalance
        if (balance.addAndGet(-price) < 0) {
            balance.addAndGet(price)
            throw ApiException.badRequest("BALANCE_TOO_LOW")
        }
        val owned = generator.generateObject(
            "OwnedGiftRegular",
            overrides = mapOf(
                "type" to JsonPrimitive("regular"),
                "gift" to gift,
                "send_date" to JsonPrimitive(nowSeconds),
                "owned_gift_id" to JsonPrimitive("og_${bot.extra.next()}"),
            ),
        )!!
        bot.extra.ownedGifts.computeIfAbsent(target) { java.util.concurrent.CopyOnWriteArrayList() } += owned
        TRUE
    }

    private fun ownedGifts(key: String): Handler = {
        val id = params.long(key) ?: throw ApiException.badRequest("$key is empty")
        val list = bot.extra.ownedGifts[id].orEmpty()
        buildJsonObject {
            put("total_count", list.size)
            put("gifts", JsonArray(list.toList()))
        }
    }

    val userGifts: Handler = { ownedGifts("user_id")(this) }
    val chatGifts: Handler = {
        val chat = chat()
        val list = bot.extra.ownedGifts[chat.id].orEmpty()
        buildJsonObject {
            put("total_count", list.size)
            put("gifts", JsonArray(list.toList()))
        }
    }

    val starBalance: Handler = { buildJsonObject { put("amount", bot.extra.starBalance.get()) } }

    val starTransactions: Handler = {
        val offset = (params.long("offset") ?: 0L).toInt().coerceAtLeast(0)
        val limit = (params.long("limit") ?: MAX_STAR_PAGE.toLong()).toInt().coerceIn(1, MAX_STAR_PAGE)
        buildJsonObject {
            put(
                "transactions",
                JsonArray(
                    bot.extra.ledger
                        .drop(offset)
                        .take(limit),
                ),
            )
        }
    }

    val refund: Handler = {
        val charge = params.str("telegram_payment_charge_id").orEmpty()
        val userId = params.long("user_id")
        val entry =
            bot.extra.ledger.firstOrNull {
                it.str("id") == charge && (it.long("user_id") == userId) &&
                    (it.long("amount") ?: 0) > 0
            }
                ?: throw ApiException.badRequest("CHARGE_ID_EMPTY")
        if (!bot.extra.refunded.add(charge)) throw ApiException.badRequest("CHARGE_ALREADY_REFUNDED")
        val amount = entry.long("amount") ?: 0L
        bot.extra.starBalance.addAndGet(-amount)
        bot.extra.ledger += buildJsonObject {
            put("id", "refund_$charge")
            put("amount", -amount)
            put("date", nowSeconds)
            put("user_id", userId ?: 0L)
        }
        TRUE
    }

    // --- payments ---------------------------------------------------------------------------------------------

    val createInvoiceLink: Handler = {
        val prices = (params["prices"] as? JsonArray).orEmpty()
        if (prices.isEmpty()) throw ApiException.badRequest("prices must not be empty")
        val link = "https://t.me/\$mockinv${bot.extra.next()}"
        bot.extra.invoiceLinks[link] = params
        JsonPrimitive(link)
    }

    private fun CallContext.answerPayment(idKey: String, needsOptions: Boolean): JsonElement {
        val id = params.str(idKey).orEmpty()
        val ok = params.bool("ok") ?: throw ApiException.badRequest("ok is empty")
        if (!ok &&
            params.str("error_message").isNullOrEmpty()
        ) throw ApiException.badRequest("error_message is required if ok is false")
        if (ok && needsOptions && (params["shipping_options"] as? JsonArray).isNullOrEmpty()) {
            throw ApiException.badRequest("shipping_options is required if ok is true")
        }
        if (bot.extra.paymentQueries
                .replace(id, false, true)
                .not()
        ) throw ApiException.badRequest(QUERY_EXPIRED)
        return TRUE
    }

    val answerShipping: Handler = { answerPayment("shipping_query_id", needsOptions = true) }
    val answerPreCheckout: Handler = { answerPayment("pre_checkout_query_id", needsOptions = false) }

    // --- games ------------------------------------------------------------------------------------------------

    private fun CallContext.gameKey(): String =
        params.str("inline_message_id")?.let { "inline:$it" }
            ?: "${params.str("chat_id")}:${params.str("message_id")}"

    val setGameScore: Handler = {
        val userId = params.long("user_id") ?: throw ApiException.badRequest("invalid user_id specified")
        val score = params.long("score") ?: throw ApiException.badRequest("score is empty")
        if (score < 0) throw ApiException.badRequest("SCORE_INVALID")
        val inline = params.str("inline_message_id") != null
        val chat = if (inline) null else chat()
        val messageId = params.long("message_id")
        val message =
            chat?.messages?.get(messageId ?: -1)
                ?: if (inline) null else throw ApiException.badRequest("message to edit not found")
        if (!inline && message?.get("game") == null) throw ApiException.badRequest("message can't be edited")
        val scores = bot.extra.gameScores.computeIfAbsent(gameKey()) { java.util.concurrent.ConcurrentHashMap() }
        val current = scores[userId]
        if (current != null && score <= current && params.bool("force") != true) {
            throw ApiException.badRequest("BOT_SCORE_NOT_MODIFIED")
        }
        scores[userId] = score
        if (inline || params.bool("disable_edit_message") == true) {
            TRUE
        } else {
            val edited = message!!.with("edit_date" to JsonPrimitive(nowSeconds))
            chat!!.messages[messageId!!] = edited
            edited
        }
    }

    val getGameHighScores: Handler = {
        val scores = bot.extra.gameScores[gameKey()]
            .orEmpty()
            .entries
            .sortedByDescending { it.value }
        JsonArray(
            scores.mapIndexed { index, (userId, score) ->
                buildJsonObject {
                    put("position", index + 1)
                    put(
                        "user",
                        bot.users[userId] ?: buildJsonObject {
                            put("id", userId)
                            put("is_bot", false)
                            put("first_name", "Mock")
                        },
                    )
                    put("score", score)
                }
            },
        )
    }

    // --- managed bots and prepared messages ---------------------------------------------------------------------

    private fun CallContext.managedUser(): Long =
        params.long("user_id") ?: throw ApiException.badRequest("invalid user_id specified")

    private fun CallContext.freshToken(userId: Long) =
        "$userId:Managed${bot.extra.next()}".padEnd(MANAGED_TOKEN_MIN, 'x')

    val managedToken: Handler = {
        val id = managedUser()
        JsonPrimitive(bot.extra.managedTokens.computeIfAbsent(id) { freshToken(it) })
    }

    val replaceManagedToken: Handler = {
        val id = managedUser()
        JsonPrimitive(freshToken(id).also { bot.extra.managedTokens[id] = it })
    }

    val managedAccess: Handler = {
        bot.extra.managedAccess[managedUser()] ?: buildJsonObject { put("is_access_restricted", false) }
    }

    val setManagedAccess: Handler = {
        bot.extra.managedAccess[managedUser()] = JsonObject(
            listOf("is_access_restricted", "added_user_ids").mapNotNull { k -> params[k]?.let { k to it } }.toMap(),
        )
        TRUE
    }

    val preparedInline: Handler = {
        val allowed = listOf("allow_user_chats", "allow_bot_chats", "allow_group_chats", "allow_channel_chats")
        if (allowed.none {
                params.bool(
                    it,
                ) == true
            }
        ) throw ApiException.badRequest("at least one chat type must be allowed")
        buildJsonObject {
            put("id", "mock_prepared_${bot.extra.next()}")
            put("expiration_date", nowSeconds + SECONDS_PER_HOUR)
        }
    }

    val preparedButton: Handler = { buildJsonObject { put("id", "mock_button_${bot.extra.next()}") } }

    private const val MANAGED_TOKEN_MIN = 45
}
