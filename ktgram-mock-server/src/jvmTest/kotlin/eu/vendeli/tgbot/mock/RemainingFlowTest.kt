package eu.vendeli.tgbot.mock

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.put

class RemainingFlowTest :
    FunSpec({
        fun server() = MockTelegramServer(MockServerConfig(port = 0))
        val group = -100L

        test("admin titles and member tags show up in getChatMember") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    bot.addChat(group)
                    bot.addUser(10)
                    bot.setMemberStatus(group, 10, "member")
                    s.ok("setChatMemberTag", "chat_id" to group, "user_id" to 10, "tag" to "vip")
                    s.ok("getChatMember", "chat_id" to group, "user_id" to 10).o().str("tag") shouldBe "vip"
                    s
                        .error(
                            "setChatMemberTag",
                            "chat_id" to group,
                            "user_id" to 10,
                            "tag" to "x".repeat(17),
                        ).description shouldContain
                        "TAG_TOO_LONG"
                    s
                        .error(
                            "setChatAdministratorCustomTitle",
                            "chat_id" to group,
                            "user_id" to 10,
                            "custom_title" to "Boss",
                        ).description shouldContain
                        "not an administrator"
                    s.ok("promoteChatMember", "chat_id" to group, "user_id" to 10)
                    s.ok(
                        "setChatAdministratorCustomTitle",
                        "chat_id" to group,
                        "user_id" to 10,
                        "custom_title" to "Boss",
                    )
                    s.ok("getChatMember", "chat_id" to group, "user_id" to 10).o().str("custom_title") shouldBe "Boss"
                    s.ok("setChatMemberTag", "chat_id" to group, "user_id" to 10, "tag" to "x")
                    bot.addChat(-200, "channel", "Other")
                    s.ok("banChatSenderChat", "chat_id" to group, "sender_chat_id" to -200) shouldBe TRUE
                    s.error("banChatSenderChat", "chat_id" to group, "sender_chat_id" to group).code shouldBe 400
                    s.ok("unbanChatSenderChat", "chat_id" to group, "sender_chat_id" to -200)
                }
            }
        }

        test("emoji status, verification and passport errors") {
            server().use { s ->
                runBlocking {
                    s.bot().allowEmojiStatus(USER)
                    s.ok(
                        "setUserEmojiStatus",
                        "user_id" to USER,
                        "emoji_status_custom_emoji_id" to "e1",
                        "emoji_status_expiration_date" to 99,
                    )
                    val chat = s.ok("getChat", "chat_id" to USER).o()
                    chat.str("emoji_status_custom_emoji_id") shouldBe "e1"
                    s.ok("setUserEmojiStatus", "user_id" to USER)
                    s.ok("getChat", "chat_id" to USER).o()["emoji_status_custom_emoji_id"] shouldBe null

                    s.error("verifyUser", "user_id" to 5).description shouldContain "BOT_VERIFIER_FORBIDDEN"
                    s.error("setUserEmojiStatus", "user_id" to 5).description shouldContain "emoji status"
                    s.bot().allowVerification()
                    s.ok("verifyUser", "user_id" to 5, "custom_description" to "Trusted")
                    s.bot().isVerified(5) shouldBe true
                    s.ok("removeUserVerification", "user_id" to 5)
                    s.bot().isVerified(5) shouldBe false
                    s.ok("verifyChat", "chat_id" to group)
                    s.bot().isChatVerified(group) shouldBe true

                    s.ok(
                        "setPassportDataErrors",
                        "user_id" to 5,
                        "errors" to
                            listOf(
                                mapOf(
                                    "source" to "data",
                                    "type" to "passport",
                                    "field_name" to "x",
                                    "data_hash" to "h",
                                    "message" to "m",
                                ),
                            ),
                    )
                    s.bot().passportErrors(5) shouldHaveSize 1
                    s
                        .error(
                            "setPassportDataErrors",
                            "user_id" to 5,
                            "errors" to listOf(mapOf("type" to "passport")),
                        ).code shouldBe
                        400
                }
            }
        }

        test("boosts, profile photos and audios, personal chat messages") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    bot.addChat(group)
                    s.ok("getUserChatBoosts", "chat_id" to group, "user_id" to 5).o()["boosts"]!!.a() shouldHaveSize 0
                    bot.addBoost(group, 5)
                    bot.addBoost(group, 5)
                    s.ok("getUserChatBoosts", "chat_id" to group, "user_id" to 5).o()["boosts"]!!.a() shouldHaveSize 2
                    s.ok("getUserChatBoosts", "chat_id" to group, "user_id" to 6).o()["boosts"]!!.a() shouldHaveSize 0

                    bot.setProfilePhotos(5, 3)
                    val photos = s.ok("getUserProfilePhotos", "user_id" to 5, "offset" to 1, "limit" to 5).o()
                    photos.long("total_count") shouldBe 3L
                    photos["photos"]!!.a() shouldHaveSize 2
                    s.ok("getUserProfilePhotos", "user_id" to 9).o().long("total_count") shouldBe 0L
                    bot.setProfileAudios(5, 2)
                    s.ok("getUserProfileAudios", "user_id" to 5).o()["audios"]!!.a() shouldHaveSize 2

                    bot.setPersonalChatMessages(
                        5,
                        listOf(
                            obj {
                                put("message_id", 1)
                                put("date", 1)
                                put(
                                    "chat",
                                    obj {
                                        put("id", 5)
                                        put("type", "private")
                                    },
                                )
                            },
                        ),
                    )
                    s.ok("getUserPersonalChatMessages", "user_id" to 5, "limit" to 10).a() shouldHaveSize 1
                }
            }
        }

        test("subscriptions: premium gifts cost Stars, subscriptions can be cancelled once") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    s
                        .error(
                            "giftPremiumSubscription",
                            "user_id" to 5,
                            "month_count" to 2,
                            "star_count" to 10,
                        ).description shouldContain
                        "month_count"
                    s
                        .error(
                            "giftPremiumSubscription",
                            "user_id" to 5,
                            "month_count" to 3,
                            "star_count" to 10,
                        ).description shouldContain
                        "BALANCE_TOO_LOW"
                    bot.setStarBalance(30)
                    s.ok("giftPremiumSubscription", "user_id" to 5, "month_count" to 3, "star_count" to 10)
                    bot.starBalance() shouldBe 20L

                    val charge = bot
                        .pay(
                            5,
                            5,
                            "sub",
                            "XTR",
                            5,
                        )["message"]!!
                        .o()["successful_payment"]!!
                        .o()
                        .str("telegram_payment_charge_id")!!
                    s
                        .error(
                            "editUserStarSubscription",
                            "user_id" to 5,
                            "telegram_payment_charge_id" to "nope",
                            "is_canceled" to true,
                        ).description shouldContain
                        "CHARGE_ID_INVALID"
                    s.ok(
                        "editUserStarSubscription",
                        "user_id" to 5,
                        "telegram_payment_charge_id" to charge,
                        "is_canceled" to true,
                    )
                    s
                        .error(
                            "editUserStarSubscription",
                            "user_id" to 5,
                            "telegram_payment_charge_id" to charge,
                            "is_canceled" to true,
                        ).description shouldContain
                        "SUBSCRIPTION_NOT_MODIFIED"
                    s.ok(
                        "editUserStarSubscription",
                        "user_id" to 5,
                        "telegram_payment_charge_id" to charge,
                        "is_canceled" to false,
                    )
                }
            }
        }

        test("drafts are updated by id and cleared by empty text") {
            server().use { s ->
                runBlocking {
                    s.ok("sendMessageDraft", "chat_id" to USER, "draft_id" to 1, "text" to "Hel")
                    s.ok(
                        "sendMessageDraft",
                        "chat_id" to USER,
                        "draft_id" to 1,
                        "text" to "Hello <b>x</b>",
                        "parse_mode" to "HTML",
                    )
                    s
                        .bot()
                        .drafts(USER)
                        .single()
                        .str("text") shouldBe "Hello x"
                    s
                        .error(
                            "sendMessageDraft",
                            "chat_id" to USER,
                            "draft_id" to 0,
                            "text" to "x",
                        ).description shouldContain
                        "DRAFT_ID_INVALID"
                    s.ok("sendMessageDraft", "chat_id" to USER, "draft_id" to 1)
                    s.bot().drafts(USER) shouldHaveSize 0
                    s.ok(
                        "sendRichMessageDraft",
                        "chat_id" to USER,
                        "draft_id" to 2,
                        "rich_message" to mapOf("blocks" to listOf<Any>()),
                    )
                    s.bot().drafts(USER) shouldHaveSize 1
                }
            }
        }

        test("ephemeral messages live apart from regular ones and can be edited and deleted") {
            server().use { s ->
                runBlocking {
                    val sent = s.ok("sendMessage", "chat_id" to USER, "text" to "secret", "receiver_user_id" to 7).o()
                    sent.long("ephemeral_message_id") shouldBe 1L
                    sent["receiver_user"]!!.o().long("id") shouldBe 7L
                    s.bot().messages(USER) shouldHaveSize 0
                    s.bot().ephemeralMessages(USER) shouldHaveSize 1
                    val where = arrayOf("chat_id" to USER, "receiver_user_id" to 7, "ephemeral_message_id" to 1)
                    s.ok("editEphemeralMessageText", *where, "text" to "changed") shouldBe TRUE
                    s
                        .bot()
                        .ephemeralMessages(USER)
                        .single()
                        .str("text") shouldBe "changed"
                    s.ok(
                        "editEphemeralMessageReplyMarkup",
                        *where,
                        "reply_markup" to mapOf("inline_keyboard" to listOf<Any>()),
                    )
                    s
                        .error(
                            "editEphemeralMessageText",
                            "chat_id" to USER,
                            "receiver_user_id" to 8,
                            "ephemeral_message_id" to 1,
                            "text" to "x",
                        ).description shouldContain
                        "ephemeral message not found"
                    s.ok("deleteEphemeralMessage", *where)
                    s.bot().ephemeralMessages(USER) shouldHaveSize 0
                }
            }
        }

        test("widgets, web app, guest and join queries are answered exactly once") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    val widget = bot.widgetQuery(5, "hi")["widget_query"]!!.o().str("id")!!
                    s.ok("answerWidgetQuery", "widget_query_id" to widget, "data" to "reply") shouldBe TRUE
                    s.error("answerWidgetQuery", "widget_query_id" to widget).description shouldContain
                        "query ID is invalid"

                    s.ok("sendMessage", "chat_id" to USER, "text" to "w")
                    s.ok("editMessageWidgetData", "chat_id" to USER, "message_id" to 1, "public_data" to "d")
                    bot.widgetData("$USER:1")!!.str("public_data") shouldBe "d"
                    s.error("editMessageWidgetData", "chat_id" to USER, "message_id" to 1).code shouldBe 400
                    s.ok("editSharedWidgetData", "widget_id" to "w1", "public_data" to "p", "cache_time" to 5)
                    s
                        .error(
                            "editSharedWidgetData",
                            "widget_id" to "w1",
                            "public_data" to "p",
                            "cache_time" to -1,
                        ).code shouldBe
                        400

                    val app = bot.webAppQuery()
                    val result = mapOf(
                        "type" to "article",
                        "id" to "1",
                        "title" to "t",
                        "input_message_content" to mapOf("message_text" to "x"),
                    )
                    s
                        .ok(
                            "answerWebAppQuery",
                            "web_app_query_id" to app,
                            "result" to result,
                        ).o()
                        .str("inline_message_id")!! shouldStartWith
                        "mock_inline_"
                    s.error("answerWebAppQuery", "web_app_query_id" to app, "result" to result).code shouldBe 400

                    val inline = bot.inlineQuery("q")["inline_query"]!!.o().str("id")!!
                    s.ok("answerInlineQuery", "inline_query_id" to inline, "results" to listOf<Any>()) shouldBe TRUE
                    s.error("answerInlineQuery", "inline_query_id" to inline, "results" to listOf<Any>()).code shouldBe
                        400

                    bot.enqueue(obj { put("guest_message", obj { put("guest_query_id", "g1") }) })
                    s
                        .ok(
                            "answerGuestQuery",
                            "guest_query_id" to "g1",
                            "result" to result,
                        ).o()["inline_message_id"]!!
                        .toString() shouldContain
                        "mock_inline_"

                    bot.addChat(group)
                    val join = bot.requestToJoin(group, 11)["chat_join_request"]!!.o().str("query_id")!!
                    s
                        .error(
                            "sendChatJoinRequestWebApp",
                            "chat_join_request_query_id" to join,
                            "web_app_url" to "http://x",
                        ).code shouldBe
                        400
                    s.ok(
                        "sendChatJoinRequestWebApp",
                        "chat_join_request_query_id" to join,
                        "web_app_url" to "https://x.y/app",
                    )
                    s.ok("answerChatJoinRequestQuery", "chat_join_request_query_id" to join, "result" to "ok")
                    s
                        .error(
                            "answerChatJoinRequestQuery",
                            "chat_join_request_query_id" to join,
                            "result" to "ok",
                        ).code shouldBe
                        400
                }
            }
        }

        test("suggested posts are resolved once") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    val first = bot.suggestPost(-500, 5, "post me")["message"]!!.o().long("message_id")!!
                    s.ok("approveSuggestedPost", "chat_id" to -500, "message_id" to first, "send_date" to 123) shouldBe
                        TRUE
                    bot.lastMessage(-500)!!["suggested_post_info"]!!.o().str("state") shouldBe "approved"
                    s.error("declineSuggestedPost", "chat_id" to -500, "message_id" to first).description shouldContain
                        "SUGGESTED_POST_NOT_PENDING"
                    val second = bot.suggestPost(-500, 6, "another")["message"]!!.o().long("message_id")!!
                    s.ok("declineSuggestedPost", "chat_id" to -500, "message_id" to second, "comment" to "no")
                    bot
                        .messages(-500)
                        .last()["suggested_post_info"]!!
                        .o()
                        .str("state") shouldBe "declined"
                    s.ok("sendMessage", "chat_id" to -500, "text" to "plain")
                    s.error("approveSuggestedPost", "chat_id" to -500, "message_id" to 3).description shouldContain
                        "not a suggested post"
                }
            }
        }

        test("business gifts: list, convert, upgrade, transfer, delete") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    bot.addBusinessConnection("c1", 77, stars = 100)
                    val a = bot.addBusinessGift("c1", "g1", 40)
                    val b = bot.addBusinessGift("c1", "g2", 10)
                    val c = bot.addBusinessGift("c1", "g3", 70)
                    val list = s
                        .ok(
                            "getBusinessAccountGifts",
                            "business_connection_id" to "c1",
                            "sort_by_price" to true,
                            "limit" to 2,
                        ).o()
                    list.long("total_count") shouldBe 3L
                    list["gifts"]!!.a().map { it.o()["gift"]!!.o().str("id") } shouldBe listOf("g3", "g1")
                    list.str("next_offset") shouldBe "2"

                    s.ok("convertGiftToStars", "business_connection_id" to "c1", "owned_gift_id" to b) shouldBe TRUE
                    s.ok("getBusinessAccountStarBalance", "business_connection_id" to "c1").o().long("amount") shouldBe
                        110L
                    s
                        .error(
                            "convertGiftToStars",
                            "business_connection_id" to "c1",
                            "owned_gift_id" to b,
                        ).description shouldContain
                        "OWNED_GIFT_NOT_FOUND"

                    s
                        .error(
                            "upgradeGift",
                            "business_connection_id" to "c1",
                            "owned_gift_id" to a,
                            "star_count" to 500,
                        ).description shouldContain
                        "BALANCE_TOO_LOW"
                    s.ok("upgradeGift", "business_connection_id" to "c1", "owned_gift_id" to a, "star_count" to 30)
                    s.ok("getBusinessAccountStarBalance", "business_connection_id" to "c1").o().long("amount") shouldBe
                        80L
                    bot.businessGifts("c1").first { it.str("owned_gift_id") == a }.str("type") shouldBe "unique"
                    s
                        .error(
                            "convertGiftToStars",
                            "business_connection_id" to "c1",
                            "owned_gift_id" to a,
                        ).description shouldContain
                        "GIFT_CONVERT_UNAVAILABLE"
                    s
                        .error(
                            "transferGift",
                            "business_connection_id" to "c1",
                            "owned_gift_id" to c,
                            "new_owner_chat_id" to 5,
                        ).description shouldContain
                        "GIFT_NOT_TRANSFERABLE"
                    s.ok(
                        "transferGift",
                        "business_connection_id" to "c1",
                        "owned_gift_id" to a,
                        "new_owner_chat_id" to 5,
                    )
                    s.ok("getChatGifts", "chat_id" to 5).o().long("total_count") shouldBe 1L
                    s.ok("deleteBusinessAccountGift", "business_connection_id" to "c1", "owned_gift_id" to c)
                    bot.businessGifts("c1") shouldHaveSize 0
                    s.ok("getBusinessAccountGifts", "business_connection_id" to "c1").o().long("total_count") shouldBe
                        0L
                }
            }
        }

        test("received gifts carry an owned_gift_id") {
            server().use { s ->
                runBlocking {
                    s.bot().setStarBalance(100)
                    s.ok("sendGift", "user_id" to 5, "gift_id" to "mock_gift_1")
                    s
                        .ok(
                            "getUserGifts",
                            "user_id" to 5,
                        ).o()["gifts"]!!
                        .a()
                        .single()
                        .o()
                        .str("owned_gift_id") shouldStartWith
                        "og_"
                }
            }
        }
    })
