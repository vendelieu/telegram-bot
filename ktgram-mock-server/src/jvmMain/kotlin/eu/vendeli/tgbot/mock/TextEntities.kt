package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/** Text with the entities (UTF-16 offsets, like the Bot API) that formatting produced. */
data class FormattedText(
    val text: String,
    val entities: List<JsonObject>,
)

internal fun entity(type: String, offset: Int, length: Int, extra: Map<String, JsonElement> = emptyMap()) =
    JsonObject(
        mapOf<String, JsonElement>(
            "type" to JsonPrimitive(type),
            "offset" to JsonPrimitive(offset),
            "length" to JsonPrimitive(length),
        ) + extra,
    )

private fun parseError(reason: String) = ApiException.badRequest("can't parse entities: $reason")

private fun String.utf8Offset(index: Int) = substring(0, index.coerceIn(0, length)).toByteArray().size

/** Parses `parse_mode` markup the way the Bot API does and rejects invalid markup with its error messages. */
object TextFormatter {
    const val HTML = "HTML"
    const val MARKDOWN = "Markdown"
    const val MARKDOWN_V2 = "MarkdownV2"

    fun format(source: String, parseMode: String?, explicitEntities: List<JsonObject>?): FormattedText {
        if (explicitEntities != null) return FormattedText(source, explicitEntities).withAutoEntities()
        return when (parseMode) {
            null -> FormattedText(source, emptyList())
            HTML -> HtmlParser(source).parse()
            MARKDOWN -> LegacyMarkdownParser(source).parse()
            MARKDOWN_V2 -> MarkdownV2Parser(source).parse()
            else -> throw ApiException.badRequest("unsupported parse_mode")
        }.withAutoEntities()
    }

    fun entitiesFrom(element: JsonElement?): List<JsonObject>? =
        (element as? JsonArray)?.mapNotNull { it as? JsonObject }
}

private class OpenEntity(
    val type: String,
    val tag: String,
    val start: Int,
    val sourceIndex: Int,
    val extra: Map<String, JsonElement> = emptyMap(),
)

