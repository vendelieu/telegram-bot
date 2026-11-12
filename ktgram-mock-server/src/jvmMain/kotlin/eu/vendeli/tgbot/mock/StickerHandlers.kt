package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val MAX_STICKERS_PER_SET = 120
private const val MAX_INITIAL_STICKERS = 50
private const val STICKER_SIZE = 512L
private const val MAX_KEYWORDS = 20
private const val MAX_KEYWORD_CHARS = 64
private const val INVALID_SET = "STICKERSET_INVALID"
private val SET_NAME = Regex("^[A-Za-z][A-Za-z0-9_]{0,63}$")
private val STICKER_TYPES = setOf("regular", "mask", "custom_emoji")

/** Sticker sets: create, read, change and delete, with the limits and errors of the Bot API. */
internal object StickerHandlers {
    private val CallContext.sets get() = bot.extra.stickerSets

    private fun CallContext.set(key: String = "name"): JsonObject {
        val name = params.str(key).orEmpty()
        return sets[name] ?: throw ApiException.badRequest(INVALID_SET)
    }

    private fun stickers(set: JsonObject): List<JsonObject> = (set["stickers"] as? JsonArray).orEmpty().map {
        it as JsonObject
    }

    private fun withStickers(set: JsonObject, list: List<JsonObject>) = set.with("stickers" to JsonArray(list))

    private fun CallContext.sticker(input: JsonObject, setName: String, type: String): JsonObject {
        val file = Messages.stickerFile(this, input.str("sticker"))
        val format = input.str("format")
        val overrides = buildMap<String, JsonElement> {
            put("file_id", JsonPrimitive(file.fileId))
            put("file_unique_id", JsonPrimitive(file.uniqueId))
            put("type", JsonPrimitive(type))
            put("set_name", JsonPrimitive(setName))
            put("width", JsonPrimitive(STICKER_SIZE))
            put("height", JsonPrimitive(STICKER_SIZE))
            put("is_animated", JsonPrimitive(format == "animated"))
            put("is_video", JsonPrimitive(format == "video"))
            ((input["emoji_list"] as? JsonArray)?.firstOrNull() as? JsonPrimitive)?.let { put("emoji", it) }
            input["mask_position"]?.let { put("mask_position", it) }
            if (type == "custom_emoji") put("custom_emoji_id", JsonPrimitive("ce_${file.uniqueId}"))
        }
        return generator.generateObject("Sticker", overrides = overrides)!!
    }

    private fun CallContext.requireNewSticker(): JsonObject =
        params["sticker"] as? JsonObject ?: throw ApiException.badRequest("sticker is empty")

    val create: Handler = {
        val name = params.str("name").orEmpty()
        val suffix = "_by_${bot.username}"
        val valid = SET_NAME.matches(name) && name.endsWith(suffix, ignoreCase = true) && !name.contains("__")
        if (!valid) throw ApiException.badRequest("invalid sticker set name is specified")
        if (sets.containsKey(name)) throw ApiException.badRequest("sticker set name is already occupied")
        val type = params.str("sticker_type") ?: "regular"
        if (type !in STICKER_TYPES) throw ApiException.badRequest("unsupported sticker type specified")
        val inputs = (params["stickers"] as? JsonArray).orEmpty().map { it as JsonObject }
        if (inputs.isEmpty() || inputs.size > MAX_INITIAL_STICKERS) throw ApiException.badRequest("STICKERS_TOO_MUCH")
        val created = buildJsonObject {
            put("name", name)
            put("title", params.str("title").orEmpty())
            put("sticker_type", type)
            put("stickers", JsonArray(inputs.map { sticker(it, name, type) }))
        }
        sets[name] = created
        TRUE
    }

    val get: Handler = { set() }

    val add: Handler = {
        val set = set()
        val current = stickers(set)
        if (current.size >= MAX_STICKERS_PER_SET) throw ApiException.badRequest("STICKERS_TOO_MUCH")
        val added = sticker(requireNewSticker(), set.str("name")!!, set.str("sticker_type")!!)
        sets[set.str("name")!!] = withStickers(set, current + added)
        TRUE
    }

    private fun CallContext.locate(): Pair<JsonObject, Int> {
        val fileId = params.str("sticker").orEmpty()
        for (set in sets.values) {
            val index = stickers(set).indexOfFirst { it.str("file_id") == fileId }
            if (index >= 0) return set to index
        }
        throw ApiException.badRequest("STICKER_INVALID")
    }

