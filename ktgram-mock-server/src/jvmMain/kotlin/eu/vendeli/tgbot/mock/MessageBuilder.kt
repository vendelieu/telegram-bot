package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val MAX_TEXT = 4096
private const val MAX_CAPTION = 1024
private const val MAX_POLL_QUESTION = 300
private const val MIN_POLL_OPTIONS = 2
private const val MAX_POLL_OPTIONS = 12
private const val STICKER_SIZE = 512L
private val FILE_NAMED = setOf("document", "audio", "video", "animation")
private const val MIN_ALBUM = 2
private const val MAX_ALBUM = 10

/** A media-carrying message field: the Message key, the spec type, whether it is an array and the request param. */
internal class MediaKind(
    val field: String,
    val type: String,
    val dir: String,
    val ext: String,
    val isArray: Boolean = false,
)

private val MEDIA_BY_PARAM = mapOf(
    "photo" to MediaKind("photo", "PhotoSize", "photos", "jpg", isArray = true),
    "audio" to MediaKind("audio", "Audio", "music", "mp3"),
    "document" to MediaKind("document", "Document", "documents", "bin"),
    "video" to MediaKind("video", "Video", "videos", "mp4"),
    "animation" to MediaKind("animation", "Animation", "animations", "mp4"),
    "voice" to MediaKind("voice", "Voice", "voice", "oga"),
    "video_note" to MediaKind("video_note", "VideoNote", "video_notes", "mp4"),
    "sticker" to MediaKind("sticker", "Sticker", "stickers", "webp"),
)

private val MEDIA_FIELDS =
    MEDIA_BY_PARAM.values.map { it.field }.toSet() +
        setOf("location", "venue", "contact", "dice", "poll", "invoice", "checklist", "game", "paid_media")
private val DICE_MAX = mapOf("🎲" to 6, "🎯" to 6, "🏀" to 5, "⚽" to 5, "🎳" to 6, "🎰" to 64)

/** Builds the `Message` objects the stateful handlers store and return. */
internal object Messages {
    /** Everything that decides how a message is built, minus the id and date which [store] assigns. */
    fun build(ctx: CallContext, chat: ChatState): JsonObject {
        val params = ctx.params
        val fields = LinkedHashMap<String, JsonElement>()
        fields["from"] = ctx.bot.user
        fields["chat"] = chat.toJson()
        params.str("business_connection_id")?.let { fields["business_connection_id"] = JsonPrimitive(it) }
        ChatAdminHandlers.requireOpenTopic(chat, params.long("message_thread_id"))
        params.long("message_thread_id")?.let {
            fields["message_thread_id"] = JsonPrimitive(it)
            fields["is_topic_message"] = JsonPrimitive(true)
        }
        if (params.bool("protect_content") == true) fields["has_protected_content"] = JsonPrimitive(true)
        replyTo(ctx, chat)?.let { fields["reply_to_message"] = it }
        (params["reply_markup"] as? JsonObject)?.takeIf { "inline_keyboard" in it }?.let { fields["reply_markup"] = it }
        (params["link_preview_options"] as? JsonObject)?.let { fields["link_preview_options"] = it }

        val name = ctx.method.name.lowercase()
        if (name == "sendmessage") {
            val text =
                formatText(
                    ctx.core.config.textLength,
                    params,
                    "text",
                    "entities",
                    MAX_TEXT,
                    "message text is empty",
                    "message is too long",
                )
            fields["text"] = JsonPrimitive(text.text)
            if (text.entities.isNotEmpty()) fields["entities"] = JsonArray(text.entities)
        } else {
            fields += media(ctx, name)
            if (params.str("caption") != null) {
                val caption =
                    formatText(
                        ctx.core.config.textLength,
                        params,
                        "caption",
                        "caption_entities",
                        MAX_CAPTION,
                        null,
                        "message caption is too long",
                    )
                fields["caption"] = JsonPrimitive(caption.text)
                if (caption.entities.isNotEmpty()) fields["caption_entities"] = JsonArray(caption.entities)
                if (params.bool("show_caption_above_media") ==
                    true
                ) fields["show_caption_above_media"] = JsonPrimitive(true)
            }
        }
        return JsonObject(fields)
    }