private class HtmlParser(
    private val src: String,
) {
    private val out = StringBuilder()
    private val done = mutableListOf<JsonObject>()
    private val stack = ArrayDeque<OpenEntity>()

    fun parse(): FormattedText {
        var i = 0
        while (i < src.length) {
            val c = src[i]
            i = when (c) {
                '<' -> {
                    readTag(i)
                }

                '&' -> {
                    readReference(i)
                }

                else -> {
                    out.append(c)
                    i + 1
                }
            }
        }
        stack.lastOrNull()?.let {
            throw parseError("Can't find end tag corresponding to start tag \"${it.tag}\"")
        }
        return FormattedText(out.toString(), done.sortedBy { it.long("offset") })
    }

    private fun readReference(i: Int): Int {
        val end = src.indexOf(';', i)
        if (end == -1 || end - i > MAX_REFERENCE) {
            out.append('&')
            return i + 1
        }
        val name = src.substring(i + 1, end)
        val replacement = when {
            name == "lt" -> "<"
            name == "gt" -> ">"
            name == "amp" -> "&"
            name == "quot" -> "\""
            name.startsWith("#x") -> name.drop(2).toIntOrNull(HEX)?.let { String(Character.toChars(it)) }
            name.startsWith("#") -> name.drop(1).toIntOrNull()?.let { String(Character.toChars(it)) }
            else -> null
        }
        if (replacement == null) {
            out.append('&')
            return i + 1
        }
        out.append(replacement)
        return end + 1
    }

    private fun readTag(i: Int): Int {
        val end = src.indexOf('>', i)
        val next = src.getOrNull(i + 1)
        if (end == -1 || next == null || !(next.isLetter() || next == '/')) {
            throw parseError(
                "Unsupported start tag \"${src.drop(
                    i + 1,
                ).takeWhile { !it.isWhitespace() }}\" at byte offset ${src.utf8Offset(i)}",
            )
        }
        val body = src.substring(i + 1, end).trim()
        if (body.startsWith("/")) closeTag(body.drop(1).trim().lowercase(), i) else openTag(body, i)
        return end + 1
    }

    private fun openTag(body: String, at: Int) {
        val name = body.takeWhile { !it.isWhitespace() }.lowercase()
        val attrs = ATTRIBUTE.findAll(body.drop(name.length)).associate {
            it.groupValues[1].lowercase() to (it.groupValues[2].ifEmpty { it.groupValues[3] })
        }
        val (type, extra) = when (name) {
            "b", "strong" -> "bold" to emptyMap()

            "i", "em" -> "italic" to emptyMap()

            "u", "ins" -> "underline" to emptyMap()

            "s", "strike", "del" -> "strikethrough" to emptyMap()

            "tg-spoiler" -> "spoiler" to emptyMap()

            "span" -> if (attrs["class"] == "tg-spoiler") "spoiler" to emptyMap() else unsupported(name, at)

            "code" -> "code" to emptyMap()

            "pre" -> "pre" to emptyMap()

            "blockquote" -> (if ("expandable" in body) "expandable_blockquote" else "blockquote") to emptyMap()

            "tg-emoji" -> "custom_emoji" to mapOf("custom_emoji_id" to JsonPrimitive(attrs["emoji-id"].orEmpty()))

            "a" -> linkEntity(
                attrs["href"] ?: throw parseError("Unsupported start tag \"a\" at byte offset ${src.utf8Offset(at)}"),
            )

            else -> unsupported(name, at)
        }
        stack.addLast(OpenEntity(type, name, out.length, at, extra))
    }

    private fun linkEntity(href: String): Pair<String, Map<String, JsonElement>> {
        val userId = Regex("^tg://user\\?id=(\\d+)$")
            .matchEntire(href)
            ?.groupValues
            ?.get(1)
            ?.toLong()
        return if (userId != null) {
            "text_mention" to mapOf("user" to buildJsonObject { put("id", userId) })
        } else {
            "text_link" to mapOf("url" to JsonPrimitive(href))
        }
    }

    private fun unsupported(name: String, at: Int): Nothing =
        throw parseError("Unsupported start tag \"$name\" at byte offset ${src.utf8Offset(at)}")

    private fun closeTag(name: String, at: Int) {
        val open = stack.lastOrNull()
            ?: throw parseError("Unexpected end tag at byte offset ${src.utf8Offset(at)}")
        if (open.tag != name) {
            throw parseError(
                "Unmatched end tag at byte offset ${src.utf8Offset(
                    at,
                )}, expected \"</${open.tag}>\", found \"</$name>\"",
            )
        }
        stack.removeLast()
        val length = out.length - open.start
        if (length <= 0) return
        val parentPre = stack.lastOrNull()?.takeIf { it.tag == "pre" && it.start == open.start && open.tag == "code" }
        val language = Regex("language-([\\w+#-]+)").find(src.substring(open.sourceIndex, at.coerceAtMost(src.length)))
        if (parentPre != null) {
            // <pre><code class="language-x"> collapses into a single pre entity, the code entity is dropped.
            stack.removeLast()
            stack.addLast(
                OpenEntity(
                    "pre",
                    "pre",
                    parentPre.start,
                    parentPre.sourceIndex,
                    language?.let { mapOf("language" to JsonPrimitive(it.groupValues[1])) }.orEmpty(),
                ),
            )
            return
        }
        done += entity(open.type, open.start, length, open.extra)
    }

    private companion object {
        const val MAX_REFERENCE = 10
        const val HEX = 16
        val ATTRIBUTE = Regex("([\\w:-]+)\\s*=\\s*(?:\"([^\"]*)\"|'([^']*)')")
    }
}

