package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.keyboard.ForceReply
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.types.keyboard.KeyboardButtonPollType
import eu.vendeli.tgbot.types.keyboard.KeyboardButtonRequestChat
import eu.vendeli.tgbot.types.keyboard.KeyboardButtonRequestUsers
import eu.vendeli.tgbot.types.keyboard.LoginUrl
import eu.vendeli.tgbot.types.keyboard.ReplyKeyboardMarkup
import eu.vendeli.tgbot.utils.builders.inlineKeyboardMarkup
import eu.vendeli.tgbot.utils.builders.replyKeyboardMarkup
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class MarkupBuilderTest : BotTestContext() {
    @Test
    fun `building inline keyboard markup`() {
        val callbackMarkup = inlineKeyboardMarkup {
            "test" callback "testCallback"
        }

        callbackMarkup.inlineKeyboard shouldHaveSize 1
        callbackMarkup.inlineKeyboard.first() shouldHaveSize 1
        callbackMarkup.inlineKeyboard
            .first()
            .first()
            .callbackData shouldBe "testCallback"

        val newLinedMarkup = inlineKeyboardMarkup {
            "test1" url "testUrl"
            "test2" webAppInfo "webAppInfoUrl"
            newLine()
            "test3" loginUrl LoginUrl("loginUrl")
            "test4" switchInlineQuery "switchInlineQueryTest"
        }

        newLinedMarkup.inlineKeyboard shouldHaveSize 2 // two rows

        newLinedMarkup.inlineKeyboard.run {
            // 1 row 1 element
            first().first().text shouldBe "test1"
            first().first().url shouldBe "testUrl"
            first().first().callbackData.shouldBeNull()
            first().first().webApp.shouldBeNull()
            first().first().loginUrl.shouldBeNull()
            first().first().switchInlineQuery.shouldBeNull()
            first().first().callbackGame.shouldBeNull()
            first().first().pay.shouldBeNull()

            // 1 row 2 element
            first()[1].text shouldBe "test2"
            first()[1].webApp?.url shouldBe "webAppInfoUrl"
            first()[1].callbackData.shouldBeNull()
            first()[1].url.shouldBeNull()
            first()[1].loginUrl.shouldBeNull()
            first()[1].switchInlineQuery.shouldBeNull()
            first()[1].callbackGame.shouldBeNull()
            first()[1].pay.shouldBeNull()

            // 2 row 1 element
            get(1).first().text shouldBe "test3"
            get(1).first().loginUrl?.url shouldBe "loginUrl"
            get(1).first().callbackData.shouldBeNull()
            get(1).first().webApp.shouldBeNull()
            get(1).first().url.shouldBeNull()
            get(1).first().switchInlineQuery.shouldBeNull()
            get(1).first().callbackGame.shouldBeNull()
            get(1).first().pay.shouldBeNull()

            // 2 row 2 element
            get(1)[1].text shouldBe "test4"
            get(1)[1].switchInlineQuery shouldBe "switchInlineQueryTest"
            get(1)[1].callbackData.shouldBeNull()
            get(1)[1].webApp.shouldBeNull()
            get(1)[1].url.shouldBeNull()
            get(1)[1].loginUrl.shouldBeNull()
            get(1)[1].callbackGame.shouldBeNull()
            get(1)[1].pay.shouldBeNull()
        }
    }

    @Test
    @Suppress("LongMethod")
    fun `building reply keyboard markup`() {
        val replyMarkup = replyKeyboardMarkup {
            "test" requestContact true
        }

        replyMarkup.keyboard shouldHaveSize 1

        val fullyBuiltKeyboard = replyKeyboardMarkup {
            options {
                resizeKeyboard = true
                oneTimeKeyboard = false
                isPersistent = true
                inputFieldPlaceholder = "test"
            }

            "test1" requestLocation true
            "test2" requestPoll KeyboardButtonPollType()
            newLine()
            "test3" webApp "test"
            "test4" requestContact false
            br()
            "test5" requestUser KeyboardButtonRequestUsers(1, userIsBot = true, userIsPremium = false)
            "test6" requestChat KeyboardButtonRequestChat(2, chatIsChannel = false, chatIsForum = true)
        }

        fullyBuiltKeyboard.keyboard shouldHaveSize 3 // three rows

        fullyBuiltKeyboard.keyboard.run {
            // 1 row 1 element
            val row1el1 = first().first()
            row1el1.text shouldBe "test1"

            row1el1.requestLocation shouldBe true

            row1el1.requestChat.shouldBeNull()
            row1el1.requestUsers.shouldBeNull()
            row1el1.requestPoll.shouldBeNull()
            row1el1.requestContact.shouldBeNull()
            row1el1.webApp.shouldBeNull()

            // 1 row 2 element
            val row1el2 = first()[1]
            row1el2.text shouldBe "test2"

            row1el2.requestPoll.shouldNotBeNull()
            row1el2.requestPoll?.type.shouldBeNull()

            row1el2.requestChat.shouldBeNull()
            row1el2.requestUsers.shouldBeNull()
            row1el2.requestLocation.shouldBeNull()
            row1el2.requestContact.shouldBeNull()
            row1el2.webApp.shouldBeNull()

            // 2 row 1 element
            val row2el1 = get(1).first()
            row2el1.text shouldBe "test3"

            row2el1.webApp.shouldNotBeNull()
            row2el1.webApp?.url shouldBe "test"

            row2el1.requestChat.shouldBeNull()
            row2el1.requestUsers.shouldBeNull()
            row2el1.requestLocation.shouldBeNull()
            row2el1.requestContact.shouldBeNull()
            row2el1.requestPoll.shouldBeNull()

            // 2 row 2 element
            val row2el2 = get(1)[1]
            row2el2.text shouldBe "test4"
            row2el2.webApp.shouldBeNull()
            row2el2.requestLocation.shouldBeNull()
            row2el2.requestPoll.shouldBeNull()
            row2el2.requestChat.shouldBeNull()
            row2el2.requestUsers.shouldBeNull()

            row2el2.requestContact.shouldNotBeNull()
            row2el2.requestContact shouldBe false

            // 3 row 1 element
            val row3el1 = get(2).first()
            row3el1.text shouldBe "test5"
            row3el1.webApp.shouldBeNull()
            row3el1.requestLocation.shouldBeNull()
            row3el1.requestContact.shouldBeNull()
            row3el1.requestContact.shouldBeNull()
            row3el1.requestPoll.shouldBeNull()
            row3el1.requestChat.shouldBeNull()

            row3el1.requestUsers.shouldNotBeNull()
            row3el1.requestUsers?.requestId shouldBe 1
            row3el1.requestUsers?.userIsBot shouldBe true
            row3el1.requestUsers?.userIsPremium shouldBe false

            // 3 row 2 element
            val row3el2 = get(2)[1]
            row3el2.text shouldBe "test6"
            row3el2.webApp.shouldBeNull()
            row3el2.requestLocation.shouldBeNull()
            row3el2.requestContact.shouldBeNull()
            row3el2.requestContact.shouldBeNull()
            row3el2.requestPoll.shouldBeNull()
            row3el2.requestUsers.shouldBeNull()

            row3el2.requestChat.run {
                shouldNotBeNull()
                requestId shouldBe 2
                chatIsChannel shouldBe false
                chatIsForum shouldBe true
            }
        }

        fullyBuiltKeyboard.resizeKeyboard shouldBe true
        fullyBuiltKeyboard.oneTimeKeyboard shouldBe false
        fullyBuiltKeyboard.isPersistent shouldBe true
        fullyBuiltKeyboard.inputFieldPlaceholder shouldBe "test"
        fullyBuiltKeyboard.selective shouldBe null
    }

    @Test
    fun `check unary plus button adding`() {
        val operatorButtons = replyKeyboardMarkup {
            +"test"
            br()
            +"test2"

            options {
                selective = false
            }
        }

        operatorButtons.keyboard.run {
            this shouldHaveSize 2
            first().first().text shouldBe "test"
            first().first().requestContact.shouldBeNull()
            first().first().requestPoll.shouldBeNull()
            first().first().requestLocation.shouldBeNull()
            first().first().webApp.shouldBeNull()

            shouldThrow<IndexOutOfBoundsException> { first()[1] }

            get(1).first().text shouldBe "test2"
            get(1).first().requestContact.shouldBeNull()
            get(1).first().requestPoll.shouldBeNull()
            get(1).first().requestLocation.shouldBeNull()
            get(1).first().webApp.shouldBeNull()

            shouldThrow<IndexOutOfBoundsException> { get(1)[1] }
        }

        operatorButtons.selective shouldBe false
    }

    @Test
    fun `check markup shortcut builder test`() {
        val action = message("")
        with(action) {
            parameters["reply_markup"].shouldBeNull()
        }
        // inline markup
        action.inlineKeyboardMarkup {
            "test" callback "testC"
        }
        with(action) {
            val markup = parameters["reply_markup"].shouldNotBeNull()
            markup.isSerializableTo<InlineKeyboardMarkup>().keyboard.size shouldBe 1
        }

        // reply markup
        action.replyKeyboardMarkup {
            +"test"
        }
        with(action) {
            val markup = parameters["reply_markup"].shouldNotBeNull()
            markup.isSerializableTo<ReplyKeyboardMarkup>().keyboard.size shouldBe 1
        }

        // forceReply markup
        action.forceReply()
        with(action) {
            val markup = parameters["reply_markup"].shouldNotBeNull()
            markup.isSerializableTo<ForceReply>().forceReply.shouldBeTrue()
        }
    }
}