    private fun CallContext.change(transform: (List<JsonObject>, Int) -> List<JsonObject>): JsonElement {
        val (set, index) = locate()
        sets[set.str("name")!!] = withStickers(set, transform(stickers(set), index))
        return TRUE
    }

    val position: Handler = {
        val target = params.long("position")?.toInt() ?: throw ApiException.badRequest("position is empty")
        change { list, index ->
            if (target !in list.indices) throw ApiException.badRequest("STICKER_POSITION_INVALID")
            list.toMutableList().also { it.add(target, it.removeAt(index)) }
        }
    }

    val delete: Handler = { change { list, index -> list.filterIndexed { i, _ -> i != index } } }

    val emojiList: Handler = {
        val emoji = ((params["emoji_list"] as? JsonArray)?.firstOrNull() as? JsonPrimitive)
            ?: throw ApiException.badRequest("EMOJI_INVALID")
        change { list, index -> list.mapIndexed { i, s -> if (i == index) s.with("emoji" to emoji) else s } }
    }

    val keywords: Handler = {
        val (set, index) = locate()
        if (set.str("sticker_type") == "mask") throw ApiException.badRequest("STICKER_TYPE_INVALID")
        val words = (params["keywords"] as? JsonArray).orEmpty().map { (it as JsonPrimitive).content }
        if (words.size > MAX_KEYWORDS || words.sumOf { it.length } > MAX_KEYWORD_CHARS) {
            throw ApiException.badRequest("KEYWORDS_INVALID")
        }
        val id = stickers(set)[index].str("file_id")!!
        if (words.isEmpty()) bot.extra.stickerKeywords.remove(id) else bot.extra.stickerKeywords[id] = words
        TRUE
    }

    val maskPosition: Handler = {
        val (set, _) = locate()
        if (set.str("sticker_type") != "mask") throw ApiException.badRequest("STICKER_MASK_COORDS_NOT_SUPPORTED")
        val position = params["mask_position"] as? JsonObject
        change { list, index ->
            list.mapIndexed { i, s ->
                if (i != index) s else position?.let { s.with("mask_position" to it) } ?: s.without("mask_position")
            }
        }
    }

    val replace: Handler = {
        val set = set()
        val oldId = params.str("old_sticker").orEmpty()
        val list = stickers(set)
        val index = list.indexOfFirst { it.str("file_id") == oldId }
        if (index < 0) throw ApiException.badRequest("STICKER_INVALID")
        val fresh = sticker(requireNewSticker(), set.str("name")!!, set.str("sticker_type")!!)
        sets[set.str("name")!!] = withStickers(set, list.mapIndexed { i, s -> if (i == index) fresh else s })
        TRUE
    }

    val title: Handler = {
        val set = set()
        val title = params.str("title").orEmpty()
        if (title.isEmpty()) throw ApiException.badRequest("sticker set title is empty")
        sets[set.str("name")!!] = set.with("title" to JsonPrimitive(title))
        TRUE
    }

    val thumbnail: Handler = {
        val set = set()
        val updated = params.str("thumbnail")?.let {
            val file = Messages.stickerFile(this, it)
            set.with(
                "thumbnail" to
                    generator.generateObject(
                        "PhotoSize",
                        overrides = mapOf(
                            "file_id" to JsonPrimitive(file.fileId),
                            "file_unique_id" to JsonPrimitive(file.uniqueId),
                        ),
                    )!!,
            )
        } ?: set.without("thumbnail")
        sets[set.str("name")!!] = updated
        TRUE
    }

    val customEmojiThumbnail: Handler = {
        val set = set()
        if (set.str("sticker_type") != "custom_emoji") throw ApiException.badRequest("STICKERSET_INVALID")
        TRUE
    }

    val deleteSet: Handler = {
        val set = set()
        sets.remove(set.str("name"))
        TRUE
    }

    val customEmoji: Handler = {
        val ids = (params["custom_emoji_ids"] as? JsonArray).orEmpty().map { (it as JsonPrimitive).content }.toSet()
        JsonArray(sets.values.flatMap(::stickers).filter { it.str("custom_emoji_id") in ids })
    }

    val upload: Handler = {
        val file = Messages.stickerFile(this, params.str("sticker"))
        buildJsonObject {
            put("file_id", file.fileId)
            put("file_unique_id", file.uniqueId)
            file.bytes?.let { put("file_size", it.size) }
            put("file_path", file.path)
        }
    }
}
