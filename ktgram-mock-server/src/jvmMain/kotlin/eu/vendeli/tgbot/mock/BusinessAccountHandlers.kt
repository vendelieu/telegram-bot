package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val MAX_NAME = 64
private const val MAX_BIO = 140
private val USERNAME = Regex("^[A-Za-z][A-Za-z0-9_]{4,31}$")

/** Profile edits of a managed business account, plus reading and deleting its messages. */
internal object BusinessAccountHandlers {
    private fun CallContext.connectionId() = params.str("business_connection_id").orEmpty()

    private fun CallContext.connection(): JsonObject = bot.extra.businessConnections.getValue(connectionId())

    private fun CallContext.account(): JsonObject = bot.extra.businessAccounts[connectionId()] ?: JsonObject(emptyMap())

    private fun CallContext.update(account: JsonObject, user: JsonObject? = null): JsonElement {
        bot.extra.businessAccounts[connectionId()] = account
        user?.let { changed ->
            bot.extra.businessConnections[connectionId()] = connection().with("user" to changed)
            changed.long("id")?.let { bot.users[it] = changed }
        }
        return TRUE
    }

    private fun JsonObject.withUser(vararg entries: Pair<String, JsonElement>) = with(*entries)

    private fun CallContext.user(): JsonObject = connection()["user"] as? JsonObject ?: JsonObject(emptyMap())

    val setName: Handler = {
        val first = params.str("first_name").orEmpty()
        val last = params.str("last_name")
        if (first.isEmpty() || first.length > MAX_NAME || (last?.length ?: 0) > MAX_NAME) {
            throw ApiException.badRequest("names must be 1-$MAX_NAME characters long")
        }
        val user = user()
            .without("last_name")
            .withUser("first_name" to JsonPrimitive(first))
            .let { u -> last?.takeIf { it.isNotEmpty() }?.let { u.withUser("last_name" to JsonPrimitive(it)) } ?: u }
        update(account().with("first_name" to JsonPrimitive(first)), user)
    }

    val setUsername: Handler = {
        val username = params.str("username").orEmpty()
        val id = connectionId()
        if (username.isNotEmpty()) {
            if (!USERNAME.matches(username)) throw ApiException.badRequest("USERNAME_INVALID")
            val taken = bot.extra.businessAccounts.any { (other, a) ->
                other != id &&
                    a.str("username").equals(username, true)
            }
            if (taken) throw ApiException.badRequest("USERNAME_OCCUPIED")
        }
        val user = user()
            .without(
                "username",
            ).let { u -> if (username.isEmpty()) u else u.withUser("username" to JsonPrimitive(username)) }
        update(
            account().without("username").let {
                if (username.isEmpty()) it else it.with("username" to JsonPrimitive(username))
            },
            user,
        )
    }

    val setBio: Handler = {
        val bio = params.str("bio").orEmpty()
        if (bio.length > MAX_BIO) throw ApiException.badRequest("BIO_TOO_LONG")
        update(account().without("bio").let { if (bio.isEmpty()) it else it.with("bio" to JsonPrimitive(bio)) })
    }

    /** Checks an InputProfilePhoto (static or animated) and returns the `attach://` or file reference it uses. */
    fun profilePhotoRef(ctx: CallContext, key: String = "photo"): String {
        val input = ctx.params[key] as? JsonObject ?: throw ApiException.badRequest("photo is empty")
        val type = input.str("type")
        val ref = when (type) {
            "static" -> input.str("photo")
            "animated" -> input.str("animation")
            else -> null
        } ?: throw ApiException.badRequest("PHOTO_INVALID")
        if (ref.startsWith("attach://") && !ctx.uploads.containsKey(ref.removePrefix("attach://"))) {
            throw ApiException.badRequest("wrong file identifier/HTTP URL specified")
        }
        return ref
    }

    val setProfilePhoto: Handler = {
        profilePhotoRef(this)
        val photo = buildJsonObject {
            put("type", (params["photo"] as JsonObject).str("type").orEmpty())
            put("is_public", params.bool("is_public") ?: false)
        }
        update(account().with("photo" to photo))
    }

    val removeProfilePhoto: Handler = { update(account().without("photo")) }

    val setGiftSettings: Handler = {
        val settings = buildJsonObject {
            put("show_gift_button", params.bool("show_gift_button") ?: false)
            put("accepted_gift_types", params.getValue("accepted_gift_types"))
        }
        update(account().with("gift_settings" to settings))
    }

    val read: Handler = {
        val chat = chat()
        val id = params.long("message_id") ?: throw ApiException.badRequest("message_id is empty")
        val message = chat.messages[id]
        if (message == null || message.str("business_connection_id") != connectionId()) {
            throw ApiException.badRequest("message not found")
        }
        chat.readMessages += id
        TRUE
    }

    val delete: Handler = {
        val connection = connection()
        val chat = bot.chats[connection.long("user_chat_id") ?: 0L]
        val ids = (params["message_ids"] as? JsonArray).orEmpty().mapNotNull {
            (it as? JsonPrimitive)
                ?.content
                ?.toLongOrNull()
        }
        val found = ids.mapNotNull { id ->
            chat
                ?.messages
                ?.get(id)
                ?.takeIf {
                    it.str("business_connection_id") ==
                        connectionId()
                }?.let { id to it }
        }
        if (found.isEmpty()) throw ApiException.badRequest("MESSAGE_ID_INVALID")
        found.forEach { (id, message) ->
            val isOwn = (message["from"] as? JsonObject)?.bool("is_bot") == true
            val right = if (isOwn) "can_delete_sent_messages" else "can_delete_all_messages"
            val rights = connection["rights"] as? JsonObject
            if (rights != null && rights.bool(right) != true && rights.bool("can_delete_all_messages") != true) {
                throw ApiException.badRequest("not enough rights: $right is required")
            }
        }
        found.forEach { (id, _) -> chat!!.messages.remove(id) }
        TRUE
    }
}