    fun store(chat: ChatState, message: JsonObject, nowSeconds: Long): JsonObject {
        val id = chat.nextMessageId()
        val stored = JsonObject(
            linkedMapOf<String, JsonElement>("message_id" to JsonPrimitive(id), "date" to JsonPrimitive(nowSeconds)) +
                message,
        )
        chat.messages[id] = stored
        return stored
    }

    fun formatText(
        unit: TextLengthUnit,
        params: JsonObject,
        textKey: String,
        entitiesKey: String,
        max: Int,
        emptyError: String?,
        tooLongError: String,
    ): FormattedText {
        val raw = params.str(textKey).orEmpty()
        val formatted = TextFormatter.format(
            raw,
            params.str("parse_mode"),
            TextFormatter.entitiesFrom(params[entitiesKey]),
        )
        if (emptyError != null && formatted.text.isBlank()) throw ApiException.badRequest(emptyError)
        if (unit.length(formatted.text) > max) throw ApiException.badRequest(tooLongError)
        return formatted
    }

    private fun replyTo(ctx: CallContext, chat: ChatState): JsonObject? {
        val rp = ctx.params["reply_parameters"] as? JsonObject
        val id = rp?.long("message_id") ?: ctx.params.long("reply_to_message_id") ?: return null
        val source = rp?.long("chat_id")?.let { ctx.bot.chats[it] } ?: chat
        val target = source.messages[id]
        val lenient =
            rp?.bool("allow_sending_without_reply") == true || ctx.params.bool("allow_sending_without_reply") == true
        if (target == null && !lenient) throw ApiException.badRequest("message to be replied not found")
        return target?.without("reply_to_message")
    }

    private fun media(ctx: CallContext, method: String): Map<String, JsonElement> {
        val params = ctx.params
        val kindParam = method.removePrefix("send").let { if (it == "videonote") "video_note" else it }
        MEDIA_BY_PARAM[kindParam]?.let { kind ->
            val param = kindParam
            return mapOf(kind.field to file(ctx, kind, param, params[param], params))
        }
        return when (method) {
            "sendlocation" -> {
                mapOf(
                    "location" to
                        overlay(
                            ctx,
                            "Location",
                            params,
                            mapOf(
                                "latitude" to params.getValue("latitude"),
                                "longitude" to params.getValue("longitude"),
                            ),
                        ),
                )
            }

            "sendvenue" -> {
                val location = overlay(
                    ctx,
                    "Location",
                    params,
                    mapOf(
                        "latitude" to params.getValue("latitude"),
                        "longitude" to params.getValue("longitude"),
                    ),
                )
                mapOf("venue" to overlay(ctx, "Venue", params, mapOf("location" to location)), "location" to location)
            }

            "sendcontact" -> {
                val phone = params.str("phone_number").orEmpty().replace(Regex("[\\s()\\-]"), "")
                mapOf("contact" to overlay(ctx, "Contact", params, mapOf("phone_number" to JsonPrimitive(phone))))
            }

            "sendpaidmedia" -> {
                mapOf("paid_media" to paidMedia(ctx))
            }

            "senddice" -> {
                mapOf("dice" to dice(ctx))
            }

            "sendpoll" -> {
                mapOf("poll" to poll(ctx))
            }

            "sendinvoice" -> {
                mapOf("invoice" to invoice(ctx))
            }

            "sendchecklist" -> {
                mapOf("checklist" to checklist(params["checklist"] as? JsonObject))
            }

            "sendgame" -> {
                mapOf("game" to game(ctx))
            }

            else -> {
                emptyMap()
            }
        }
    }

    /** Copies request parameters that are fields of [type] onto a generated sample of it. */
    private fun overlay(
        ctx: CallContext,
        type: String,
        source: JsonObject,
        extra: Map<String, JsonElement>,
    ): JsonObject {
        val known = ctx.core.spec.types[type]
            ?.fieldsByName
            .orEmpty()
        val copied = source.filterKeys { it in known }.filterValues { it is JsonPrimitive || it is JsonObject }
        return ctx.generator.generateObject(type, overrides = copied + extra) ?: JsonObject(copied + extra)
    }

    private fun dice(ctx: CallContext): JsonObject {
        val emoji = ctx.params.str("emoji") ?: "🎲"
        val max = DICE_MAX[emoji] ?: throw ApiException.badRequest("invalid dice emoji specified")
        val next = (
            ctx.bot.chats[ctx.params.str("chat_id")?.toLongOrNull()]
                ?.messages
                ?.size ?: 0
        ) % max + 1
        return buildJsonObject {
            put("emoji", emoji)
            put("value", next)
        }
    }

