package eu.vendeli.api.stickerset

import BotTestContext
import eu.vendeli.tgbot.api.botactions.getMe
import eu.vendeli.tgbot.api.getFile
import eu.vendeli.tgbot.api.stickerset.addStickerToSet
import eu.vendeli.tgbot.api.stickerset.createNewStickerSet
import eu.vendeli.tgbot.api.stickerset.deleteStickerFromSet
import eu.vendeli.tgbot.api.stickerset.deleteStickerSet
import eu.vendeli.tgbot.api.stickerset.getCustomEmojiStickers
import eu.vendeli.tgbot.api.stickerset.getStickerSet
import eu.vendeli.tgbot.api.stickerset.setStickerEmojiList
import eu.vendeli.tgbot.api.stickerset.setStickerKeywords
import eu.vendeli.tgbot.api.stickerset.setStickerMaskPosition
import eu.vendeli.tgbot.api.stickerset.setStickerPositionInSet
import eu.vendeli.tgbot.api.stickerset.setStickerSetThumbnail
import eu.vendeli.tgbot.api.stickerset.setStickerSetTitle
import eu.vendeli.tgbot.api.stickerset.uploadStickerFile
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.onFailure
import eu.vendeli.tgbot.types.internal.toImplicitFile
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.types.media.MaskPoint
import eu.vendeli.tgbot.types.media.MaskPosition
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.types.media.StickerType
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class StickerBaseTest : BotTestContext() {
    @Suppress("VariableNaming", "PrivatePropertyName", "ktlint:standard:property-naming")
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
        deleteStickerSet(setName).sendAsync(bot).await()

        val result = createNewStickerSet(
            setName,
            "test_2",
            StickerFormat.Static,
            listOf(
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
            stickers.size shouldBeGreaterThanOrEqual 0
        }
    }

    @Test
    suspend fun `crud sticker in set methods test`() {
        val botName = getMe().sendAsync(bot).await().getOrNull().shouldNotBeNull().username.shouldNotBeNull()
        val setName = "Test_2_by_$botName"

        val addResult = addStickerToSet(
            setName,
            InputSticker(StickerFile.FileId(TEMP_STICKER_FILE_ID), listOf("\uD83D\uDC4D\uD83C\uDFFB")),
        ).sendAsync(TG_ID, bot).await().shouldSuccess()
        addResult.shouldBeTrue()
        val fileId = getStickerSet(setName).sendAsync(bot).await().shouldSuccess().stickers.last().fileId

        val setEmojiListResult = setStickerEmojiList(
            fileId,
            listOf("\uD83D\uDC40"),
        ).sendAsync(bot).await().shouldSuccess()
        setEmojiListResult.shouldBeTrue()

        val setStickerKeywordsResult = setStickerKeywords(
            fileId,
            listOf("test"),
        ).sendAsync(bot).await().shouldSuccess()
        setStickerKeywordsResult.shouldBeTrue()

        val setStickerPositionInSetResult = setStickerPositionInSet(
            fileId,
            2,
        ).sendAsync(bot).await().shouldSuccess()
        setStickerPositionInSetResult.shouldBeTrue()

        val setStickerSetThumbnailResult = setStickerSetThumbnail(
            setName,
            StickerFile.FileId(TEMP_STICKER_FILE_ID),
        ).sendAsync(TG_ID, bot).await().shouldSuccess()
        setStickerSetThumbnailResult.shouldBeTrue()

        val setStickerMaskPositionResult = setStickerMaskPosition(
            fileId,
            MaskPosition(MaskPoint.Mouth, -0.1F, -0.1F, 2.0F),
        ).sendAsync(bot).await()
        setStickerMaskPositionResult.onFailure {
            it.description shouldContain "STICKER_MASK_COORDS_NOT_SUPPORTED"
        }?.shouldBeTrue()

        val delResult = deleteStickerFromSet(fileId).sendAsync(bot).await().shouldSuccess()
        delResult.shouldBeTrue()
    }

    @Test
    suspend fun `set sticker set title method test`() {
        val botName = getMe().sendAsync(bot).await().getOrNull().shouldNotBeNull().username.shouldNotBeNull()
        val setName = "Test_2_by_$botName"

        val result = setStickerSetTitle(setName, "title1").sendAsync(bot).await().shouldSuccess()
        result.shouldBeTrue()

        setStickerSetTitle(setName, "test_2").send(bot)
    }

    @Test
    @Ignore
    suspend fun `custom emoji sticker set manipulation methods test`() {
        val botName = getMe().sendAsync(bot).await().getOrNull().shouldNotBeNull().username.shouldNotBeNull()
        val setName = "Test_3_by_$botName"

        val result = createNewStickerSet(
            setName,
            "test_2",
            StickerFormat.Static,
            listOf(
                InputSticker(
                    StickerFile.FileId(TEMP_STICKER_FILE_ID),
                    listOf("\uD83D\uDCAF"),
                ),
            ),
        ).options {
            stickerType = StickerType.CustomEmoji
        }.sendReturning(TG_ID, bot).shouldSuccess()
        result.shouldBeTrue()

        val setCustomEmojiStickerSetThumbnailResult = setStickerSetThumbnail(
            setName,
            StickerFile.FileId(TEMP_STICKER_FILE_ID),
        ).sendAsync(TG_ID, bot).await().shouldSuccess()
        setCustomEmojiStickerSetThumbnailResult.shouldBeTrue()

        val fileId = getStickerSet(setName).sendAsync(bot).await().shouldSuccess().stickers.last().fileId

        val getCustomEmojiStickersResult = getCustomEmojiStickers(fileId).sendAsync(bot).await().shouldSuccess()
        getCustomEmojiStickersResult.size shouldBe 1
        with(getCustomEmojiStickersResult.first()) {
            type shouldBe StickerType.CustomEmoji
            customEmojiId.shouldNotBeNull()
        }

        deleteStickerSet(setName).send(bot)
    }
}
