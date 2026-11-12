package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val NOT_MODIFIED =
    "message is not modified: specified new message content and reply markup are exactly the same as a current content and reply markup of the message"

/** Resolves the chat of a delivery (send, forward, copy), enforcing blocks and flood limits. */
internal fun CallContext.deliveryChat(key: String = "chat_id"): ChatState {
    val chat = chat(key)
    bot.requireDeliverable(chat)
    bot.limiter?.check(chat)
    return chat
}

internal object MessageHandlers {
    val send: Handler = {
        val chat = deliveryChat()
        if (method.name.equals("sendMediaGroup", ignoreCase = true)) {
            Messages.mediaGroup(this, chat, nowSeconds)
        } else {
            val receiver = params.long("receiver_user_id")
            if (receiver != null) {
                SocialHandlers.sendEphemeral(this, chat, Messages.build(this, chat), receiver)
            } else {
                Messages.store(chat, Messages.build(this, chat), nowSeconds)
            }
        }
    }

    private fun CallContext.target(chat: ChatState, error: String): Pair<Long, JsonObject> {
        val id = params.long("message_id") ?: throw ApiException.badRequest("message_id is empty")
        return id to (chat.messages[id] ?: throw ApiException.badRequest(error))
    }

    private fun result(chat: ChatState, id: Long, updated: JsonObject, nowSeconds: Long): JsonObject {
        val withDate = updated.with("edit_date" to JsonPrimitive(nowSeconds))
        chat.messages[id] = withDate
        return withDate
    }

    /** Inline-message edits cannot be tracked (they live in the user's client), they just succeed. */
    private fun CallContext.isInline() = params.str("inline_message_id") != null

    val editText: Handler = {
        if (isInline()) {
            TRUE
        } else {
            val chat = chat()
            val (id, message) = target(chat, "message to edit not found")
            requireEditable(message)
            if (message["text"] == null) throw ApiException.badRequest("there is no text in the message to edit")
            val text = Messages.formatText(
                core.config.textLength,
                params,
                "text",
                "entities",
                MAX_TEXT,
                "message text is empty",
                "message is too long",
            )
            val markup = (params["reply_markup"] as? JsonObject)
            val unchanged = message.str("text") == text.text &&
                message.entityList("entities") == text.entities &&
                message["reply_markup"] == markup
            if (unchanged) throw ApiException.badRequest(NOT_MODIFIED)
            var updated = message.without("entities", "reply_markup").with("text" to JsonPrimitive(text.text))
            if (text.entities.isNotEmpty()) updated = updated.with("entities" to JsonArray(text.entities))
            markup?.let { updated = updated.with("reply_markup" to it) }
            result(chat, id, updated, nowSeconds)
        }
    }

    val editCaption: Handler = {
        if (isInline()) {
            TRUE
        } else {
            val chat = chat()
            val (id, message) = target(chat, "message to edit not found")
            requireEditable(message)
            if (!Messages.hasMedia(message)) throw ApiException.badRequest("there is no caption in the message to edit")
            val caption = Messages.formatText(
                core.config.textLength,
                params,
                "caption",
                "caption_entities",
                MAX_CAPTION,
                null,
                "message caption is too long",
            )
            val markup = params["reply_markup"] as? JsonObject
            val unchanged = message.str("caption").orEmpty() == caption.text &&
                message.entityList("caption_entities") == caption.entities &&
                message["reply_markup"] == markup
            if (unchanged) throw ApiException.badRequest(NOT_MODIFIED)
            var updated = message.without("caption", "caption_entities", "reply_markup")
            if (caption.text.isNotEmpty()) updated = updated.with("caption" to JsonPrimitive(caption.text))
            if (caption.entities.isNotEmpty()) updated = updated.with("caption_entities" to JsonArray(caption.entities))
            markup?.let { updated = updated.with("reply_markup" to it) }
            result(chat, id, updated, nowSeconds)
        }
    }

    val editReplyMarkup: Handler = {
        if (isInline()) {
            TRUE
        } else {
            val chat = chat()
            val (id, message) = target(chat, "message to edit not found")
            requireEditable(message)
            val markup = params["reply_markup"] as? JsonObject
            if (message["reply_markup"] == markup) throw ApiException.badRequest(NOT_MODIFIED)
            val updated = message.without("reply_markup").let { m -> markup?.let { m.with("reply_markup" to it) } ?: m }
            result(chat, id, updated, nowSeconds)
        }
    }

    val editMedia: Handler = {
        if (isInline()) {
            TRUE
        } else {
            val chat = chat()
            val (id, message) = target(chat, "message to edit not found")
            requireEditable(message)
            val input = params["media"] as? JsonObject ?: throw ApiException.badRequest("media is empty")
            var updated = Messages.replaceMedia(this, message, input).without("reply_markup")
            (params["reply_markup"] as? JsonObject)?.let { updated = updated.with("reply_markup" to it) }
            result(chat, id, updated, nowSeconds)
        }
    }

    val editLiveLocation: Handler = {
        if (isInline()) {
            TRUE
        } else {
            val chat = chat()
            val (id, message) = target(chat, "message to edit not found")
            val location =
                message["location"] as? JsonObject ?: throw ApiException.badRequest("message can't be edited")
            val optional = listOf("horizontal_accuracy", "heading", "proximity_alert_radius", "live_period")
                .mapNotNull { key -> params[key]?.let { key to it } }
            val moved = location.with(
                "latitude" to params.getValue("latitude"),
                "longitude" to params.getValue("longitude"),
                *optional.toTypedArray(),
            )
            result(chat, id, message.with("location" to moved), nowSeconds)
        }
    }

