package eu.vendeli.tgbot.mock

import com.sun.net.httpserver.HttpServer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress

class ExtendedFlowTest :
    FunSpec({
        fun server(config: MockServerConfig = MockServerConfig(port = 0)) = MockTelegramServer(config)
        val group = -100L

        test("chat settings show up in getChat") {
            server().use { s ->
                runBlocking {
                    s.bot().addChat(group, "supergroup", "Old")
                    s.ok("setChatTitle", "chat_id" to group, "title" to "New title")
                    s.ok("setChatDescription", "chat_id" to group, "description" to "About")
                    s.ok("setChatPermissions", "chat_id" to group, "permissions" to mapOf("can_send_messages" to false))
                    val chat = s.ok("getChat", "chat_id" to group).o()
                    chat.str("title") shouldBe "New title"
                    chat.str("description") shouldBe "About"
                    chat["permissions"]!!.o().bool("can_send_messages") shouldBe false
                    s
                        .ok("sendMessage", "chat_id" to group, "text" to "x")
                        .o()["chat"]!!
                        .o()
                        .str("title") shouldBe
                        "New title"
                    s.error("setChatTitle", "chat_id" to group, "title" to "").code shouldBe 400
                    s.error("setChatTitle", "chat_id" to USER, "title" to "x").code shouldBe 400
                    s.error("setChatPhoto", "chat_id" to group, "photo" to "file_id").description shouldBe
                        "Bad Request: PHOTO_INVALID"
                    s
                        .error(
                            "setChatStickerSet",
                            "chat_id" to group,
                            "sticker_set_name" to "nope",
                        ).description shouldContain
                        "can't set supergroup sticker set"
                }
            }
        }

        test("invite links: create, edit, revoke, export") {
            server().use { s ->
                runBlocking {
                    s.bot().addChat(group)
                    val link = s
                        .ok(
                            "createChatInviteLink",
                            "chat_id" to group,
                            "name" to "promo",
                            "member_limit" to 5,
                        ).o()
                    link.str("name") shouldBe "promo"
                    link.bool("is_revoked") shouldBe false
                    val url = link.str("invite_link")!!
                    s
                        .ok(
                            "editChatInviteLink",
                            "chat_id" to group,
                            "invite_link" to url,
                            "member_limit" to 9,
                        ).o()
                        .long("member_limit") shouldBe
                        9L
                    s
                        .ok(
                            "revokeChatInviteLink",
                            "chat_id" to group,
                            "invite_link" to url,
                        ).o()
                        .bool("is_revoked") shouldBe
                        true
                    s.error("editChatInviteLink", "chat_id" to group, "invite_link" to url).description shouldContain
                        "INVITE_HASH_EXPIRED"
                    s
                        .error(
                            "editChatInviteLink",
                            "chat_id" to group,
                            "invite_link" to "https://t.me/+zzz",
                        ).description shouldContain
                        "INVITE_HASH_EXPIRED"
                    s
                        .error(
                            "createChatInviteLink",
                            "chat_id" to group,
                            "creates_join_request" to true,
                            "member_limit" to 3,
                        ).code shouldBe
                        400
                    val first = s.ok("exportChatInviteLink", "chat_id" to group).toString()
                    val second = s.ok("exportChatInviteLink", "chat_id" to group).toString()
                    (first != second) shouldBe true
                    s.ok("getChat", "chat_id" to group).o().str("invite_link") shouldBe second.trim('"')
                }
            }
        }

        test("join requests are approved or declined once") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    bot.addChat(group)
                    val update = bot.requestToJoin(group, 11)
                    update["chat_join_request"]!!.o().long("user_chat_id") shouldBe 11L
                    s.ok("getUpdates").a() shouldHaveSize 1
                    s.ok("approveChatJoinRequest", "chat_id" to group, "user_id" to 11) shouldBe TRUE
                    s.ok("getChatMember", "chat_id" to group, "user_id" to 11).o().str("status") shouldBe "member"
                    s.error("approveChatJoinRequest", "chat_id" to group, "user_id" to 11).description shouldContain
                        "USER_ALREADY_PARTICIPANT"
                    bot.requestToJoin(group, 12)
                    s.ok("declineChatJoinRequest", "chat_id" to group, "user_id" to 12)
                    s.ok("getChatMember", "chat_id" to group, "user_id" to 12).o().str("status") shouldBe "left"
                }
            }
        }

        test("forum topics and the topic checks on send") {
            server().use { s ->
                runBlocking {
                    s.bot().addChat(group, "supergroup", "Forum", isForum = true)
                    s.error("createForumTopic", "chat_id" to USER, "name" to "x").code shouldBe 400
                    val topic = s.ok("createForumTopic", "chat_id" to group, "name" to "Ideas").o()
                    val id = topic.long("message_thread_id")!!
                    s
                        .ok(
                            "sendMessage",
                            "chat_id" to group,
                            "text" to "in topic",
                            "message_thread_id" to id,
                        ).o()
                        .long("message_thread_id") shouldBe
                        id
                    s.ok("closeForumTopic", "chat_id" to group, "message_thread_id" to id)
                    s.error("closeForumTopic", "chat_id" to group, "message_thread_id" to id).description shouldContain
                        "TOPIC_NOT_MODIFIED"
                    s
                        .error(
                            "sendMessage",
                            "chat_id" to group,
                            "text" to "x",
                            "message_thread_id" to id,
                        ).description shouldContain
                        "TOPIC_CLOSED"
                    s.ok("reopenForumTopic", "chat_id" to group, "message_thread_id" to id)
                    s.ok("editForumTopic", "chat_id" to group, "message_thread_id" to id, "name" to "Renamed")
                    s
                        .error(
                            "editForumTopic",
                            "chat_id" to group,
                            "message_thread_id" to id,
                            "name" to "Renamed",
                        ).code shouldBe
                        400
                    s.ok("deleteForumTopic", "chat_id" to group, "message_thread_id" to id)
                    s
                        .error(
                            "sendMessage",
                            "chat_id" to group,
                            "text" to "x",
                            "message_thread_id" to id,
                        ).description shouldContain
                        "message thread not found"
                    s.ok("closeGeneralForumTopic", "chat_id" to group)
                    s.error("closeGeneralForumTopic", "chat_id" to group).code shouldBe 400
                    s.ok("hideGeneralForumTopic", "chat_id" to group)
                    s.ok("unhideGeneralForumTopic", "chat_id" to group)
                    s.ok("getForumTopicIconStickers").a() shouldHaveSize 1
                }
            }
        }

        test("reactions are stored and cleared") {
            server().use { s ->
                runBlocking {
                    s.ok("sendMessage", "chat_id" to USER, "text" to "x")
                    val reaction = listOf(mapOf("type" to "emoji", "emoji" to "👍"))
                    s.ok("setMessageReaction", "chat_id" to USER, "message_id" to 1, "reaction" to reaction)
                    s.bot().reactionsOf(USER, 1)!!.a() shouldHaveSize 1
                    s.ok("setMessageReaction", "chat_id" to USER, "message_id" to 1)
                    s.bot().reactionsOf(USER, 1) shouldBe null
                    s
                        .error(
                            "setMessageReaction",
                            "chat_id" to USER,
                            "message_id" to 9,
                            "reaction" to reaction,
                        ).description shouldContain
                        "message to react not found"
                }
            }
        }

        test("sticker sets: create, edit, reorder, delete") {
            server().use { s ->
                runBlocking {
                    val suffix = "_by_${s.bot().username}"
                    fun input(n: Int) = mapOf(
                        "sticker" to "https://a.b/s$n.webp",
                        "format" to "static",
                        "emoji_list" to listOf("😀"),
                    )
                    s
                        .error(
                            "createNewStickerSet",
                            "user_id" to USER,
                            "name" to "bad",
                            "title" to "T",
                            "stickers" to listOf(input(1)),
                        ).description shouldContain
                        "invalid sticker set name"
                    s.ok(
                        "createNewStickerSet",
                        "user_id" to USER,
                        "name" to "pack$suffix",
                        "title" to "Pack",
                        "stickers" to listOf(input(1), input(2)),
                    ) shouldBe
                        TRUE
                    s
                        .error(
                            "createNewStickerSet",
                            "user_id" to USER,
                            "name" to "pack$suffix",
                            "title" to "T",
                            "stickers" to listOf(input(1)),
                        ).description shouldContain
                        "already occupied"
                    val set = s.ok("getStickerSet", "name" to "pack$suffix").o()
                    set.str("title") shouldBe "Pack"
                    set["stickers"]!!.a() shouldHaveSize 2
                    set["stickers"]!!
                        .a()
                        .first()
                        .o()
                        .str("emoji") shouldBe "😀"
                    s.ok("addStickerToSet", "user_id" to USER, "name" to "pack$suffix", "sticker" to input(3))
                    val ids = s
                        .ok(
                            "getStickerSet",
                            "name" to "pack$suffix",
                        ).o()["stickers"]!!
                        .a()
                        .map { it.o().str("file_id")!! }
                    ids shouldHaveSize 3
                    s.ok("setStickerPositionInSet", "sticker" to ids[2], "position" to 0)
                    s
                        .ok(
                            "getStickerSet",
                            "name" to "pack$suffix",
                        ).o()["stickers"]!!
                        .a()
                        .first()
                        .o()
                        .str("file_id") shouldBe
                        ids[2]
                    s.ok("setStickerEmojiList", "sticker" to ids[0], "emoji_list" to listOf("🔥"))
                    s.ok("deleteStickerFromSet", "sticker" to ids[1])
                    s.ok(
                        "replaceStickerInSet",
                        "user_id" to USER,
                        "name" to "pack$suffix",
                        "old_sticker" to ids[0],
                        "sticker" to input(4),
                    )
                    s.ok("setStickerSetTitle", "name" to "pack$suffix", "title" to "Renamed")
                    s.ok("getStickerSet", "name" to "pack$suffix").o().str("title") shouldBe "Renamed"
                    s.error("deleteStickerFromSet", "sticker" to "unknown").description shouldContain "STICKER_INVALID"
                    s
                        .ok(
                            "sendSticker",
                            "chat_id" to USER,
                            "sticker" to ids[2],
                        ).o()["sticker"]!!
                        .o()
                        .str("file_id") shouldBe
                        ids[2]
                    s.bot().addChat(group)
                    repeat(100) { s.bot().setMemberStatus(group, 1000L + it, "member") }
                    s.ok("setChatStickerSet", "chat_id" to group, "sticker_set_name" to "pack$suffix")
                    s.ok("getChat", "chat_id" to group).o().str("sticker_set_name") shouldBe "pack$suffix"
                    s.ok("deleteChatStickerSet", "chat_id" to group)
                    s.ok("deleteStickerSet", "name" to "pack$suffix")
                    s.error("getStickerSet", "name" to "pack$suffix").description shouldContain "STICKERSET_INVALID"
                }
            }
        }

        test("custom emoji sets and uploads") {
            server().use { s ->
                runBlocking {
                    val suffix = "_by_${s.bot().username}"
                    s.ok(
                        "createNewStickerSet",
                        "user_id" to USER,
                        "name" to "emo$suffix",
                        "title" to "E",
                        "sticker_type" to "custom_emoji",
                        "stickers" to
                            listOf(
                                mapOf(
                                    "sticker" to "https://a.b/e.webp",
                                    "format" to "static",
                                    "emoji_list" to listOf("🙂"),
                                ),
                            ),
                    )
                    val sticker = s
                        .ok("getStickerSet", "name" to "emo$suffix")
                        .o()["stickers"]!!
                        .a()
                        .single()
                        .o()
                    val emojiId = sticker.str("custom_emoji_id")!!
                    s.ok("getCustomEmojiStickers", "custom_emoji_ids" to listOf(emojiId, "unknown")).a() shouldHaveSize
                        1
                    val upload = UploadedFile("sticker", "s.png", "image/png", ByteArray(4))
                    val file = s.core.execute(
                        TOKEN,
                        "uploadStickerFile",
                        RequestParams(
                            params(
                                "user_id" to USER,
                                "sticker" to "attach://sticker",
                                "sticker_format" to "static",
                            ),
                            mapOf("sticker" to upload),
                        ),
                    )
                    (file as ApiResult.Ok).result.o().long("file_size") shouldBe 4L
                }
            }
        }

        test("bot profile settings") {
            server().use { s ->
                runBlocking {
                    s.ok("getMyName").o().str("name") shouldBe "Mock Bot"
                    s.ok("setMyName", "name" to "Shop")
                    s.ok("setMyName", "name" to "Boutique", "language_code" to "fr")
                    s.ok("getMyName").o().str("name") shouldBe "Shop"
                    s.ok("getMyName", "language_code" to "fr").o().str("name") shouldBe "Boutique"
                    s.ok("getMyName", "language_code" to "de").o().str("name") shouldBe "Shop"
                    s.error("setMyName", "name" to "x".repeat(65)).code shouldBe 400
                    s.ok("setMyDescription", "description" to "Hello")
                    s.ok("getMyDescription").o().str("description") shouldBe "Hello"
                    s.ok("setMyShortDescription", "short_description" to "Hi")
                    s.ok("getMyShortDescription").o().str("short_description") shouldBe "Hi"
                    s.error("setMyShortDescription", "short_description" to "x".repeat(121)).code shouldBe 400
                    s.ok("getChatMenuButton").o().str("type") shouldBe "default"
                    s.ok("setChatMenuButton", "menu_button" to mapOf("type" to "commands"))
                    s.ok("getChatMenuButton").o().str("type") shouldBe "commands"
                    val rights = Generator(
                        s.spec,
                        MutableClock(),
                    ).generateObject(
                        "ChatAdministratorRights",
                        overrides = mapOf("is_anonymous" to kotlinx.serialization.json.JsonPrimitive(true)),
                    )!!
                    s.ok("setMyDefaultAdministratorRights", "rights" to rights)
                    s.ok("getMyDefaultAdministratorRights").o().bool("is_anonymous") shouldBe true
                }
            }
        }

        test("business connections gate business methods, stories and Stars transfer") {
            server().use { s ->
                runBlocking {
                    s.error("getBusinessConnection", "business_connection_id" to "c1") shouldFailWith
                        "Bad Request: business connection not found"
                    s.error(
                        "sendMessage",
                        "chat_id" to USER,
                        "text" to "x",
                        "business_connection_id" to "c1",
                    ) shouldFailWith
                        "Bad Request: business connection not found"
                    s.bot().addBusinessConnection("c1", 77, stars = 50)
                    s.ok("getBusinessConnection", "business_connection_id" to "c1").o().str("id") shouldBe "c1"
                    s
                        .ok(
                            "sendMessage",
                            "chat_id" to USER,
                            "text" to "x",
                            "business_connection_id" to "c1",
                        ).o()
                        .str("business_connection_id") shouldBe
                        "c1"
                    s.ok("getBusinessAccountStarBalance", "business_connection_id" to "c1").o().long("amount") shouldBe
                        50L
                    s.error(
                        "transferBusinessAccountStars",
                        "business_connection_id" to "c1",
                        "star_count" to 51,
                    ) shouldFailWith
                        "Bad Request: BALANCE_TOO_LOW"
                    s.ok("transferBusinessAccountStars", "business_connection_id" to "c1", "star_count" to 20)
                    s.ok("getBusinessAccountStarBalance", "business_connection_id" to "c1").o().long("amount") shouldBe
                        30L
                    s.bot().starBalance() shouldBe 20L

                    val story = s
                        .ok(
                            "postStory",
                            "business_connection_id" to "c1",
                            "content" to mapOf("type" to "photo", "photo" to "attach://p"),
                            "active_period" to 86400,
                        ).o()
                    s
                        .error(
                            "postStory",
                            "business_connection_id" to "c1",
                            "content" to mapOf("type" to "photo", "photo" to "x"),
                            "active_period" to 5,
                        ).code shouldBe
                        400
                    val storyId = story.long("id")!!
                    s.ok(
                        "editStory",
                        "business_connection_id" to "c1",
                        "story_id" to storyId,
                        "content" to mapOf("type" to "photo", "photo" to "x"),
                    )
                    s
                        .ok(
                            "repostStory",
                            "business_connection_id" to "c1",
                            "from_chat_id" to 77,
                            "from_story_id" to storyId,
                            "active_period" to 86400,
                        ).o()
                        .long("id") shouldBe
                        storyId + 1
                    s.ok("deleteStory", "business_connection_id" to "c1", "story_id" to storyId)
                    s
                        .error(
                            "editStory",
                            "business_connection_id" to "c1",
                            "story_id" to storyId,
                            "content" to mapOf("type" to "photo", "photo" to "x"),
                        ).description shouldContain
                        "STORY_NOT_FOUND"
                }
            }
        }

        test("gifts cost Stars and land in the receiver's list") {
            server().use { s ->
                runBlocking {
                    s.ok("getAvailableGifts").o()["gifts"]!!.a() shouldHaveSize 1
                    s.error("sendGift", "user_id" to 5, "gift_id" to "mock_gift_1") shouldFailWith
                        "Bad Request: BALANCE_TOO_LOW"
                    s.error("sendGift", "user_id" to 5, "gift_id" to "nope") shouldFailWith
                        "Bad Request: invalid gift_id"
                    s.bot().setStarBalance(100)
                    s.ok("sendGift", "user_id" to 5, "gift_id" to "mock_gift_1") shouldBe TRUE
                    s.bot().starBalance() shouldBe 75L
                    s.ok("getUserGifts", "user_id" to 5).o().long("total_count") shouldBe 1L
                    s.ok("getUserGifts", "user_id" to 6).o().long("total_count") shouldBe 0L
                    s.bot().addGift("rare", 500)
                    s
                        .ok("getAvailableGifts")
                        .o()["gifts"]!!
                        .a()
                        .map { it.o().str("id") } shouldBe listOf("rare")
                }
            }
        }

        test("payments: queries, Stars income and refunds") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    val ship = bot.shippingQuery(5, "order-1")["shipping_query"]!!.o().str("id")!!
                    s.ok("getUpdates").a() shouldHaveSize 1
                    s.error("answerShippingQuery", "shipping_query_id" to ship, "ok" to true).code shouldBe 400
                    s.error("answerShippingQuery", "shipping_query_id" to ship, "ok" to false).code shouldBe 400
                    s.ok(
                        "answerShippingQuery",
                        "shipping_query_id" to ship,
                        "ok" to true,
                        "shipping_options" to
                            listOf(
                                mapOf(
                                    "id" to "a",
                                    "title" to "Post",
                                    "prices" to listOf(mapOf("label" to "p", "amount" to 100)),
                                ),
                            ),
                    ) shouldBe
                        TRUE
                    s
                        .error(
                            "answerShippingQuery",
                            "shipping_query_id" to ship,
                            "ok" to false,
                            "error_message" to "no",
                        ).description shouldContain
                        "query is too old"
                    val pre = bot.preCheckoutQuery(5, "order-1", "XTR", 40)["pre_checkout_query"]!!.o().str("id")!!
                    s.ok("answerPreCheckoutQuery", "pre_checkout_query_id" to pre, "ok" to true) shouldBe TRUE

                    val paid = bot.pay(5, 5, "order-1", "XTR", 40)
                    val charge = paid["message"]!!.o()["successful_payment"]!!.o().str("telegram_payment_charge_id")!!
                    bot.starBalance() shouldBe 40L
                    s.ok("getMyStarBalance").o().long("amount") shouldBe 40L
                    s.ok("getStarTransactions").o()["transactions"]!!.a() shouldHaveSize 1
                    s
                        .error(
                            "refundStarPayment",
                            "user_id" to 6,
                            "telegram_payment_charge_id" to charge,
                        ).description shouldContain
                        "CHARGE_ID_EMPTY"
                    s.ok("refundStarPayment", "user_id" to 5, "telegram_payment_charge_id" to charge) shouldBe TRUE
                    s
                        .error(
                            "refundStarPayment",
                            "user_id" to 5,
                            "telegram_payment_charge_id" to charge,
                        ).description shouldContain
                        "CHARGE_ALREADY_REFUNDED"
                    bot.starBalance() shouldBe 0L

                    val link = s
                        .ok(
                            "createInvoiceLink",
                            "title" to "T",
                            "description" to "D",
                            "payload" to "p",
                            "currency" to "XTR",
                            "prices" to listOf(mapOf("label" to "x", "amount" to 1)),
                        ).toString()
                        .trim('"')
                    link shouldStartWith "https://t.me/"
                }
            }
        }

        test("games keep the best score per user and rank them") {
            server().use { s ->
                runBlocking {
                    s.bot().addGame("snake", "snake", "d")
                    s.bot().addInlineMessage("im")
                    val sent = s.ok("sendGame", "chat_id" to USER, "game_short_name" to "snake").o()
                    sent["game"]!!.o().str("title") shouldBe "snake"
                    val where = arrayOf("chat_id" to USER, "message_id" to sent.long("message_id"))
                    s.ok("setGameScore", "user_id" to 1, "score" to 10, *where).o().str("edit_date").let {
                        (it != null) shouldBe
                            true
                    }
                    s.error("setGameScore", "user_id" to 1, "score" to 5, *where) shouldFailWith
                        "Bad Request: BOT_SCORE_NOT_MODIFIED"
                    s.ok("setGameScore", "user_id" to 1, "score" to 5, "force" to true, *where)
                    s.ok("setGameScore", "user_id" to 2, "score" to 20, "disable_edit_message" to true, *where) shouldBe
                        TRUE
                    val board = s.ok("getGameHighScores", "user_id" to 1, *where).a()
                    board.map { it.o().long("score") } shouldBe listOf(20L, 5L)
                    board.map { it.o().long("position") } shouldBe listOf(1L, 2L)
                    s
                        .error(
                            "setGameScore",
                            "user_id" to 1,
                            "score" to 5,
                            "chat_id" to USER,
                            "message_id" to 99,
                        ).code shouldBe
                        400
                    s.ok("setGameScore", "user_id" to 3, "score" to 1, "inline_message_id" to "im") shouldBe TRUE
                }
            }
        }

        test("checklists can be sent and edited") {
            server().use { s ->
                runBlocking {
                    s.bot().addBusinessConnection("c1", USER)
                    val checklist = mapOf(
                        "title" to "Todo",
                        "tasks" to listOf(mapOf("id" to 1, "text" to "a"), mapOf("id" to 2, "text" to "b")),
                    )
                    val sent = s
                        .ok(
                            "sendChecklist",
                            "business_connection_id" to "c1",
                            "chat_id" to USER,
                            "checklist" to checklist,
                        ).o()
                    sent["checklist"]!!.o()["tasks"]!!.a() shouldHaveSize 2
                    val edited = s
                        .ok(
                            "editMessageChecklist",
                            "business_connection_id" to "c1",
                            "chat_id" to USER,
                            "message_id" to sent.long("message_id"),
                            "checklist" to mapOf("title" to "Done", "tasks" to listOf(mapOf("id" to 1, "text" to "a"))),
                        ).o()
                    edited["checklist"]!!.o().str("title") shouldBe "Done"
                }
            }
        }

        test("managed bots and prepared messages") {
            server().use { s ->
                runBlocking {
                    val token = s.ok("getManagedBotToken", "user_id" to 9).toString().trim('"')
                    s.ok("getManagedBotToken", "user_id" to 9).toString().trim('"') shouldBe token
                    (MockCore.TOKEN_FORMAT.matches(token)) shouldBe true
                    val renewed = s.ok("replaceManagedBotToken", "user_id" to 9).toString().trim('"')
                    (renewed != token) shouldBe true
                    s.ok("getManagedBotAccessSettings", "user_id" to 9).o().bool("is_access_restricted") shouldBe false
                    s.ok(
                        "setManagedBotAccessSettings",
                        "user_id" to 9,
                        "is_access_restricted" to true,
                        "added_user_ids" to listOf(1),
                    )
                    s.ok("getManagedBotAccessSettings", "user_id" to 9).o().bool("is_access_restricted") shouldBe true
                    s
                        .ok(
                            "savePreparedInlineMessage",
                            "user_id" to 9,
                            "allow_user_chats" to true,
                            "result" to
                                mapOf(
                                    "type" to "article",
                                    "id" to "1",
                                    "title" to "t",
                                    "input_message_content" to mapOf("message_text" to "x"),
                                ),
                        ).o()
                        .str("id") shouldStartWith
                        "mock_prepared_"
                }
            }
        }

        test("text length can be counted in UTF-16 units") {
            val text = "😀".repeat(2049) // 2049 code points, 4098 UTF-16 units
            server().use { s ->
                runBlocking { s.ok("sendMessage", "chat_id" to USER, "text" to text) }
            }
            server(MockServerConfig(port = 0, textLength = TextLengthUnit.UTF16)).use { s ->
                runBlocking {
                    s.error("sendMessage", "chat_id" to USER, "text" to text) shouldFailWith
                        "Bad Request: message is too long"
                }
            }
        }

        test("urls can be fetched so files carry real bytes") {
            val http = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0).apply {
                createContext("/pic.png") { ex ->
                    ex.responseHeaders.add("Content-Type", "image/png")
                    ex.sendResponseHeaders(200, 3)
                    ex.responseBody.use { it.write(byteArrayOf(1, 2, 3)) }
                }
                createContext("/gone") { ex ->
                    ex.sendResponseHeaders(404, -1)
                    ex.close()
                }
                start()
            }
            try {
                val base = "http://127.0.0.1:${http.address.port}"
                server(MockServerConfig(port = 0, fetchUrlFiles = true)).use { s ->
                    runBlocking {
                        val doc = s
                            .ok(
                                "sendDocument",
                                "chat_id" to USER,
                                "document" to "$base/pic.png",
                            ).o()["document"]!!
                            .o()
                        doc.long("file_size") shouldBe 3L
                        doc.str("mime_type") shouldBe "image/png"
                        s.bot().fileBytes(doc.str("file_id")!!)!!.toList() shouldBe listOf<Byte>(1, 2, 3)
                        s.error("sendDocument", "chat_id" to USER, "document" to "$base/gone").code shouldBe 400
                    }
                }
            } finally {
                http.stop(0)
            }
        }
    })
