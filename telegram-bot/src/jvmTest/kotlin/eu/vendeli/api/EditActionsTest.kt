package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.api.message.editMessageCaption
import eu.vendeli.tgbot.api.message.editMessageMedia
import eu.vendeli.tgbot.api.message.editMessageReplyMarkup
import eu.vendeli.tgbot.api.message.editMessageText
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardButton
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.builders.inlineKeyboardMarkup
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import utils.RandomPicResource

class EditActionsTest : BotTestContext() {
    @Test
    suspend fun `edit message test method test`() {
        val msg = message("test1").sendReq().getOrNull()
        msg.shouldNotBeNull()
        val result = editMessageText(msg.messageId) { "test2" }.sendReq().shouldSuccess()

        result.text shouldBe "test2"
    }

    @Test
    suspend fun `edit markup test method test`() {
        val msg = message("test1")
            .markup(
                inlineKeyboardMarkup { "test" switchInlineQueryCurrentChat "test" },
            ).sendReq()
            .getOrNull()
        msg.shouldNotBeNull()

        val result = editMessageReplyMarkup(msg.messageId)
            .inlineKeyboardMarkup { "test2" switchInlineQuery "test" }
            .sendReq()
            .shouldSuccess()

        with(result) {
            text shouldBe "test1"
            replyMarkup.shouldNotBeNull().inlineKeyboard.first() shouldBe listOf(
                InlineKeyboardButton(
                    "test2",
                    switchInlineQuery = "test",
                ),
            )
        }
    }

    @Test
    suspend fun `edit media test method test`() {
        val msg = photo(RANDOM_PIC ?: return).sendReq().getOrNull()
        msg.shouldNotBeNull()

        val result = editMessageMedia(
            msg.messageId,
            InputMedia.Photo(RandomPicResource.RANDOM_PIC_URL),
        ).sendReq().shouldSuccess()

        with(result) {
            text.shouldBeNull()
            photo.shouldNotBeNull()
            photo?.first()?.fileUniqueId shouldNotBe msg.photo?.first()?.fileUniqueId
        }
    }

    @Test
    suspend fun `edit media caption test method test`() {
        val msg = photo(RANDOM_PIC ?: return).sendReq().getOrNull()
        msg.shouldNotBeNull()
        msg.caption.shouldBeNull()

        val result = editMessageCaption(msg.messageId).caption { "test" }.sendReq().shouldSuccess()

        with(result) {
            text.shouldBeNull()
            photo.shouldNotBeNull()
            caption shouldBe "test"
        }
    }
}