private class MarkdownV2Parser(
    private val src: String,
) {
    private val out = StringBuilder()
    private val done = mutableListOf<JsonObject>()
    private val stack = ArrayDeque<OpenEntity>()

    fun parse(): FormattedText {
        var i = 0
        while (i < src.length) {
            val c = src[i]
            val atLineStart = i == 0 || src[i - 1] == '\n'
            val consumed = when {
                c == '\\' -> {
                    escape(i)
                }

                c == '`' -> {
                    code(i)
                }

                c == '[' -> {
                    link(i)
                }

                (c == '>' || src.startsWith("**>", i)) && atLineStart -> {
                    blockquote(i)
                }

                c == '|' && src.startsWith("||", i) -> {
                    toggle("spoiler", "||", i, 2)
                }

                c == '_' && src.startsWith("__", i) -> {
                    toggle("underline", "__", i, 2)
                }

                c == '_' -> {
                    toggle("italic", "_", i, 1)
                }

                c == '*' -> {
                    toggle("bold", "*", i, 1)
                }

                c == '~' -> {
                    toggle("strikethrough", "~", i, 1)
                }

                c in RESERVED -> {
                    throw parseError(
                        "Character '$c' is reserved and must be escaped with the preceding '\\'",
                    )
                }

                else -> {
                    out.append(c)
                    1
                }
            }
            i += consumed
        }
        stack.lastOrNull()?.let {
            throw parseError(
                "Can't find end of ${it.type.replaceFirstChar(
                    Char::uppercase,
                )} entity at byte offset ${src.utf8Offset(it.sourceIndex)}",
            )
        }
        return FormattedText(out.toString(), done.sortedBy { it.long("offset") })
    }

    private fun escape(i: Int): Int {
        val next = src.getOrNull(i + 1) ?: throw parseError("Character '\\' at the end of the text must be escaped")
        out.append(next)
        return 2
    }

    private fun toggle(type: String, marker: String, i: Int, width: Int): Int {
        val top = stack.lastOrNull()
        if (top != null && top.tag == marker) {
            stack.removeLast()
            val length = out.length - top.start
            if (length > 0) done += entity(top.type, top.start, length)
        } else if (stack.any { it.tag == marker }) {
            throw parseError(
                "Can't find end of ${top?.type?.replaceFirstChar(
                    Char::uppercase,
                )} entity at byte offset ${src.utf8Offset(top?.sourceIndex ?: i)}",
            )
        } else {
            stack.addLast(OpenEntity(type, marker, out.length, i))
        }
        return width
    }

    private fun code(i: Int): Int {
        val pre = src.startsWith("```", i)
        val fence = if (pre) "```" else "`"
        val bodyStart = i + fence.length
        val end = src.indexOf(fence, bodyStart)
        if (end ==
            -1
        ) throw parseError("Can't find end of ${if (pre) "Pre" else "Code"} entity at byte offset ${src.utf8Offset(i)}")
        var body = src.substring(bodyStart, end)
        var language: String? = null
        if (pre) {
            val newline = body.indexOf('\n')
            if (newline > 0 && body.substring(0, newline).none { it.isWhitespace() }) {
                language = body.substring(0, newline)
                body = body.substring(newline + 1)
            }
        }
        val text = body.replace(Regex("\\\\(.)")) { it.groupValues[1] }
        val start = out.length
        out.append(text)
        if (text.isNotEmpty()) {
            val extra = language?.let { mapOf("language" to JsonPrimitive(it)) }.orEmpty()
            done += entity(if (pre) "pre" else "code", start, text.length, extra)
        }
        return end + fence.length - i
    }

    private fun link(i: Int): Int {
        val close = findClosingBracket(i)
        val urlStart = close + 1
        if (close == -1 || src.getOrNull(urlStart) != '(') {
            throw parseError("Can't find end of a url entity starting at byte offset ${src.utf8Offset(i)}")
        }
        val urlEnd = src.indexOf(')', urlStart)
        if (urlEnd ==
            -1
        ) throw parseError("Can't find end of a url entity starting at byte offset ${src.utf8Offset(i)}")
        val label = src.substring(i + 1, close).replace(Regex("\\\\(.)")) { it.groupValues[1] }
        val url = src.substring(urlStart + 1, urlEnd)
        val start = out.length
        out.append(label)
        val userId = Regex("^tg://user\\?id=(\\d+)$")
            .matchEntire(url)
            ?.groupValues
            ?.get(1)
            ?.toLong()
        done += if (userId != null) {
            entity("text_mention", start, label.length, mapOf("user" to buildJsonObject { put("id", userId) }))
        } else {
            entity("text_link", start, label.length, mapOf("url" to JsonPrimitive(url)))
        }
        return urlEnd + 1 - i
    }

    private fun findClosingBracket(from: Int): Int {
        var j = from + 1
        while (j < src.length) {
            when (src[j]) {
                '\\' -> j++
                ']' -> return j
            }
            j++
        }
        return -1
    }

    /** `>quote` lines, or `**>quote ... ||` for an expandable one; the quote body may hold nested formatting. */
    private fun blockquote(i: Int): Int {
        val isExpandable = src.startsWith("**>", i)
        var j = if (isExpandable) i + 2 else i
        val lines = mutableListOf<String>()
        while (j < src.length && src[j] == '>') {
            val eol = src.indexOf('\n', j).let { if (it == -1) src.length else it }
            lines += src.substring(j + 1, eol)
            j = if (eol < src.length) eol + 1 else eol
        }
        if (isExpandable && lines.isNotEmpty() && lines.last().endsWith("||")) {
            lines[lines.lastIndex] = lines.last().removeSuffix("||")
        }
        val inner = MarkdownV2Parser(lines.joinToString("\n")).parse()
        val start = out.length
        out.append(inner.text)
        inner.entities.forEach { e -> done += e.with("offset" to JsonPrimitive((e.long("offset") ?: 0L) + start)) }
        if (j < src.length) out.append('\n')
        if (inner.text.isNotEmpty()) {
            done += entity(if (isExpandable) "expandable_blockquote" else "blockquote", start, inner.text.length)
        }
        return j - i
    }

    private companion object {
        const val RESERVED = "[]()>#+-=|{}.!"
    }
}

