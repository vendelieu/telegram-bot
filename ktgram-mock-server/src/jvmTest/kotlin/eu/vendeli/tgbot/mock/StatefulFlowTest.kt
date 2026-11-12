package eu.vendeli.tgbot.mock

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.runBlocking

class StatefulFlowTest :
    FunSpec({
        fun newServer(config: MockServerConfig = MockServerConfig(port = 0)) = MockTelegramServer(config)

        test("sendMessage stores the message and numbers messages per chat") {
            newServer().use { server ->
                runBlocking {
                    val first = server
                        .ok(
                            "sendMessage",
                            "chat_id" to USER,
                            "text" to "hi <b>x</b>",
                            "parse_mode" to "HTML",
                        ).o()
                    val second = server.ok("sendMessage", "chat_id" to USER, "text" to "again").o()
                    first.long("message_id") shouldBe 1L
                    second.long("message_id") shouldBe 2L
                    first.str("text") shouldBe "hi x"
                    first.o()["entities"]!!.a() shouldHaveSize 1
                    first.o()["from"]!!.o().bool("is_bot") shouldBe true
                    server.bot().messages(USER) shouldHaveSize 2
                }
            }
        }

        test("text limits") {
            newServer().use { server ->
                runBlocking {
                    server.error("sendMessage", "chat_id" to USER, "text" to "a".repeat(4097)) shouldFailWith
                        "Bad Request: message is too long"
                    server.ok("sendMessage", "chat_id" to USER, "text" to "a".repeat(4096))
                    server.error("sendMessage", "chat_id" to USER, "text" to "  ") shouldFailWith
                        "Bad Request: message text is empty"
                    server.error(
                        "sendPhoto",
                        "chat_id" to USER,
                        "photo" to "https://a.b/p.jpg",
                        "caption" to "c".repeat(1025),
                    ) shouldFailWith
                        "Bad Request: message caption is too long"
                }
            }
        }

        test("edit changes the stored message, repeats are not modified, foreign messages are not editable") {
            newServer().use { server ->
                runBlocking {
                    server.ok("sendMessage", "chat_id" to USER, "text" to "v1")
                    val edited = server.ok("editMessageText", "chat_id" to USER, "message_id" to 1, "text" to "v2").o()
                    edited.str("text") shouldBe "v2"
                    (edited["edit_date"] != null) shouldBe true
                    server
                        .error(
                            "editMessageText",
                            "chat_id" to USER,
                            "message_id" to 1,
                            "text" to "v2",
                        ).description shouldContain
                        "message is not modified"
                    server.error("editMessageText", "chat_id" to USER, "message_id" to 99, "text" to "x") shouldFailWith
                        "Bad Request: message to edit not found"
                    server.bot().userSays("user text", fromId = USER)
                    server.error("editMessageText", "chat_id" to USER, "message_id" to 2, "text" to "x") shouldFailWith
                        "Bad Request: message can't be edited"
                    server.bot().addInlineMessage("abc")
                    server.ok("editMessageText", "inline_message_id" to "abc", "text" to "x") shouldBe TRUE
                }
            }
        }

        test("reply markup is stored, edited and removed") {
            newServer().use { server ->
                runBlocking {
                    val kb = mapOf("inline_keyboard" to listOf(listOf(mapOf("text" to "b", "callback_data" to "d"))))
                    server
                        .ok(
                            "sendMessage",
                            "chat_id" to USER,
                            "text" to "k",
                            "reply_markup" to kb,
                        ).o()["reply_markup"] shouldBe
                        jsonOf(kb)
                    server
                        .error(
                            "editMessageReplyMarkup",
                            "chat_id" to USER,
                            "message_id" to 1,
                            "reply_markup" to kb,
                        ).description shouldContain
                        "message is not modified"
                    server
                        .ok(
                            "editMessageReplyMarkup",
                            "chat_id" to USER,
                            "message_id" to 1,
                        ).o()["reply_markup"] shouldBe
                        null
                }
            }
        }

        test("delete removes the message and a second delete fails") {
            newServer().use { server ->
                runBlocking {
                    server.ok("sendMessage", "chat_id" to USER, "text" to "x")
                    server.ok("deleteMessage", "chat_id" to USER, "message_id" to 1) shouldBe TRUE
                    server.error("deleteMessage", "chat_id" to USER, "message_id" to 1) shouldFailWith
                        "Bad Request: message to delete not found"
                    server.bot().messages(USER) shouldHaveSize 0
                }
            }
        }

        test("replying to a missing message fails unless sending without reply is allowed") {
            newServer().use { server ->
                runBlocking {
                    server.error(
                        "sendMessage",
                        "chat_id" to USER,
                        "text" to "r",
                        "reply_parameters" to mapOf("message_id" to 7),
                    ) shouldFailWith
                        "Bad Request: message to be replied not found"
                    server.ok(
                        "sendMessage",
                        "chat_id" to USER,
                        "text" to "r",
                        "reply_parameters" to mapOf("message_id" to 7, "allow_sending_without_reply" to true),
                    )
                    server.ok("sendMessage", "chat_id" to USER, "text" to "second")
                    val reply = server
                        .ok(
                            "sendMessage",
                            "chat_id" to USER,
                            "text" to "re",
                            "reply_parameters" to mapOf("message_id" to 2),
                        ).o()
                    reply["reply_to_message"]!!.o().str("text") shouldBe "second"
                }
            }
        }

        test("forward and copy create new messages in the target chat") {
            newServer().use { server ->
                runBlocking {
                    server.ok("sendMessage", "chat_id" to USER, "text" to "orig")
                    val fwd = server.ok("forwardMessage", "chat_id" to 5, "from_chat_id" to USER, "message_id" to 1).o()
                    fwd["forward_origin"]!!.o().str("type") shouldBe "user"
                    fwd["chat"]!!.o().long("id") shouldBe 5L
                    val cp = server.ok("copyMessage", "chat_id" to 6, "from_chat_id" to USER, "message_id" to 1).o()
                    cp.long("message_id") shouldBe 1L
                    server
                        .bot()
                        .messages(6)
                        .single()
                        .str("text") shouldBe "orig"
                    server.bot().messages(6).single()["forward_origin"] shouldBe null
                    server.error(
                        "forwardMessage",
                        "chat_id" to 5,
                        "from_chat_id" to USER,
                        "message_id" to 50,
                    ) shouldFailWith
                        "Bad Request: message to forward not found"
                    server
                        .ok(
                            "copyMessages",
                            "chat_id" to 7,
                            "from_chat_id" to USER,
                            "message_ids" to listOf(1, 2),
                        ).a() shouldHaveSize
                        1
                    server
                        .ok(
                            "forwardMessages",
                            "chat_id" to 8,
                            "from_chat_id" to USER,
                            "message_ids" to listOf(1),
                        ).a() shouldHaveSize
                        1
                }
            }
        }

        test("media: file ids, urls and unknown ids") {
            newServer().use { server ->
                runBlocking {
                    val sent = server
                        .ok(
                            "sendPhoto",
                            "chat_id" to USER,
                            "photo" to "https://a.b/pic.jpg",
                            "caption" to "c",
                        ).o()
                    val fileId = sent["photo"]!!
                        .a()
                        .first()
                        .o()
                        .str("file_id")!!
                    server
                        .ok(
                            "sendPhoto",
                            "chat_id" to USER,
                            "photo" to fileId,
                        ).o()["photo"]!!
                        .a()
                        .first()
                        .o()
                        .str("file_id") shouldBe
                        fileId
                    server.error("sendPhoto", "chat_id" to USER, "photo" to "garbage") shouldFailWith
                        "Bad Request: wrong file identifier/HTTP URL specified"
                    val doc = server.ok("sendDocument", "chat_id" to USER, "document" to "https://a.b/f.pdf").o()
                    doc["document"]!!.o().str("file_name") shouldBe "f.pdf"
                    server
                        .ok(
                            "sendVideo",
                            "chat_id" to USER,
                            "video" to "https://a.b/v.mp4",
                            "duration" to 9,
                        ).o()["video"]!!
                        .o()
                        .long("duration") shouldBe
                        9L
                    server
                        .ok(
                            "editMessageCaption",
                            "chat_id" to USER,
                            "message_id" to 1,
                            "caption" to "new",
                        ).o()
                        .str("caption") shouldBe
                        "new"
                    server.error("editMessageText", "chat_id" to USER, "message_id" to 1, "text" to "x") shouldFailWith
                        "Bad Request: there is no text in the message to edit"
                }
            }
        }

        test("uploads get a file id, can be fetched with getFile and downloaded") {
            newServer().use { server ->
                runBlocking {
                    val upload = UploadedFile("document", "report.txt", "text/plain", "hello".toByteArray())
                    val result = server.core.execute(
                        TOKEN,
                        "sendDocument",
                        RequestParams(
                            params(
                                "chat_id" to USER,
                                "document" to "attach://document",
                            ),
                            mapOf("document" to upload),
                        ),
                    )
                    val doc = (result as ApiResult.Ok).result.o()["document"]!!.o()
                    doc.str("file_name") shouldBe "report.txt"
                    doc.long("file_size") shouldBe 5L
                    val file = server.ok("getFile", "file_id" to doc.str("file_id")).o()
                    file.str("file_path") shouldContain "documents/"
                    server.bot().fileBytes(doc.str("file_id")!!)!!.decodeToString() shouldBe "hello"
                    server.error("getFile", "file_id" to "nope") shouldFailWith
                        "Bad Request: wrong file_id or the file is temporarily unavailable"
                }
            }
        }

        test("media groups return one message per item sharing a media_group_id") {
            newServer().use { server ->
                runBlocking {
                    val media =
                        listOf(
                            mapOf("type" to "photo", "media" to "https://a.b/1.jpg", "caption" to "c"),
                            mapOf("type" to "photo", "media" to "https://a.b/2.jpg"),
                        )
                    val sent = server.ok("sendMediaGroup", "chat_id" to USER, "media" to media).a()
                    sent shouldHaveSize 2
                    sent.map { it.o().str("media_group_id") }.toSet() shouldHaveSize 1
                    server.error("sendMediaGroup", "chat_id" to USER, "media" to media.take(1)).code shouldBe 400
                }
            }
        }

        test("location, venue, contact, dice, poll and invoice carry their payload") {
            newServer().use { server ->
                runBlocking {
                    server
                        .ok(
                            "sendLocation",
                            "chat_id" to USER,
                            "latitude" to 1.5,
                            "longitude" to 2.5,
                        ).o()["location"]!!
                        .o()
                        .str("latitude") shouldBe
                        "1.5"
                    server
                        .ok(
                            "sendVenue",
                            "chat_id" to USER,
                            "latitude" to 1.5,
                            "longitude" to 2.5,
                            "title" to "T",
                            "address" to "A",
                        ).o()["venue"]!!
                        .o()
                        .str("title") shouldBe
                        "T"
                    server
                        .ok(
                            "sendContact",
                            "chat_id" to USER,
                            "phone_number" to "+1",
                            "first_name" to "N",
                        ).o()["contact"]!!
                        .o()
                        .str("phone_number") shouldBe
                        "+1"
                    server
                        .ok("sendDice", "chat_id" to USER)
                        .o()["dice"]!!
                        .o()
                        .str("emoji") shouldBe "🎲"
                    server.error("sendDice", "chat_id" to USER, "emoji" to "x") shouldFailWith
                        "Bad Request: invalid dice emoji specified"
                    val poll = server
                        .ok(
                            "sendPoll",
                            "chat_id" to USER,
                            "question" to "q?",
                            "options" to listOf(mapOf("text" to "a"), mapOf("text" to "b")),
                        ).o()
                    poll["poll"]!!.o()["options"]!!.a() shouldHaveSize 2
                    server.error(
                        "sendPoll",
                        "chat_id" to USER,
                        "question" to "q?",
                        "options" to listOf(mapOf("text" to "a")),
                    ) shouldFailWith
                        "Bad Request: poll must have at least 2 options"
                    val stopped = server.ok("stopPoll", "chat_id" to USER, "message_id" to poll.long("message_id")).o()
                    stopped.bool("is_closed") shouldBe true
                    server.error("stopPoll", "chat_id" to USER, "message_id" to poll.long("message_id")) shouldFailWith
                        "Bad Request: poll has already been closed"
                }
            }
        }

        test("live location can be moved and stopped") {
            newServer().use { server ->
                runBlocking {
                    val sent = server
                        .ok(
                            "sendLocation",
                            "chat_id" to USER,
                            "latitude" to 1.0,
                            "longitude" to 2.0,
                            "live_period" to 60,
                        ).o()
                    val id = sent.long("message_id")
                    server
                        .ok(
                            "editMessageLiveLocation",
                            "chat_id" to USER,
                            "message_id" to id,
                            "latitude" to 3.0,
                            "longitude" to 4.0,
                        ).o()["location"]!!
                        .o()
                        .str("latitude") shouldBe
                        "3.0"
                    server
                        .ok(
                            "stopMessageLiveLocation",
                            "chat_id" to USER,
                            "message_id" to id,
                        ).o()["location"]!!
                        .o()["live_period"] shouldBe
                        null
                }
            }
        }

        test("a bot that was kicked or left gets the Forbidden errors of the real Bot API server") {
            newServer(MockServerConfig(port = 0)).use { server ->
                runBlocking {
                    val bot = server.bot()
                    bot.addChat(-500, "supergroup", "g")
                    bot.addChat(-600, "channel", "c")
                    bot.setMemberStatus(-500, bot.id, "kicked")
                    bot.setMemberStatus(-600, bot.id, "left")
                    server.error("sendMessage", "chat_id" to -500, "text" to "x") shouldFailWith
                        "Forbidden: bot was kicked from the supergroup chat"
                    server.error("sendMessage", "chat_id" to -600, "text" to "x") shouldFailWith
                        "Forbidden: bot is not a member of the channel chat"
                    bot.setMemberStatus(-500, bot.id, "administrator")
                    server.ok("sendMessage", "chat_id" to -500, "text" to "x")
                }
            }
        }

        test("blocked users, unknown chats and flood limits") {
            newServer(MockServerConfig(port = 0, unknownChats = UnknownChatPolicy.REJECT)).use { server ->
                runBlocking {
                    server.error("sendMessage", "chat_id" to 77, "text" to "x") shouldFailWith
                        "Bad Request: chat not found"
                    server.bot().userSays("hi", fromId = 77)
                    server.ok("sendMessage", "chat_id" to 77, "text" to "x")
                    server.bot().blockedBy(77)
                    server.error("sendMessage", "chat_id" to 77, "text" to "x") shouldFailWith
                        "Forbidden: bot was blocked by the user"
                }
            }
            val clock = MutableClock()
            newServer(
                MockServerConfig(port = 0, clock = clock, rateLimit = RateLimitConfig(perChatPerSecond = 1)),
            ).use { server ->
                runBlocking {
                    server.ok("sendMessage", "chat_id" to USER, "text" to "1")
                    val limited = server.error("sendMessage", "chat_id" to USER, "text" to "2")
                    limited.code shouldBe 429
                    limited.parameters!!.long("retry_after") shouldBe 1L
                    clock.advanceMillis(1100)
                    server.ok("sendMessage", "chat_id" to USER, "text" to "3")
                    server.ok("sendMessage", "chat_id" to 5, "text" to "other chat is independent")
                }
            }
        }

        test("chat members, bans, admins and pins") {
            newServer().use { server ->
                runBlocking {
                    val bot = server.bot()
                    bot.addChat(-100, "supergroup", "G")
                    bot.addUser(10, "Ann")
                    bot.setMemberStatus(-100, 10, "member")
                    server.ok("getChatMember", "chat_id" to -100, "user_id" to 10).o().str("status") shouldBe "member"
                    server.ok("banChatMember", "chat_id" to -100, "user_id" to 10) shouldBe TRUE
                    server.ok("getChatMember", "chat_id" to -100, "user_id" to 10).o().str("status") shouldBe "kicked"
                    server.ok("unbanChatMember", "chat_id" to -100, "user_id" to 10)
                    server.ok("getChatMember", "chat_id" to -100, "user_id" to 10).o().str("status") shouldBe "left"
                    server.ok("promoteChatMember", "chat_id" to -100, "user_id" to 10)
                    server.ok("getChatAdministrators", "chat_id" to -100).a() shouldHaveSize 1
                    server.error("getChatMember", "chat_id" to -100, "user_id" to 999) shouldFailWith
                        "Bad Request: user not found"
                    server.error("banChatMember", "chat_id" to USER, "user_id" to 10).code shouldBe 400
                    server.ok("getChatMemberCount", "chat_id" to USER).toString() shouldBe "2"

                    server.ok("sendMessage", "chat_id" to -100, "text" to "pin me")
                    server.ok("pinChatMessage", "chat_id" to -100, "message_id" to 1)
                    server
                        .ok("getChat", "chat_id" to -100)
                        .o()["pinned_message"]!!
                        .o()
                        .str("text") shouldBe "pin me"
                    server.ok("unpinAllChatMessages", "chat_id" to -100)
                    server.ok("getChat", "chat_id" to -100).o()["pinned_message"] shouldBe null
                    server.error("pinChatMessage", "chat_id" to -100, "message_id" to 99) shouldFailWith
                        "Bad Request: message to pin not found"
                }
            }
        }

        test("bot commands, getMe, callbacks and chat actions") {
            newServer().use { server ->
                runBlocking {
                    server.ok("getMe").o().str("username") shouldBe server.bot().username
                    server.ok("getMyCommands").a() shouldHaveSize 0
                    server.ok(
                        "setMyCommands",
                        "commands" to listOf(mapOf("command" to "start", "description" to "Start")),
                    )
                    server.ok("getMyCommands").a() shouldHaveSize 1
                    server.error(
                        "setMyCommands",
                        "commands" to listOf(mapOf("command" to "Bad Cmd", "description" to "d")),
                    ) shouldFailWith
                        "Bad Request: BOT_COMMAND_INVALID"
                    server.ok("deleteMyCommands")
                    server.ok("getMyCommands").a() shouldHaveSize 0

                    server.ok("sendChatAction", "chat_id" to USER, "action" to "typing")
                    server.error("sendChatAction", "chat_id" to USER, "action" to "dancing") shouldFailWith
                        "Bad Request: wrong parameter action in request"

                    server.ok("sendMessage", "chat_id" to USER, "text" to "k")
                    val update = server.bot().pressButton(USER, 1, "d")
                    val id = update["callback_query"]!!.o().str("id")!!
                    server.ok("answerCallbackQuery", "callback_query_id" to id) shouldBe TRUE
                    server.error("answerCallbackQuery", "callback_query_id" to id).description shouldContain
                        "query is too old"
                }
            }
        }

        test("stubs and faults override everything") {
            newServer().use { server ->
                runBlocking {
                    val bot = server.bot()
                    bot.stub("getMe", params("id" to 1, "is_bot" to true, "first_name" to "Stub"))
                    server.ok("getMe").o().str("first_name") shouldBe "Stub"
                    bot.stubError("sendMessage", ApiException.badRequest("custom"), match = params("text" to "boom"))
                    server.error("sendMessage", "chat_id" to USER, "text" to "boom") shouldFailWith
                        "Bad Request: custom"
                    server.ok("sendMessage", "chat_id" to USER, "text" to "fine")
                    bot.rateLimit("sendMessage", retryAfter = 3)
                    server
                        .error(
                            "sendMessage",
                            "chat_id" to USER,
                            "text" to "x",
                        ).parameters!!
                        .long("retry_after") shouldBe
                        3L
                    server.ok("sendMessage", "chat_id" to USER, "text" to "recovered")
                    bot.calls("sendMessage").map { it.status } shouldBe listOf(400, 200, 429, 200)
                }
            }
        }
    })
