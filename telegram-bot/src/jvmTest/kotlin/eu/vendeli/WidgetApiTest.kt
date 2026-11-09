package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.api.botactions.getChatShareThemeBackdrops
import eu.vendeli.tgbot.api.gift.deleteBusinessAccountGift
import eu.vendeli.tgbot.api.gift.getUniqueGift
import eu.vendeli.tgbot.api.verification.verifyUser
import eu.vendeli.tgbot.api.widget.answerWidgetQuery
import eu.vendeli.tgbot.api.widget.editMessageWidgetData
import eu.vendeli.tgbot.api.widget.editSharedWidgetData
import eu.vendeli.tgbot.api.widget.sendSharedWidget
import eu.vendeli.tgbot.api.widget.sendWidget
import eu.vendeli.tgbot.types.chat.ChatFullInfo
import eu.vendeli.tgbot.types.chat.ChatShareTheme
import eu.vendeli.tgbot.types.chat.EmojiType
import eu.vendeli.tgbot.types.common.ChatShareThemeBackdrops
import eu.vendeli.tgbot.types.common.Update
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.component.SharedWidgetQueryUpdate
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.component.WidgetQueryUpdate
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.msg.RichText
import eu.vendeli.tgbot.utils.common.processUpdate
import eu.vendeli.tgbot.utils.common.serde
import eu.vendeli.tgbot.types.widget.WidgetQueryType
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.json.jsonPrimitive

class WidgetApiTest : BotTestContext() {
    private val user = """{"id": 1, "is_bot": false, "first_name": "Test"}"""

    private fun updateOf(body: String) = serde.decodeFromString(Update.serializer(), """{"update_id": 1, $body}""")

    @Test
    fun `widget query update is processed and typed`() {
        val update = updateOf(
            """"widget_query": {"id": "q1", "from": $user, "chat_instance": "ci", "data": "ping",
                "type": {"type": "sign_data", "sign_data": "{}",
                    "wallet": {"address": "EQ1", "amount": "10"}}}""",
        ).processUpdate()

        update.shouldBeInstanceOf<WidgetQueryUpdate>()
        update.type shouldBe UpdateType.WIDGET_QUERY
        update.text shouldBe "ping"
        update.user.id shouldBe 1
        update.widgetQuery.type
            .shouldBeInstanceOf<WidgetQueryType.SignData>()
            .wallet.address shouldBe "EQ1"
    }

    @Test
    fun `shared widget query update is processed and typed`() {
        val update = updateOf(
            """"shared_widget_query": {"id": "q2", "from": $user, "data": "d", "widget_id": "w1",
                "type": {"type": "sent_transaction", "msg_hash": "h",
                    "wallet": {"address": "EQ1", "amount": "10", "public_key": "ab"}}}""",
        ).processUpdate()

        update.shouldBeInstanceOf<SharedWidgetQueryUpdate>()
        update.type shouldBe UpdateType.SHARED_WIDGET_QUERY
        update.sharedWidgetQuery.type
            .shouldBeInstanceOf<WidgetQueryType.SentTransaction>()
            .msgHash shouldBe "h"
    }

    @Test
    fun `widget message and service messages are decoded`() {
        val message = serde.decodeFromString(
            Message.serializer(),
            """{"message_id": 1, "date": 0, "chat": {"id": 1, "type": "private"},
                "widget": {"html": "<b>x</b>", "width": 300, "public_data": "p"},
                "paid_messages_waived": {},
                "ton_wallet_transfer": {"transaction_id": "t", "peer_address": "a", "amount": "-5"}}""",
        )

        val widget = message.widget.shouldNotBeNull()
        widget.html shouldBe "<b>x</b>"
        widget.width shouldBe 300
        message.paidMessagesWaived.shouldNotBeNull()
        message.tonWalletTransfer?.amount shouldBe "-5"
    }