    val stopLiveLocation: Handler = {
        if (isInline()) {
            TRUE
        } else {
            val chat = chat()
            val (id, message) = target(chat, "message to edit not found")
            val location =
                message["location"] as? JsonObject ?: throw ApiException.badRequest("message can't be edited")
            result(chat, id, message.with("location" to location.without("live_period")), nowSeconds)
        }
    }

    val editChecklist: Handler = {
        val chat = chat()
        val (id, message) = target(chat, "message to edit not found")
        if (message["checklist"] == null) throw ApiException.badRequest("message can't be edited")
        val updated = message.with("checklist" to Messages.checklist(params["checklist"] as? JsonObject))
        result(chat, id, updated, nowSeconds)
    }

    val stopPoll: Handler = {
        val chat = chat()
        val (id, message) = target(chat, "message with the poll not found")
        val poll =
            message["poll"] as? JsonObject ?: throw ApiException.badRequest("message with poll to stop not found")
        if (poll.bool("is_closed") == true) throw ApiException.badRequest("poll has already been closed")
        val closed = poll.with("is_closed" to JsonPrimitive(true))
        chat.messages[id] = message.with("poll" to closed)
        closed
    }

    val delete: Handler = {
        val chat = chat()
        val id = params.long("message_id") ?: throw ApiException.badRequest("message_id is empty")
        chat.messages.remove(id) ?: throw ApiException.badRequest("message to delete not found")
        TRUE
    }

    val deleteMany: Handler = {
        val chat = chat()
        val ids = (params["message_ids"] as? JsonArray).orEmpty().mapNotNull {
            (it as? JsonPrimitive)
                ?.content
                ?.toLongOrNull()
        }
        if (ids.isEmpty()) throw ApiException.badRequest("message identifiers are not specified")
        ids.forEach { chat.messages.remove(it) }
        TRUE
    }

    val forward: Handler = {
        val target = deliveryChat()
        val (_, source) = sourceMessage("message to forward not found")
        Messages.store(target, forwarded(source, target, this), nowSeconds)
    }

    val forwardMany: Handler = {
        val target = deliveryChat()
        JsonArray(
            sourceMessages("messages to forward not found").map {
                Messages.store(target, forwarded(it, target, this), nowSeconds)
            },
        )
    }

    val copy: Handler = {
        val target = deliveryChat()
        val (_, source) = sourceMessage("message to copy not found")
        val stored = Messages.store(target, copied(source, target, this), nowSeconds)
        buildJsonObject { put("message_id", stored.long("message_id")!!) }
    }

    val copyMany: Handler = {
        val target = deliveryChat()
        JsonArray(
            sourceMessages("messages to copy not found").map {
                val stored = Messages.store(target, copied(it, target, this), nowSeconds)
                buildJsonObject { put("message_id", stored.long("message_id")!!) }
            },
        )
    }

    private fun CallContext.sourceChat(): ChatState {
        val raw = params["from_chat_id"]
        return bot.resolveChat(raw)
    }

    private fun CallContext.sourceMessage(error: String): Pair<Long, JsonObject> {
        val id = params.long("message_id") ?: throw ApiException.badRequest("message_id is empty")
        return id to (sourceChat().messages[id] ?: throw ApiException.badRequest(error))
    }

    private fun CallContext.sourceMessages(error: String): List<JsonObject> {
        val chat = sourceChat()
        val found = (params["message_ids"] as? JsonArray)
            .orEmpty()
            .mapNotNull { (it as? JsonPrimitive)?.content?.toLongOrNull() }
            .sorted()
            .mapNotNull { chat.messages[it] }
        if (found.isEmpty()) throw ApiException.badRequest(error)
        return found
    }

    private fun forwarded(source: JsonObject, target: ChatState, ctx: CallContext): JsonObject {
        val origin = buildJsonObject {
            put("type", "user")
            put("date", source.long("date") ?: 0L)
            put("sender_user", source["from"] ?: ctx.bot.user)
        }
        return source
            .without("message_id", "date", "reply_to_message", "reply_markup", "edit_date")
            .with("chat" to target.toJson(), "forward_origin" to origin)
    }

    private fun copied(source: JsonObject, target: ChatState, ctx: CallContext): JsonObject {
        var copy = source
            .without("message_id", "date", "reply_to_message", "edit_date", "forward_origin", "reply_markup")
            .with("chat" to target.toJson(), "from" to ctx.bot.user)
        (ctx.params["reply_markup"] as? JsonObject)?.let { copy = copy.with("reply_markup" to it) }
        if (ctx.params.str("caption") != null && Messages.hasMedia(source)) {
            val caption = Messages.formatText(
                ctx.core.config.textLength,
                ctx.params,
                "caption",
                "caption_entities",
                MAX_CAPTION,
                null,
                "message caption is too long",
            )
            copy = copy.without("caption", "caption_entities").with("caption" to JsonPrimitive(caption.text))
            if (caption.entities.isNotEmpty()) copy = copy.with("caption_entities" to JsonArray(caption.entities))
        }
        return copy
    }

    private fun requireEditable(message: JsonObject) {
        if ((message["from"] as? JsonObject)?.bool("is_bot") !=
            true
        ) throw ApiException.badRequest("message can't be edited")
    }

    private const val MAX_TEXT = 4096
    private const val MAX_CAPTION = 1024
}

private fun JsonObject.entityList(key: String): List<JsonObject> = (this[key] as? JsonArray).orEmpty().mapNotNull {
    it as? JsonObject
}

internal fun JsonElement?.orEmptyObject(): JsonObject = this as? JsonObject ?: JsonObject(emptyMap())