    private fun poll(ctx: CallContext): JsonObject {
        val params = ctx.params
        val question = params.str("question").orEmpty()
        if (question.isEmpty() || question.codePointCount(0, question.length) > MAX_POLL_QUESTION) {
            throw ApiException.badRequest("poll question must be non-empty and at most $MAX_POLL_QUESTION characters")
        }
        val options = (params["options"] as? JsonArray).orEmpty().map {
            val text = (it as? JsonObject)?.str("text") ?: (it as? JsonPrimitive)?.content.orEmpty()
            ctx.generator.generateObject(
                "PollOption",
                overrides = mapOf("text" to JsonPrimitive(text), "voter_count" to JsonPrimitive(0)),
            )!!
        }
        if (options.size <
            MIN_POLL_OPTIONS
        ) throw ApiException.badRequest("poll must have at least $MIN_POLL_OPTIONS options")
        if (options.size >
            MAX_POLL_OPTIONS
        ) throw ApiException.badRequest("poll can't have more than $MAX_POLL_OPTIONS options")
        val extra = buildMap<String, JsonElement> {
            put("id", JsonPrimitive(ctx.bot.nextPollId()))
            put("question", JsonPrimitive(question))
            put("options", JsonArray(options))
            put("total_voter_count", JsonPrimitive(0))
            put("is_closed", JsonPrimitive(false))
            put("is_anonymous", JsonPrimitive(params.bool("is_anonymous") ?: true))
            put("type", JsonPrimitive(params.str("type") ?: "regular"))
            put("allows_multiple_answers", JsonPrimitive(params.bool("allows_multiple_answers") ?: false))
            params["correct_option_id"]?.let { put("correct_option_id", it) }
            params["correct_option_ids"]?.let { put("correct_option_ids", it) }
            params["explanation"]?.let { put("explanation", it) }
            params["open_period"]?.let { put("open_period", it) }
            params["close_date"]?.let { put("close_date", it) }
        }
        return ctx.generator.generateObject("Poll", overrides = extra) ?: JsonObject(extra)
    }

    /** Builds a Checklist from an InputChecklist (both share title and tasks with id and text). */
    fun checklist(input: JsonObject?): JsonObject {
        val source = input ?: throw ApiException.badRequest("checklist is empty")
        val tasks = (source["tasks"] as? JsonArray).orEmpty().map { it as? JsonObject ?: JsonObject(emptyMap()) }
        if (tasks.isEmpty()) throw ApiException.badRequest("checklist must have at least one task")
        return JsonObject(
            mapOf(
                "title" to (source["title"] ?: JsonPrimitive("")),
                "tasks" to
                    JsonArray(
                        tasks.map {
                            JsonObject(
                                mapOf(
                                    "id" to (it["id"] ?: JsonPrimitive(0)),
                                    "text" to (it["text"] ?: JsonPrimitive("")),
                                ),
                            )
                        },
                    ),
            ),
        )
    }

    private fun game(ctx: CallContext): JsonObject {
        val name = ctx.params.str("game_short_name").orEmpty()
        val registered = ctx.bot.extra.games[name] ?: throw ApiException.badRequest("GAME_SHORT_NAME_INVALID")
        return JsonObject(
            mapOf(
                "title" to (registered["title"] ?: JsonPrimitive(name)),
                "description" to (registered["description"] ?: JsonPrimitive("")),
                "photo" to JsonArray(emptyList()),
            ),
        )
    }

    private fun paidMedia(ctx: CallContext): JsonObject {
        val items = (ctx.params["media"] as? JsonArray).orEmpty().mapNotNull { it as? JsonObject }
        if (items.isEmpty()) throw ApiException.badRequest("media is empty")
        val list = items.map { item ->
            val type = item.str("type").orEmpty()
            val kind = MEDIA_BY_PARAM[type] ?: throw ApiException.badRequest("unsupported paid media type")
            JsonObject(
                mapOf("type" to JsonPrimitive(type), kind.field to file(ctx, kind, "media", item["media"], item)),
            )
        }
        return JsonObject(
            mapOf(
                "star_count" to (ctx.params["star_count"] ?: JsonPrimitive(0)),
                "paid_media" to JsonArray(list),
            ),
        )
    }