    @Test
    fun `chat share theme and ton wallet card are decoded`() {
        val info = serde.decodeFromString(
            ChatFullInfo.serializer(),
            """{"id": 1, "type": "private", "accent_color_id": 1, "max_reaction_count": 1,
                "accepted_gift_types": {"unlimited_gifts": true, "limited_gifts": true, "unique_gifts": true,
                    "premium_subscription": true, "gifts_from_channels": true},
                "share_theme": {"type": "emoji", "background_backdrop_id": 7,
                    "emojis": [{"type": "emoji", "emoji": "🔥"}, {"type": "custom_emoji", "custom_emoji_id": "c"}]},
                "ton_wallet_card": {"address": "EQ1", "amount": "1", "stickers": []}}""",
        )

        val theme = info.shareTheme.shouldBeInstanceOf<ChatShareTheme.Emoji>()
        theme.backgroundBackdropId shouldBe 7
        theme.emojis[0].shouldBeInstanceOf<EmojiType.Emoji>().emoji shouldBe "🔥"
        theme.emojis[1].shouldBeInstanceOf<EmojiType.CustomEmoji>().customEmojiId shouldBe "c"
        info.tonWalletCard?.address shouldBe "EQ1"
    }

    @Test
    fun `backdrops list is decoded`() {
        val backdrops = serde.decodeFromString(
            ChatShareThemeBackdrops.serializer(),
            """{"backdrops": [{"id": 1, "name": "n", "colors":
                {"center_color": 1, "edge_color": 2, "symbol_color": 3, "text_color": 4}}]}""",
        )

        backdrops.backdrops
            .single()
            .colors.edgeColor shouldBe 2
    }

    @Test
    fun `ton address rich text roundtrips`() {
        val text: RichText = RichText.TonAddress(RichText.Plain("addr"), "EQ1")

        val json = serde.encodeToString(RichText.serializer(), text)
        json shouldContain "\"type\":\"ton_address\""
        serde.decodeFromString(RichText.serializer(), json) shouldBe text
    }

    @Test
    fun `widget actions build api parameters`() {
        sendWidget("<b>x</b>")
            .options {
                width = 300
                isShared = true
            }.apply {
                method shouldBe "sendWidget"
                parameters["html"]?.jsonPrimitive?.content shouldBe "<b>x</b>"
                parameters["width"]?.jsonPrimitive?.content shouldBe "300"
                parameters["is_shared"]?.jsonPrimitive?.content shouldBe "true"
            }
        sendSharedWidget("w1")
            .options {
                chatData = "d"
                parseMode = ParseMode.HTML
            }.apply {
                parameters["widget_id"]?.jsonPrimitive?.content shouldBe "w1"
                parameters["chat_data"]?.jsonPrimitive?.content shouldBe "d"
            }
        answerWidgetQuery("q", "data").parameters["data"]?.jsonPrimitive?.content shouldBe "data"
        editSharedWidgetData("w1", "", 60).apply {
            parameters["public_data"]?.jsonPrimitive?.content shouldBe ""
            parameters["cache_time"]?.jsonPrimitive?.content shouldBe "60"
        }
        editMessageWidgetData(5)
            .options { chatData = "" }
            .parameters["message_id"]
            ?.jsonPrimitive
            ?.content shouldBe "5"
        getChatShareThemeBackdrops().method shouldBe "getChatShareThemeBackdrops"
        getUniqueGift("Gift-1").parameters["name"]?.jsonPrimitive?.content shouldBe "Gift-1"
        deleteBusinessAccountGift("bc", "og").parameters["owned_gift_id"]?.jsonPrimitive?.content shouldBe "og"
    }

    @Test
    fun `verify accepts parse mode and entities for custom description`() {
        val action = verifyUser(1L, "custom")
            .options { customDescriptionParseMode = ParseMode.HTML }
            .entities { (0 to 1) url "https://example.com" }

        action.parameters["custom_description_parse_mode"].shouldNotBeNull()
        action.parameters["custom_description_entities"].shouldNotBeNull()
    }
}
