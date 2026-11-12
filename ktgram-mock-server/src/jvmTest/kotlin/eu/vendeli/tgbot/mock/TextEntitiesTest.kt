package eu.vendeli.tgbot.mock

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.serialization.json.JsonObject

private fun List<JsonObject>.summary() = map { Triple(it.str("type"), it.long("offset"), it.long("length")) }

class TextEntitiesTest :
    FunSpec({
        fun fmt(text: String, mode: String? = null) = TextFormatter.format(text, mode, null)

        test("HTML produces entities with UTF-16 offsets") {
            val r = fmt("a <b>bold</b> and <i>it<u>al</u></i> 😀<code>x</code>", "HTML")
            r.text shouldBe "a bold and ital 😀x"
            r.entities.summary() shouldBe listOf(
                Triple("bold", 2L, 4L),
                Triple("italic", 11L, 4L),
                Triple("underline", 13L, 2L),
                Triple("code", 18L, 1L),
            )
        }

        test("HTML understands links, pre with language, blockquote and html references") {
            val r =
                fmt(
                    "<a href=\"https://x.org\">l</a> &lt;&amp;&gt; <pre><code class=\"language-kotlin\">val a</code></pre><blockquote>q</blockquote>",
                    "HTML",
                )
            r.text shouldBe "l <&> val aq"
            r.entities.map { it.str("type") } shouldBe listOf("text_link", "pre", "blockquote")
            r.entities[1].str("language") shouldBe "kotlin"
        }

        test("HTML reports Telegram style parse errors") {
            shouldThrow<ApiException> { fmt("<b>open", "HTML") }.description shouldContain
                "Can't find end tag corresponding to start tag \"b\""
            shouldThrow<ApiException> { fmt("<b>x</i>", "HTML") }.description shouldContain "Unmatched end tag"
            shouldThrow<ApiException> { fmt("<foo>x</foo>", "HTML") }.description shouldContain
                "Unsupported start tag \"foo\""
            shouldThrow<ApiException> { fmt("a < b", "HTML") }.code shouldBe 400
        }

        test("MarkdownV2 formats and demands escaping of reserved characters") {
            val r = fmt("*bold* _it_ __under__ ~s~ ||sp|| `c` [t](https://a.b)", "MarkdownV2")
            r.text shouldBe "bold it under s sp c t"
            r.entities.map { it.str("type") } shouldBe
                listOf("bold", "italic", "underline", "strikethrough", "spoiler", "code", "text_link")
            fmt("1\\.5 \\- ok", "MarkdownV2").text shouldBe "1.5 - ok"
            shouldThrow<ApiException> { fmt("1.5", "MarkdownV2") }.description shouldContain
                "Character '.' is reserved and must be escaped"
            shouldThrow<ApiException> { fmt("*open", "MarkdownV2") }.description shouldContain
                "Can't find end of Bold entity"
        }

        test("MarkdownV2 handles pre blocks with language and blockquotes") {
            val r = fmt("```kotlin\nval a = 1\n```\n>quote", "MarkdownV2")
            r.entities.first().str("type") shouldBe "pre"
            r.entities.first().str("language") shouldBe "kotlin"
            r.entities.last().str("type") shouldBe "blockquote"
        }

        test("legacy Markdown") {
            val r = fmt("*b* _i_ `c` [t](https://a.b)", "Markdown")
            r.text shouldBe "b i c t"
            r.entities.map { it.str("type") } shouldBe listOf("bold", "italic", "code", "text_link")
            shouldThrow<ApiException> { fmt("*b", "Markdown") }.code shouldBe 400
        }

        test("an unknown parse mode is rejected") {
            shouldThrow<ApiException> { fmt("x", "BBCode") }.description shouldBe "Bad Request: unsupported parse_mode"
        }

        test("Telegram detects commands, urls, mentions, hashtags and emails on its own") {
            val r = fmt("/start@my_bot see https://a.b/c, @some_user #tag mail me@x.org")
            r.entities.summary().map { it.first } shouldBe listOf("bot_command", "url", "mention", "hashtag", "email")
            r.entities.first().long("length") shouldBe 13L
            r.entities[1].long("length") shouldBe 13L
        }

        test("auto detection skips code and explicit links") {
            fmt("/start and https://a.b").entities.map { it.str("type") } shouldBe listOf("bot_command", "url")
            fmt("`https://a.b`", "MarkdownV2").entities.map { it.str("type") } shouldBe listOf("code")
            fmt("<a href=\"https://c.d\">https://a.b</a>", "HTML").entities.map { it.str("type") } shouldBe
                listOf("text_link")
        }
    })

class TextEntitiesEdgeCaseTest :
    FunSpec({
        fun fmt(text: String, mode: String? = null) = TextFormatter.format(text, mode, null)

        test("MarkdownV2 blockquotes can hold formatting and expandable ones end with a spoiler marker") {
            val quote = fmt(">a *b* c\n>second", "MarkdownV2")
            quote.text shouldBe "a b c\nsecond"
            quote.entities.map { it.str("type") }.toSet() shouldBe setOf("bold", "blockquote")
            quote.entities.first { it.str("type") == "blockquote" }.long("length") shouldBe 12L

            val expandable = fmt("**>hidden\n>text||", "MarkdownV2")
            expandable.text shouldBe "hidden\ntext"
            expandable.entities.single().str("type") shouldBe "expandable_blockquote"
        }

        test("cashtags are detected") {
            fmt("buy \$USD now").entities.map { it.str("type") } shouldBe listOf("cashtag")
        }

        test("HTML nested entities keep their offsets") {
            val r = fmt("<b>a<i>b</i>c</b>", "HTML")
            r.entities.map { Triple(it.str("type"), it.long("offset"), it.long("length")) }.toSet() shouldBe
                setOf(Triple("bold", 0L, 3L), Triple("italic", 1L, 1L))
        }
    })