    private fun invoice(ctx: CallContext): JsonObject {
        val total = (ctx.params["prices"] as? JsonArray).orEmpty().sumOf { (it as? JsonObject)?.long("amount") ?: 0L }
        return overlay(
            ctx,
            "Invoice",
            ctx.params,
            mapOf(
                "total_amount" to JsonPrimitive(total),
                "start_parameter" to JsonPrimitive(ctx.params.str("start_parameter").orEmpty()),
            ),
        )
    }

    /** Resolves [value] (file_id, url or `attach://`) into a stored file and wraps it as the typed Message field. */
    private fun file(
        ctx: CallContext,
        kind: MediaKind,
        param: String,
        value: JsonElement?,
        source: JsonObject,
    ): JsonElement {
        val stored = resolveFile(ctx, (value as? JsonPrimitive)?.content, kind)
        val overrides = mapOf(
            "file_id" to JsonPrimitive(stored.fileId),
            "file_unique_id" to JsonPrimitive(stored.uniqueId),
        ) + (stored.bytes?.let { mapOf("file_size" to JsonPrimitive(it.size.toLong())) } ?: emptyMap()) +
            (
                stored.name
                    ?.takeIf {
                        kind.field in FILE_NAMED
                    }?.let { mapOf("file_name" to JsonPrimitive(it)) }
                    ?: emptyMap()
            ) +
            (stored.mimeType?.let { mapOf("mime_type" to JsonPrimitive(it)) } ?: emptyMap())
        val known = ctx.core.spec.types[kind.type]
            ?.fieldsByName
            .orEmpty()
        if (kind.field == "sticker") {
            storedSticker(ctx, stored.fileId)?.let { return it }
        }
        val primitives = setOf<TypeRef>(TypeRef.Str, TypeRef.Int, TypeRef.Float, TypeRef.Bool)
        val fromRequest = source
            .filterKeys { name ->
                name in known && name != param && name !in overrides &&
                    known.getValue(name).types.all { it in primitives }
            }.filterValues { it is JsonPrimitive && !(it.isString && it.content.startsWith("attach://")) }
        val typed = if (kind.field == "sticker") {
            mapOf(
                "type" to JsonPrimitive("regular"),
                "width" to JsonPrimitive(STICKER_SIZE),
                "height" to JsonPrimitive(STICKER_SIZE),
            )
        } else {
            emptyMap()
        }
        val obj = ctx.generator.generateObject(
            kind.type,
            overrides = typed + fromRequest + overrides + photoFields(ctx, known, source),
        ) ?: JsonObject(overrides)
        return if (kind.isArray) JsonArray(listOf(obj)) else obj
    }

    /** Thumbnails and covers are PhotoSize (or an array of them) in the result while the request carries files. */
    private fun photoFields(
        ctx: CallContext,
        known: Map<String, FieldSpec>,
        source: JsonObject,
    ): Map<String, JsonElement> {
        val photoSize = TypeRef.Obj("PhotoSize")
        val result = LinkedHashMap<String, JsonElement>()
        for ((name, field) in known) {
            val ref = (source[name] as? JsonPrimitive)?.takeIf { it.isString }?.content ?: continue
            val type = field.types.first()
            if (type != photoSize && type != TypeRef.Arr(photoSize)) continue
            val file = resolveFile(ctx, ref, MEDIA_BY_PARAM.getValue("photo"))
            val dimensions = file.bytes
                ?.let(::imageSize)
                ?.let { (w, h) -> mapOf("width" to JsonPrimitive(w), "height" to JsonPrimitive(h)) }
                .orEmpty()
            val size = ctx.generator.generateObject(
                "PhotoSize",
                overrides = mapOf(
                    "file_id" to JsonPrimitive(file.fileId),
                    "file_unique_id" to JsonPrimitive(file.uniqueId),
                ) + dimensions,
            )!!
            result[name] = if (type == photoSize) size else JsonArray(listOf(size))
        }
        return result
    }

    private fun imageSize(bytes: ByteArray): Pair<Int, Int>? = runCatching {
        javax.imageio.ImageIO
            .read(java.io.ByteArrayInputStream(bytes))
            ?.let { it.width to it.height }
    }.getOrNull()

    private fun storedSticker(ctx: CallContext, fileId: String): JsonObject? = ctx.bot.extra.stickerSets.values
        .flatMap { (it["stickers"] as? JsonArray).orEmpty() }
        .firstOrNull { (it as? JsonObject)?.str("file_id") == fileId } as? JsonObject

