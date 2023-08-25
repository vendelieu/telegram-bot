package eu.vendeli.api.stickerset

import BotTestContext
import eu.vendeli.tgbot.api.botactions.getMe
import eu.vendeli.tgbot.api.getFile
import eu.vendeli.tgbot.api.stickerset.addStickerToSet
import eu.vendeli.tgbot.api.stickerset.createNewStickerSet
import eu.vendeli.tgbot.api.stickerset.deleteStickerFromSet
import eu.vendeli.tgbot.api.stickerset.deleteStickerSet
import eu.vendeli.tgbot.api.stickerset.getStickerSet
import eu.vendeli.tgbot.api.stickerset.uploadStickerFile
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.toImplicitFile
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.types.media.StickerType
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class StickerBaseTest : BotTestContext() {
    @Suppress("PrivatePropertyName")
    private val TEMP_STICKER_FILE_ID = "CAACAgIAAxkBAAEKHjlk6Gex1hZNv_s6-by8ho52CXC9oAACbwEAAhZ8aAOK9nH8d3JcRjAE"

    @Test
    suspend fun `upload sticker file method test`() {
        val stickerFile = getFile(TEMP_STICKER_FILE_ID)
            .sendAsync(bot).await().getOrNull().shouldNotBeNull()
        val file = bot.getFileContent(stickerFile).shouldNotBeNull()

        val result = uploadStickerFile(
            StickerFile.WEBP(file.toInputFile("sticker.webp", "image/webp").toImplicitFile()),
        ).sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            fileSize.shouldNotBeNull() shouldBeGreaterThan 0L
        }
    }

    @Test
    suspend fun `create new sticker set method test`() {
        val botName = getMe().sendAsync(bot).await().getOrNull().shouldNotBeNull().username.shouldNotBeNull()
        val setName = "Test_1_by_$botName"

        val result = createNewStickerSet(
            setName,
            "test_2",
            StickerFormat.Static,
            listOf(
                InputSticker(
                    StickerFile.FileId(TEMP_STICKER_FILE_ID),
                    listOf("\uD83D\uDCAF"),
                ),
                InputSticker(
                    StickerFile.FileId(TEMP_STICKER_FILE_ID),
                    listOf("\uD83D\uDCAF"),
                ),
            ),
        ).sendReturning(TG_ID, bot).shouldSuccess()

        result.shouldBeTrue()

        deleteStickerSet(setName).send(bot)
    }

    @Test
    suspend fun `get sticker set method test`() {
        val botName = getMe().sendAsync(bot).await().getOrNull().shouldNotBeNull().username.shouldNotBeNull()
        val setName = "Test_2_by_$botName"

        val result = getStickerSet(setName).sendAsync(bot).await().shouldSuccess()

        with(result) {
            name shouldBe setName
            isAnimated shouldBe false
            isVideo shouldBe false
            title shouldBe "test_2"
            stickerType shouldBe StickerType.Regular
            stickers.size shouldBe 1
        }
    }

    @Test
    suspend fun `add & del sticker in set method test`() {
        val botName = getMe().sendAsync(bot).await().getOrNull().shouldNotBeNull().username.shouldNotBeNull()
        val setName = "Test_2_by_$botName"

        val addResult = addStickerToSet(
            setName, InputSticker(StickerFile.FileId(TEMP_STICKER_FILE_ID), listOf("\uD83D\uDC4D\uD83C\uDFFB"))
        ).sendAsync(TG_ID, bot,).await().shouldSuccess()
        addResult.shouldBeTrue()
        val fileId = getStickerSet(setName).sendAsync(bot).await().shouldSuccess().stickers.last().fileId

        val delResult = deleteStickerFromSet(fileId).sendAsync(bot).await().shouldSuccess()
        delResult.shouldBeTrue()
    }
}