private class LegacyMarkdownParser(
    private val src: String,
) {
    private val out = StringBuilder()
    private val done = mutableListOf<JsonObject>()

    fun parse(): FormattedText {
        var i = 0
        while (i < src.length) {
            val c = src[i]
            i = when {
                c == '\\' && src.getOrNull(i + 1) in ESCAPABLE -> {
                    out.append(src[i + 1])
                    i + 2
                }

                src.startsWith("```", i) -> {
                    pair("```", "pre", i)
                }

                c == '`' -> {
                    pair("`", "code", i)
                }

                c == '*' -> {
                    pair("*", "bold", i)
                }

                c == '_' -> {
                    pair("_", "italic", i)
                }

                c == '[' -> {
                    link(i)
                }

                else -> {
                    out.append(c)
                    i + 1
                }
            }
        }
        return FormattedText(out.toString(), done.sortedBy { it.long("offset") })
    }

    private fun pair(marker: String, type: String, i: Int): Int {
        val end = src.indexOf(marker, i + marker.length)
        if (end == -1) throw parseError("Can't find end of the entity starting at byte offset ${src.utf8Offset(i)}")
        val body = src.substring(i + marker.length, end)
        val start = out.length
        out.append(body)
        if (body.isNotEmpty()) done += entity(type, start, body.length)
        return end + marker.length
    }

    private fun link(i: Int): Int {
        val close = src.indexOf(']', i)
        if (close == -1 || src.getOrNull(close + 1) != '(') {
            out.append('[')
            return i + 1
        }
        val urlEnd = src.indexOf(')', close)
        if (urlEnd == -1) throw parseError("Can't find end of the entity starting at byte offset ${src.utf8Offset(i)}")
        val label = src.substring(i + 1, close)
        val start = out.length
        out.append(label)
        done +=
            entity("text_link", start, label.length, mapOf("url" to JsonPrimitive(src.substring(close + 2, urlEnd))))
        return urlEnd + 1
    }

    private companion object {
        val ESCAPABLE = setOf('_', '*', '`', '[')
    }
}

private val URL = Regex("https?://[^\\s<>]+")
private val EMAIL = Regex("[\\w.+-]+@[\\w-]+(?:\\.[\\w-]+)+")
private val MENTION = Regex("(?<![\\w@])@[A-Za-z][A-Za-z0-9_]{4,31}")
private val CASHTAG = Regex("(?<![\\w$])\\$[A-Z]{3,8}(?!\\w)")
private val HASHTAG = Regex("(?<![\\w#])#[\\p{L}\\p{N}_]{1,64}")
private val COMMAND = Regex("(?<!\\S)/[A-Za-z0-9_]{1,32}(?:@[A-Za-z][A-Za-z0-9_]{4,31})?")
private const val TRAILING_PUNCTUATION = ".,;:!?)"
private val VERBATIM = setOf("code", "pre", "text_link", "url", "text_mention")

/** Adds the entities Telegram detects on its own: bot commands, urls, emails, mentions and hashtags. */
internal fun FormattedText.withAutoEntities(): FormattedText {
    val taken = entities.filter { it.str("type") in VERBATIM }.map { ranges(it) }
    val found = mutableListOf<JsonObject>()

    fun add(type: String, range: IntRange) {
        val overlaps = (taken + found.map { ranges(it) }).any { it.first <= range.last && range.first <= it.last }
        if (!overlaps) found += entity(type, range.first, range.last - range.first + 1)
    }

    for (m in EMAIL.findAll(text)) add("email", m.range)
    for (m in URL.findAll(text)) {
        val trimmed = m.value.trimEnd { it in TRAILING_PUNCTUATION }
        add("url", m.range.first until m.range.first + trimmed.length)
    }
    for (m in COMMAND.findAll(text)) add("bot_command", m.range)
    for (m in MENTION.findAll(text)) add("mention", m.range)
    for (m in HASHTAG.findAll(text)) add("hashtag", m.range)
    for (m in CASHTAG.findAll(text)) add("cashtag", m.range)
    return copy(entities = (entities + found).sortedBy { it.long("offset") })
}

private fun ranges(e: JsonObject): IntRange {
    val offset = e.long("offset")!!.toInt()
    return offset until offset + e.long("length")!!.toInt()
}