    fun mediaGroup(ctx: CallContext, chat: ChatState, nowSeconds: Long): JsonArray {
        val items = (ctx.params["media"] as? JsonArray).orEmpty().mapNotNull { it as? JsonObject }
        if (items.size !in
            MIN_ALBUM..MAX_ALBUM
        ) throw ApiException.badRequest("media group must contain $MIN_ALBUM-$MAX_ALBUM items")
        val groupId = ctx.bot.nextMediaGroupId()
        val messages = items.map { item ->
            val kind =
                MEDIA_BY_PARAM[item.str("type")] ?: throw ApiException.badRequest("unsupported media type in the group")
            val fields = LinkedHashMap<String, JsonElement>()
            fields["from"] = ctx.bot.user
            fields["chat"] = chat.toJson()
            fields["media_group_id"] = JsonPrimitive(groupId)
            fields[kind.field] = file(ctx, kind, "media", item["media"], item)
            if (item.str("caption") != null) {
                val caption =
                    formatText(
                        ctx.core.config.textLength,
                        item,
                        "caption",
                        "caption_entities",
                        MAX_CAPTION,
                        null,
                        "message caption is too long",
                    )
                fields["caption"] = JsonPrimitive(caption.text)
                if (caption.entities.isNotEmpty()) fields["caption_entities"] = JsonArray(caption.entities)
            }
            store(chat, JsonObject(fields), nowSeconds)
        }
        return JsonArray(messages)
    }

    /** Swaps the media of a stored message (editMessageMedia). */
    fun replaceMedia(ctx: CallContext, existing: JsonObject, input: JsonObject): JsonObject {
        val kind = MEDIA_BY_PARAM[input.str("type")] ?: throw ApiException.badRequest("unsupported media type")
        val base = existing.without(*MEDIA_FIELDS.toTypedArray(), "caption", "caption_entities")
        val fields = LinkedHashMap<String, JsonElement>(base)
        fields[kind.field] = file(ctx, kind, "media", input["media"], input)
        if (input.str("caption") != null) {
            val caption =
                formatText(
                    ctx.core.config.textLength,
                    input,
                    "caption",
                    "caption_entities",
                    MAX_CAPTION,
                    null,
                    "message caption is too long",
                )
            fields["caption"] = JsonPrimitive(caption.text)
            if (caption.entities.isNotEmpty()) fields["caption_entities"] = JsonArray(caption.entities)
        }
        return JsonObject(fields)
    }

    fun stickerFile(ctx: CallContext, value: String?): StoredFile =
        resolveFile(ctx, value, MEDIA_BY_PARAM.getValue("sticker"))

    fun hasMedia(message: JsonObject) = MEDIA_FIELDS.any { it in message && it != "poll" && it != "checklist" }

    fun resolveFile(ctx: CallContext, value: String?, kind: MediaKind): StoredFile {
        val bot = ctx.bot
        val uploads = ctx.uploads
        val ref = value ?: throw ApiException.badRequest("there is no ${kind.field} in the request")
        if (ref.startsWith("attach://")) {
            val upload =
                uploads[ref.removePrefix("attach://")]
                    ?: throw ApiException.badRequest("wrong file identifier/HTTP URL specified")
            return register(bot, kind, upload.fileName, upload.contentType, upload.bytes)
        }
        bot.files[ref]?.let { return it }
        if (ref.startsWith("http://") || ref.startsWith("https://")) {
            val content = ctx.core.fetchUrl(ref)
            return register(bot, kind, ref.substringAfterLast('/'), content?.second, content?.first)
        }
        throw ApiException.badRequest("wrong file identifier/HTTP URL specified")
    }

    private fun register(bot: BotState, kind: MediaKind, name: String?, mime: String?, bytes: ByteArray?): StoredFile {
        val n = bot.fileCounter.incrementAndGet()
        val ext = name?.substringAfterLast('.', "")?.takeIf { it.isNotEmpty() && it.length <= 5 } ?: kind.ext
        val fileName = name?.takeIf { it.isNotBlank() } ?: "file_$n.$ext"
        val file =
            StoredFile("mock_${kind.field}_$n", "mock_unique_$n", "${kind.dir}/file_$n.$ext", fileName, mime, bytes)
        bot.files[file.fileId] = file
        return file
    }
}
