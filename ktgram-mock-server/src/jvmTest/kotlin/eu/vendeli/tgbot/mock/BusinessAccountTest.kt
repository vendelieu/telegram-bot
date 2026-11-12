package eu.vendeli.tgbot.mock

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.runBlocking

class BusinessAccountTest :
    FunSpec({
        fun server() = MockTelegramServer(MockServerConfig(port = 0))
        val conn = "business" to "c1"

        test("rights named in the method descriptions are enforced") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    bot.addBusinessConnection("c1", 77, rights = setOf("can_edit_bio"))
                    s.ok("setBusinessAccountBio", "business_connection_id" to "c1", "bio" to "hello") shouldBe TRUE
                    s
                        .error(
                            "setBusinessAccountName",
                            "business_connection_id" to "c1",
                            "first_name" to "A",
                        ).description shouldContain
                        "can_edit_name"
                    s
                        .error(
                            "sendMessage",
                            "chat_id" to 77,
                            "text" to "x",
                            "business_connection_id" to "c1",
                        ).description shouldContain
                        "can_reply"
                    s
                        .error(
                            "postStory",
                            "business_connection_id" to "c1",
                            "content" to mapOf("type" to "photo", "photo" to "x"),
                            "active_period" to 86400,
                        ).description shouldContain
                        "can_manage_stories"
                    s.error("getBusinessAccountStarBalance", "business_connection_id" to "c1").description shouldContain
                        "can_view_gifts_and_stars"
                    bot.addBusinessConnection("c2", 78)
                    s.ok("sendMessage", "chat_id" to 78, "text" to "x", "business_connection_id" to "c2")
                    s
                        .ok(
                            "getBusinessConnection",
                            "business_connection_id" to "c1",
                        ).o()["rights"]!!
                        .o()
                        .bool("can_edit_bio") shouldBe
                        true
                    s
                        .ok(
                            "getBusinessConnection",
                            "business_connection_id" to "c1",
                        ).o()["rights"]!!
                        .o()
                        .bool("can_reply") shouldBe
                        false
                }
            }
        }

        test("name, username, bio, photo and gift settings change the account") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    bot.addBusinessConnection("c1", 77)
                    bot.addBusinessConnection("c2", 78)
                    s.ok(
                        "setBusinessAccountName",
                        "business_connection_id" to "c1",
                        "first_name" to "Ann",
                        "last_name" to "Lee",
                    )
                    val user = s.ok("getBusinessConnection", "business_connection_id" to "c1").o()["user"]!!.o()
                    user.str("first_name") shouldBe "Ann"
                    user.str("last_name") shouldBe "Lee"
                    s
                        .error(
                            "setBusinessAccountName",
                            "business_connection_id" to "c1",
                            "first_name" to "",
                        ).code shouldBe
                        400

                    s.ok("setBusinessAccountUsername", "business_connection_id" to "c1", "username" to "ann_shop")
                    bot.businessAccount("c1").str("username") shouldBe "ann_shop"
                    s
                        .ok(
                            "getBusinessConnection",
                            "business_connection_id" to "c1",
                        ).o()["user"]!!
                        .o()
                        .str("username") shouldBe
                        "ann_shop"
                    s
                        .error(
                            "setBusinessAccountUsername",
                            "business_connection_id" to "c1",
                            "username" to "x",
                        ).description shouldContain
                        "USERNAME_INVALID"
                    s
                        .error(
                            "setBusinessAccountUsername",
                            "business_connection_id" to "c2",
                            "username" to "ANN_shop",
                        ).description shouldContain
                        "USERNAME_OCCUPIED"
                    s.ok("setBusinessAccountUsername", "business_connection_id" to "c1")
                    bot.businessAccount("c1").str("username") shouldBe null

                    s.ok("setBusinessAccountBio", "business_connection_id" to "c1", "bio" to "We sell")
                    bot.businessAccount("c1").str("bio") shouldBe "We sell"
                    s
                        .error(
                            "setBusinessAccountBio",
                            "business_connection_id" to "c1",
                            "bio" to "x".repeat(141),
                        ).description shouldContain
                        "BIO_TOO_LONG"
                    s.ok("setBusinessAccountBio", "business_connection_id" to "c1")
                    bot.businessAccount("c1").str("bio") shouldBe null

                    s
                        .error(
                            "setBusinessAccountProfilePhoto",
                            "business_connection_id" to "c1",
                            "photo" to mapOf("type" to "static", "photo" to "attach://p"),
                        ).description shouldContain
                        "wrong file identifier"
                    val upload = UploadedFile("p", "p.jpg", "image/jpeg", ByteArray(2))
                    val set = s.core.execute(
                        TOKEN,
                        "setBusinessAccountProfilePhoto",
                        RequestParams(
                            params(
                                "business_connection_id" to "c1",
                                "photo" to mapOf("type" to "static", "photo" to "attach://p"),
                                "is_public" to true,
                            ),
                            mapOf("p" to upload),
                        ),
                    )
                    (set is ApiResult.Ok) shouldBe true
                    bot.businessAccount("c1")["photo"]!!.o().bool("is_public") shouldBe true
                    s.ok("removeBusinessAccountProfilePhoto", "business_connection_id" to "c1")
                    bot.businessAccount("c1")["photo"] shouldBe null

                    val types = mapOf(
                        "unlimited_gifts" to true,
                        "limited_gifts" to false,
                        "unique_gifts" to true,
                        "premium_subscription" to false,
                        "gifts_from_channels" to false,
                    )
                    s.ok(
                        "setBusinessAccountGiftSettings",
                        "business_connection_id" to "c1",
                        "show_gift_button" to true,
                        "accepted_gift_types" to types,
                    )
                    bot
                        .businessAccount(
                            "c1",
                        )["gift_settings"]!!
                        .o()["accepted_gift_types"]!!
                        .o()
                        .bool("limited_gifts") shouldBe
                        false
                }
            }
        }

        test("business messages can be read and deleted according to rights") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    bot.addBusinessConnection(
                        "c1",
                        77,
                        rights = setOf("can_read_messages", "can_delete_sent_messages", "can_reply"),
                    )
                    val update = bot.userSays("hi", fromId = 77, businessConnectionId = "c1")
                    (update["business_message"] != null) shouldBe true
                    val incoming = update["business_message"]!!.o().long("message_id")!!
                    s.ok(
                        "readBusinessMessage",
                        "business_connection_id" to "c1",
                        "chat_id" to 77,
                        "message_id" to incoming,
                    ) shouldBe
                        TRUE
                    bot.isRead(77, incoming) shouldBe true
                    s
                        .error(
                            "readBusinessMessage",
                            "business_connection_id" to "c1",
                            "chat_id" to 77,
                            "message_id" to 99,
                        ).description shouldContain
                        "message not found"

                    val own = s
                        .ok(
                            "sendMessage",
                            "chat_id" to 77,
                            "text" to "reply",
                            "business_connection_id" to "c1",
                        ).o()
                        .long("message_id")!!
                    s
                        .error(
                            "deleteBusinessMessages",
                            "business_connection_id" to "c1",
                            "message_ids" to listOf(incoming),
                        ).description shouldContain
                        "can_delete_all_messages"
                    s.ok(
                        "deleteBusinessMessages",
                        "business_connection_id" to "c1",
                        "message_ids" to listOf(own),
                    ) shouldBe
                        TRUE
                    bot.messages(77) shouldHaveSize 1
                    s
                        .error(
                            "deleteBusinessMessages",
                            "business_connection_id" to "c1",
                            "message_ids" to listOf(own),
                        ).description shouldContain
                        "MESSAGE_ID_INVALID"
                }
            }
        }

        test("sticker keywords and mask positions are stored") {
            server().use { s ->
                runBlocking {
                    val suffix = "_by_${s.bot().username}"
                    fun input(n: Int) = mapOf(
                        "sticker" to "https://a.b/s$n.webp",
                        "format" to "static",
                        "emoji_list" to listOf("😀"),
                    )
                    s.ok(
                        "createNewStickerSet",
                        "user_id" to USER,
                        "name" to "reg$suffix",
                        "title" to "R",
                        "stickers" to listOf(input(1)),
                    )
                    s.ok(
                        "createNewStickerSet",
                        "user_id" to USER,
                        "name" to "msk$suffix",
                        "title" to "M",
                        "sticker_type" to "mask",
                        "stickers" to listOf(input(2)),
                    )
                    val regular = s
                        .ok(
                            "getStickerSet",
                            "name" to "reg$suffix",
                        ).o()["stickers"]!!
                        .a()
                        .single()
                        .o()
                        .str("file_id")!!
                    val mask = s
                        .ok(
                            "getStickerSet",
                            "name" to "msk$suffix",
                        ).o()["stickers"]!!
                        .a()
                        .single()
                        .o()
                        .str("file_id")!!

                    s.ok("setStickerKeywords", "sticker" to regular, "keywords" to listOf("smile", "happy"))
                    s.bot().stickerKeywords(regular) shouldBe listOf("smile", "happy")
                    s
                        .error(
                            "setStickerKeywords",
                            "sticker" to regular,
                            "keywords" to List(21) { "k$it" },
                        ).description shouldContain
                        "KEYWORDS_INVALID"
                    s
                        .error(
                            "setStickerKeywords",
                            "sticker" to mask,
                            "keywords" to listOf("a"),
                        ).description shouldContain
                        "STICKER_TYPE_INVALID"
                    s.ok("setStickerKeywords", "sticker" to regular)
                    s.bot().stickerKeywords(regular) shouldHaveSize 0

                    val position = mapOf("point" to "eyes", "x_shift" to 0.5, "y_shift" to 0.0, "scale" to 1.0)
                    s.ok("setStickerMaskPosition", "sticker" to mask, "mask_position" to position)
                    s
                        .ok(
                            "getStickerSet",
                            "name" to "msk$suffix",
                        ).o()["stickers"]!!
                        .a()
                        .single()
                        .o()["mask_position"]!!
                        .o()
                        .str("point") shouldBe
                        "eyes"
                    s.ok("setStickerMaskPosition", "sticker" to mask)
                    s
                        .ok(
                            "getStickerSet",
                            "name" to "msk$suffix",
                        ).o()["stickers"]!!
                        .a()
                        .single()
                        .o()["mask_position"] shouldBe
                        null
                    s
                        .error(
                            "setStickerMaskPosition",
                            "sticker" to regular,
                            "mask_position" to position,
                        ).description shouldContain
                        "STICKER_MASK_COORDS_NOT_SUPPORTED"
                }
            }
        }

        test("the bot's own profile photo shows up in getUserProfilePhotos") {
            server().use { s ->
                runBlocking {
                    val bot = s.bot()
                    s.ok("getUserProfilePhotos", "user_id" to bot.id).o().long("total_count") shouldBe 0L
                    val upload = UploadedFile("p", "p.jpg", "image/jpeg", ByteArray(2))
                    val set = s.core.execute(
                        TOKEN,
                        "setMyProfilePhoto",
                        RequestParams(
                            params("photo" to mapOf("type" to "static", "photo" to "attach://p")),
                            mapOf("p" to upload),
                        ),
                    )
                    (set is ApiResult.Ok) shouldBe true
                    bot.hasProfilePhoto() shouldBe true
                    s.ok("getUserProfilePhotos", "user_id" to bot.id).o().long("total_count") shouldBe 1L
                    s.error("setMyProfilePhoto", "photo" to mapOf("type" to "oops")).description shouldContain
                        "InputProfilePhoto expected"
                    s.ok("removeMyProfilePhoto")
                    bot.hasProfilePhoto() shouldBe false
                }
            }
        }

        test("share theme backdrops come from a catalogue the test can replace") {
            server().use { s ->
                runBlocking {
                    s.ok("getChatShareThemeBackdrops").o()["backdrops"]!!.a() shouldHaveSize 2
                    val custom = Generator(
                        s.spec,
                        MutableClock(),
                    ).generateObject(
                        "ChatShareThemeBackdrop",
                        overrides = mapOf("id" to kotlinx.serialization.json.JsonPrimitive(9)),
                    )!!
                    s.bot().setShareThemeBackdrops(listOf(custom))
                    s
                        .ok("getChatShareThemeBackdrops")
                        .o()["backdrops"]!!
                        .a()
                        .single()
                        .o()
                        .long("id") shouldBe 9L
                }
            }
        }
    })
