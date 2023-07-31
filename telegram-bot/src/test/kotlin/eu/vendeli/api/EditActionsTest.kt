package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.editCaption
import eu.vendeli.tgbot.api.editMarkup
import eu.vendeli.tgbot.api.editMedia
import eu.vendeli.tgbot.api.editText
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardButton
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.builders.inlineKeyboardMarkup
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EditActionsTest : BotTestContext() {
    @Test
    suspend fun `edit message test method test`() {
        val msg = message("test1").sendReturning(TG_ID, bot).getOrNull()
        msg.shouldNotBeNull()
        val request = editText(msg.messageId) { "test2" }.sendAsync(TG_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        result.text shouldBe "test2"
    }

    @Test
    suspend fun `edit markup test method test`() {
        val msg = message("test1").markup(
            inlineKeyboardMarkup { "test" switchInlineQueryCurrentChat "test" },
        ).sendReturning(TG_ID, bot).getOrNull()
        msg.shouldNotBeNull()
        val request =
            editMarkup(msg.messageId).markup(inlineKeyboardMarkup { "test2" switchInlineQueryCurrentChat "test" })
                .sendAsync(TG_ID, bot)
                .await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result) {
            text shouldBe "test1"
            replyMarkup.shouldNotBeNull().inlineKeyboard.first() shouldBe listOf(
                InlineKeyboardButton(
                    "test2",
                    switchInlineQueryCurrentChat = "test",
                ),
            )
        }
    }

    @Test
    suspend fun `edit media test method test`() {
        val msg = photo(RANDOM_PIC).sendReturning(TG_ID, bot).getOrNull()
        msg.shouldNotBeNull()
        val request = editMedia(
            msg.messageId,
            InputMedia.Photo(ImplicitFile.FromString(RANDOM_PIC_URL)),
        ).sendAsync(TG_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result) {
            text.shouldBeNull()
            photo.shouldNotBeNull()
            photo?.first()?.fileUniqueId shouldNotBe msg.photo?.first()?.fileUniqueId
        }
    }

    @Test
    suspend fun `edit media caption test method test`() {
        val msg = photo(RANDOM_PIC).sendReturning(TG_ID, bot).getOrNull()
        msg.shouldNotBeNull()
        msg.caption.shouldBeNull()

        val request = editCaption(msg.messageId).caption { "test" }.sendAsync(TG_ID, bot).await()
        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result) {
            text.shouldBeNull()
            photo.shouldNotBeNull()
            caption shouldBe "test"
        }
    }
}
