package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.media.InputRichBlock
import eu.vendeli.tgbot.types.media.InputRichBlockListItem
import eu.vendeli.tgbot.types.media.InputRichMessage
import eu.vendeli.tgbot.types.msg.RichText
import eu.vendeli.tgbot.utils.builders.RichEntitiesBuilder
import eu.vendeli.tgbot.utils.builders.bold
import eu.vendeli.tgbot.utils.builders.customEmoji
import eu.vendeli.tgbot.utils.builders.dateTime
import eu.vendeli.tgbot.utils.builders.InputRichMessage
import eu.vendeli.tgbot.utils.builders.italic
import eu.vendeli.tgbot.utils.builders.minus
import eu.vendeli.tgbot.utils.builders.richText
import eu.vendeli.tgbot.utils.builders.url
import eu.vendeli.tgbot.utils.common.serde
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class RichTextDslTest : BotTestContext() {
    private fun RichText.json() = serde.encodeToString(RichText.serializer(), this)

    // region factories

    @Test
    fun `factories wrap plain strings and rich texts`() {
        bold("x") shouldBe RichText.Bold(RichText.Plain("x"))
        bold(italic("x")) shouldBe RichText.Bold(RichText.Italic(RichText.Plain("x")))
    }

    @Test
    fun `factories take metadata first and text last`() {
        url("https://example.com", "docs") shouldBe
            RichText.Url(RichText.Plain("docs"), "https://example.com")
        dateTime(10L, "HH:mm", "now") shouldBe RichText.DateTime(RichText.Plain("now"), 10L, "HH:mm")
    }

    @Test
    fun `customEmoji keeps id and alternative text in their own fields`() {
        customEmoji("5368324170671202286", "👍") shouldBe
            RichText.CustomEmoji("5368324170671202286", "👍")
    }

    // endregion

    // region operators

    @Test
    fun `operator chain can start from any element`() {
        ("a" - bold("b")).build() shouldBe RichText.Chunks(RichText.Plain("a"), bold("b"))
        (bold("a") - "b").build() shouldBe RichText.Chunks(bold("a"), RichText.Plain("b"))
        (bold("a") - italic("b")).build() shouldBe RichText.Chunks(bold("a"), italic("b"))
    }

    @Test
    fun `operator chain does not mutate shared builders`() {
        val base = "Hi " - bold("x")

        val first = base - "A"
        val second = base - "B"

        base.richTexts shouldHaveSize 2
        first.richTexts.last() shouldBe RichText.Plain("A")
        second.richTexts.last() shouldBe RichText.Plain("B")
        first.richTexts shouldHaveSize 3
        second.richTexts shouldHaveSize 3
    }

    @Test
    fun `operator chain flattens nested chunks`() {
        val nested = RichText.Chunks(RichText.Plain("b"), RichText.Chunks(RichText.Plain("c")))

        val built = ("a" - nested).build()

        built shouldBe RichText.Chunks(RichText.Plain("a"), RichText.Plain("b"), RichText.Plain("c"))
    }

    @Test
    fun `entities builder exposes an immutable view`() {
        val source = mutableListOf<RichText>(RichText.Plain("a"))
        val builder = RichEntitiesBuilder(source)

        source += RichText.Plain("b")

        builder.richTexts shouldHaveSize 1
    }

    // endregion

    // region richText builder

    @Test
    fun `richText builder nests formatting`() {
        val text = richText {
            +"Hello "
            bold {
                +"big "
                italic("world")
            }
        }

        text.json() shouldBe
            """["Hello ",{"type":"bold","text":["big ",{"type":"italic","text":"world"}]}]"""
    }

    @Test
    fun `richText builder adds string rich text and lambda forms`() {
        val text = richText {
            bold("a")
            bold(RichText.Italic(RichText.Plain("b")))
            bold { +"c" }
        }

        text shouldBe RichText.Chunks(
            bold("a"),
            bold(italic("b")),
            bold("c"),
        )
    }

    @Test
    fun `richText builder places metadata before its content`() {
        val text = richText {
            url("https://example.com") { bold("docs") }
            dateTime(1L, "HH:mm", "now")
        }

        text shouldBe RichText.Chunks(
            RichText.Url(bold("docs"), "https://example.com"),
            RichText.DateTime(RichText.Plain("now"), 1L, "HH:mm"),
        )
    }

    @Test
    fun `richText builder supports mentions of users`() {
        val user = User(id = 1L, isBot = false, firstName = "Test")

        richText { textMention(user, "me") } shouldBe RichText.TextMention(RichText.Plain("me"), user)
    }

    @Test
    fun `richText builder collapses single and empty content`() {
        richText { +"only" } shouldBe RichText.Plain("only")
        richText { bold("only") } shouldBe bold("only")
        richText { } shouldBe RichText.Plain("")
    }

    @Test
    fun `richText builder flattens added chunks`() {
        val text = richText {
            +RichText.Chunks(RichText.Plain("a"), RichText.Plain("b"))
            +"c"
        }

        text shouldBe RichText.Chunks(RichText.Plain("a"), RichText.Plain("b"), RichText.Plain("c"))
    }

    // endregion

    // region blocks

    @Test
    fun `InputRichMessage builds blocks`() {
        val message = InputRichMessage {
            heading(2) { +"Title" }
            paragraph {
                +"Hello "
                bold("world")
            }
            paragraph("plain")
            divider()
            footer("bye")
        }

        message.blocks shouldBe listOf(
            InputRichBlock.SectionHeading(RichText.Plain("Title"), 2),
            InputRichBlock.Paragraph(RichText.Chunks(RichText.Plain("Hello "), bold("world"))),
            InputRichBlock.Paragraph(RichText.Plain("plain")),
            InputRichBlock.Divider,
            InputRichBlock.Footer(RichText.Plain("bye")),
        )
        message.html.shouldBeNull()
        message.markdown.shouldBeNull()
    }

    @Test
    fun `InputRichMessage builds nested structures`() {
        val message = InputRichMessage {
            list {
                item("one")
                item(hasCheckbox = true, isChecked = true) { paragraph("two") }
            }
            blockquote(credit = "Someone") { paragraph("quoted") }
            details("More", isOpen = true) { paragraph("hidden") }
            preformatted("val a = 1", language = "kotlin")
            pullquote { +"pull" }
            thinking("hmm")
            mathematicalExpression("E=mc^2")
            anchor("top")
        }

        message.blocks shouldBe listOf(
            InputRichBlock.ListBlock(
                listOf(
                    InputRichBlockListItem(listOf(InputRichBlock.Paragraph(RichText.Plain("one")))),
                    InputRichBlockListItem(
                        listOf(InputRichBlock.Paragraph(RichText.Plain("two"))),
                        hasCheckbox = true,
                        isChecked = true,
                    ),
                ),
            ),
            InputRichBlock.BlockQuotation(
                listOf(InputRichBlock.Paragraph(RichText.Plain("quoted"))),
                RichText.Plain("Someone"),
            ),
            InputRichBlock.Details(
                RichText.Plain("More"),
                listOf(InputRichBlock.Paragraph(RichText.Plain("hidden"))),
                isOpen = true,
            ),
            InputRichBlock.Preformatted(RichText.Plain("val a = 1"), "kotlin"),
            InputRichBlock.PullQuotation(RichText.Plain("pull")),
            InputRichBlock.Thinking(RichText.Plain("hmm")),
            InputRichBlock.MathematicalExpression("E=mc^2"),
            InputRichBlock.Anchor("top"),
        )
    }

    @Test
    fun `InputRichMessage applies message level options`() {
        val message = InputRichMessage {
            isRtl = true
            skipEntityDetection = true
            paragraph("text")
        }

        message.isRtl shouldBe true
        message.skipEntityDetection shouldBe true
        message.media.shouldBeNull()
    }

    @Test
    fun `InputRichMessage accepts prebuilt blocks as an escape hatch`() {
        val message = InputRichMessage {
            +InputRichBlock.Divider
        }

        message.blocks shouldBe listOf(InputRichBlock.Divider)
    }

    @Test
    fun `InputRichMessage requires exactly one content kind`() {
        shouldThrow<IllegalArgumentException> { InputRichMessage { } }
        shouldThrow<IllegalArgumentException> {
            InputRichMessage {
                html = "<p>x</p>"
                paragraph("x")
            }
        }
        InputRichMessage { markdown = "*x*" }.markdown shouldBe "*x*"
    }

    @Test
    fun `InputRichMessage rejects invalid heading size`() {
        shouldThrow<IllegalArgumentException> { InputRichMessage { heading(0) { +"x" } } }
        shouldThrow<IllegalArgumentException> { InputRichMessage { heading(7, "x") } }
    }

    @Test
    fun `built InputRichMessage is serialized to api payload`() {
        val message: InputRichMessage = InputRichMessage {
            paragraph { bold("hi") }
        }

        val json = serde.encodeToString(InputRichMessage.serializer(), message)

        json.contains("\"type\":\"paragraph\"") shouldBe true
        json.contains("\"type\":\"bold\"") shouldBe true
    }

    // endregion
}
