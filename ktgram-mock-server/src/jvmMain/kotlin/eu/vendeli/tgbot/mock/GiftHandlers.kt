package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val MAX_PAGE = 100
private const val NOT_FOUND = "OWNED_GIFT_NOT_FOUND"

/** The gifts a business account owns: list, convert to Stars, upgrade, transfer, delete. */
internal object GiftHandlers {
    private fun CallContext.connectionId(): String =
        params.str("business_connection_id").orEmpty()

    private fun CallContext.gifts() = bot.extra.businessGifts.computeIfAbsent(connectionId()) {
        java.util.concurrent.CopyOnWriteArrayList()
    }

    private fun CallContext.find(): JsonObject {
        val id = params.str("owned_gift_id").orEmpty()
        return gifts().firstOrNull { it.str("owned_gift_id") == id } ?: throw ApiException.badRequest(NOT_FOUND)
    }

    private fun price(gift: JsonObject) = (gift["gift"] as? JsonObject)?.long("star_count") ?: 0L

    val list: Handler = {
        var all = gifts().toList()
        if (params.bool("exclude_unique") == true) all = all.filter { it.str("type") != "unique" }
        if (params.bool("exclude_saved") == true) all = all.filter { it.bool("is_saved") != true }
        if (params.bool("exclude_unsaved") == true) all = all.filter { it.bool("is_saved") == true }
        if (params.bool("sort_by_price") == true) all = all.sortedByDescending(::price)
        val offset = params.str("offset")?.toIntOrNull()?.coerceAtLeast(0) ?: 0
        val limit = (params.long("limit") ?: MAX_PAGE.toLong()).toInt().coerceIn(1, MAX_PAGE)
        val page = all.drop(offset).take(limit)
        buildJsonObject {
            put("total_count", all.size)
            put("gifts", JsonArray(page))
            if (offset + limit < all.size) put("next_offset", (offset + limit).toString())
        }
    }

    val convert: Handler = {
        val gift = find()
        if (gift.str("type") == "unique") throw ApiException.badRequest("GIFT_CONVERT_UNAVAILABLE")
        val stars = gift.long("convert_star_count") ?: price(gift)
        gifts().remove(gift)
        bot.extra.businessStars.merge(connectionId(), stars, Long::plus)
        TRUE
    }

    val upgrade: Handler = {
        val gift = find()
        if (gift.str("type") == "unique") throw ApiException.badRequest("GIFT_ALREADY_UNIQUE")
        val cost = params.long("star_count") ?: 0L
        spend(cost)
        val unique = generator.generateObject(
            "OwnedGiftUnique",
            overrides = mapOf(
                "type" to JsonPrimitive("unique"),
                "owned_gift_id" to JsonPrimitive(gift.str("owned_gift_id")!!),
                "send_date" to JsonPrimitive(nowSeconds),
                "can_be_transferred" to JsonPrimitive(true),
            ),
        )!!
        gifts()[gifts().indexOf(gift)] = unique
        TRUE
    }

    val transfer: Handler = {
        val gift = find()
        if (gift.str("type") != "unique") throw ApiException.badRequest("GIFT_NOT_TRANSFERABLE")
        val owner = params.long("new_owner_chat_id") ?: throw ApiException.badRequest("new_owner_chat_id is empty")
        spend(params.long("star_count") ?: 0L)
        gifts().remove(gift)
        bot.extra.ownedGifts.computeIfAbsent(owner) { java.util.concurrent.CopyOnWriteArrayList() } += gift
        TRUE
    }

    val delete: Handler = {
        gifts().remove(find())
        TRUE
    }

    private fun CallContext.spend(cost: Long) {
        if (cost <= 0) return
        var isEnough = false
        bot.extra.businessStars.compute(connectionId()) { _, have ->
            val current = have ?: 0L
            if (current >= cost) {
                isEnough = true
                current - cost
            } else {
                current
            }
        }
        if (!isEnough) throw ApiException.badRequest("BALANCE_TOO_LOW")
    }
}
