package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

/** State of the less common method groups: bot profile, stickers, business, payments, games. */
class ExtraState {
    /** Bot profile settings (name, descriptions, menu buttons, default rights) keyed by `kind|scope`. */
    val settings = ConcurrentHashMap<String, JsonElement>()
    val businessConnections = ConcurrentHashMap<String, JsonObject>()
    val businessStars = ConcurrentHashMap<String, Long>()
    val stories = ConcurrentHashMap<Long, JsonObject>()
    val stickerSets = ConcurrentHashMap<String, JsonObject>()
    val gameScores = ConcurrentHashMap<String, ConcurrentHashMap<Long, Long>>()

    /** Shipping and pre-checkout queries: id to "already answered". */
    val paymentQueries = ConcurrentHashMap<String, Boolean>()
    val ledger = CopyOnWriteArrayList<JsonObject>()
    val refunded = ConcurrentHashMap.newKeySet<String>()
    val gifts = CopyOnWriteArrayList<JsonObject>()
    val ownedGifts = ConcurrentHashMap<Long, CopyOnWriteArrayList<JsonObject>>()
    val managedTokens = ConcurrentHashMap<Long, String>()
    val managedAccess = ConcurrentHashMap<Long, JsonObject>()
    val invoiceLinks = ConcurrentHashMap<String, JsonObject>()

    val inlineMessages = ConcurrentHashMap.newKeySet<String>()
    val games = ConcurrentHashMap<String, JsonObject>()
    val emojiStatusGrants = ConcurrentHashMap.newKeySet<Long>()

    @Volatile
    var canVerify = false
    val businessAccounts = ConcurrentHashMap<String, JsonObject>()
    val stickerKeywords = ConcurrentHashMap<String, List<String>>()
    val shareBackdrops = CopyOnWriteArrayList<JsonObject>()
    val verifiedUsers = ConcurrentHashMap<Long, String>()
    val verifiedChats = ConcurrentHashMap<Long, String>()
    val emojiStatus = ConcurrentHashMap<Long, JsonObject>()
    val boosts = ConcurrentHashMap<String, CopyOnWriteArrayList<JsonObject>>()
    val profilePhotos = ConcurrentHashMap<Long, Int>()
    val profileAudios = ConcurrentHashMap<Long, Int>()
    val personalChatMessages = ConcurrentHashMap<Long, List<JsonObject>>()
    val passportErrors = ConcurrentHashMap<Long, List<JsonObject>>()
    val subscriptionCanceled = ConcurrentHashMap<String, Boolean>()
    val premiumGifts = CopyOnWriteArrayList<JsonObject>()
    val businessGifts = ConcurrentHashMap<String, CopyOnWriteArrayList<JsonObject>>()

    /** One-shot query ids (inline, widget, web app, guest, join request): id to "already answered". */
    val inlineQueries = ConcurrentHashMap<String, Boolean>()
    val widgetQueries = ConcurrentHashMap<String, Boolean>()
    val webAppQueries = ConcurrentHashMap<String, Boolean>()
    val guestQueries = ConcurrentHashMap<String, Boolean>()
    val joinQueries = ConcurrentHashMap<String, Boolean>()
    val widgetAnswers = ConcurrentHashMap<String, String>()
    val widgetData = ConcurrentHashMap<String, JsonObject>()

    val starBalance = AtomicLong()
    private val counter = AtomicLong()

    fun next(): Long = counter.incrementAndGet()

    fun clear() {
        settings.clear()
        businessConnections.clear()
        businessStars.clear()
        stories.clear()
        stickerSets.clear()
        gameScores.clear()
        paymentQueries.clear()
        ledger.clear()
        refunded.clear()
        gifts.clear()
        ownedGifts.clear()
        managedTokens.clear()
        managedAccess.clear()
        invoiceLinks.clear()
        businessAccounts.clear()
        inlineMessages.clear()
        games.clear()
        emojiStatusGrants.clear()
        canVerify = false
        stickerKeywords.clear()
        shareBackdrops.clear()
        verifiedUsers.clear()
        verifiedChats.clear()
        emojiStatus.clear()
        boosts.clear()
        profilePhotos.clear()
        profileAudios.clear()
        personalChatMessages.clear()
        passportErrors.clear()
        subscriptionCanceled.clear()
        premiumGifts.clear()
        businessGifts.clear()
        inlineQueries.clear()
        widgetQueries.clear()
        webAppQueries.clear()
        guestQueries.clear()
        joinQueries.clear()
        widgetAnswers.clear()
        widgetData.clear()
        starBalance.set(0)
    }
}
