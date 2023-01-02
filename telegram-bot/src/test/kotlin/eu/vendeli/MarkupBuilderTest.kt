package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.KeyboardButtonPollType
import eu.vendeli.tgbot.types.LoginUrl
import eu.vendeli.tgbot.types.WebAppInfo
import eu.vendeli.tgbot.utils.builders.inlineKeyboardMarkup
import eu.vendeli.tgbot.utils.builders.replyKeyboardMarkup
import io.kotest.assertions.throwables.shouldThrow
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
        callbackMarkup.inlineKeyboard.first().first().callbackData shouldBe "testCallback"

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
            "test3" webApp WebAppInfo("test")
            "test4" requestContact false
        }

        fullyBuiltKeyboard.keyboard shouldHaveSize 2 // two rows

        fullyBuiltKeyboard.keyboard.run {
            // 1 row 1 element
            first().first().text shouldBe "test1"

            first().first().requestLocation shouldBe true

            first().first().requestPoll.shouldBeNull()
            first().first().requestContact.shouldBeNull()
            first().first().webApp.shouldBeNull()

            // 1 row 2 element
            first()[1].text shouldBe "test2"

            first()[1].requestPoll.shouldNotBeNull()
            first()[1].requestPoll?.type.shouldBeNull()

            first()[1].requestLocation.shouldBeNull()
            first()[1].requestContact.shouldBeNull()
            first()[1].webApp.shouldBeNull()

            // 2 row 1 element
            get(1).first().text shouldBe "test3"

            get(1).first().webApp.shouldNotBeNull()
            get(1).first().webApp?.url shouldBe "test"

            get(1).first().requestLocation.shouldBeNull()
            get(1).first().requestContact.shouldBeNull()
            get(1).first().requestPoll.shouldBeNull()

            // 2 row 2 element
            get(1)[1].text shouldBe "test4"
            get(1)[1].webApp.shouldBeNull()
            get(1)[1].requestLocation.shouldBeNull()

            get(1)[1].requestContact.shouldNotBeNull()
            get(1)[1].requestContact shouldBe false

            get(1)[1].requestPoll.shouldBeNull()
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
            first().first().requestContact.shouldBeNull()
            first().first().requestPoll.shouldBeNull()
            first().first().requestLocation.shouldBeNull()
            first().first().webApp.shouldBeNull()

            shouldThrow<IndexOutOfBoundsException> { get(1)[1] }
        }

        operatorButtons.selective shouldBe false
    }
}
