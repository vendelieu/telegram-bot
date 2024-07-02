package eu.vendeli.api.stickerset

import BotTestContext
import eu.vendeli.tgbot.api.botactions.getMe
import eu.vendeli.tgbot.api.stickerset.addStickerToSet
import eu.vendeli.tgbot.api.stickerset.createNewStickerSet
import eu.vendeli.tgbot.api.stickerset.deleteStickerFromSet
import eu.vendeli.tgbot.api.stickerset.deleteStickerSet
import eu.vendeli.tgbot.api.stickerset.getCustomEmojiStickers
import eu.vendeli.tgbot.api.stickerset.getStickerSet
import eu.vendeli.tgbot.api.stickerset.replaceStickerInSet
import eu.vendeli.tgbot.api.stickerset.setStickerEmojiList
import eu.vendeli.tgbot.api.stickerset.setStickerKeywords
import eu.vendeli.tgbot.api.stickerset.setStickerMaskPosition
import eu.vendeli.tgbot.api.stickerset.setStickerPositionInSet
import eu.vendeli.tgbot.api.stickerset.setStickerSetThumbnail
import eu.vendeli.tgbot.api.stickerset.setStickerSetTitle
import eu.vendeli.tgbot.api.stickerset.uploadStickerFile
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.onFailure
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.types.media.MaskPoint
import eu.vendeli.tgbot.types.media.MaskPosition
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.types.media.StickerType
import eu.vendeli.tgbot.utils.toImplicitFile
import eu.vendeli.utils.LOREM
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
        val result = uploadStickerFile(
            LOREM.STICKER.inputFile,
            StickerFormat.Animated,
        ).sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            fileSize.shouldNotBeNull() shouldBeGreaterThan 0L
        }
    }

    @Test
    suspend fun `create new sticker set method test`() {
        val botName = getMe()
            .sendAsync(bot)
            .await()
            .getOrNull()
            .shouldNotBeNull()
            .username
            .shouldNotBeNull()
        val setName = "Test_1_by_$botName"
        deleteStickerSet(setName).sendAsync(bot).await()

        val result = createNewStickerSet(
            setName,
            "test_2",
            listOf(
                InputSticker(
                    TEMP_STICKER_FILE_ID.toImplicitFile(),
                    StickerFormat.Static,
                    listOf("\uD83D\uDCAF"),
                ),
            ),
        ).sendReturning(TG_ID, bot).shouldSuccess()

        val oldSticker = getStickerSet(setName)
            .sendAsync(bot)
            .shouldSuccess()
            .stickers
            .first()
        replaceStickerInSet(
            userId = TG_ID,
            name = setName,
            oldSticker = oldSticker.fileId,
            sticker = InputSticker(
                LOREM.STICKER.inputFile.toImplicitFile(),
                StickerFormat.Animated,
                listOf("\uD83D\uDC4D\uD83C\uDFFB", "\uD83D\uDC05"),
            ),
        ).sendAsync(TG_ID, bot).getOrNull()?.shouldBeTrue()

        result.shouldBeTrue()

        deleteStickerSet(setName).send(bot)
    }

    @Test
    suspend fun `get sticker set method test`() {
        val botName = getMe()
            .sendAsync(bot)
            .await()
            .getOrNull()
            .shouldNotBeNull()
            .username
            .shouldNotBeNull()
        val setName = "Test_2_by_$botName"

        val result = getStickerSet(setName).sendAsync(bot).shouldSuccess()

        with(result) {
            name shouldBe setName
            title shouldBe "test_2"
            stickerType shouldBe StickerType.Regular
            stickers.size shouldBeGreaterThanOrEqual 0
        }
    }

    @Test
    suspend fun `crud sticker in set methods test`() {
        val botName = getMe()
            .sendAsync(bot)
            .await()
            .getOrNull()
            .shouldNotBeNull()
            .username
            .shouldNotBeNull()
        val setName = "Test_2_by_$botName"

        val addResult = addStickerToSet(
            setName,
            InputSticker(
                TEMP_STICKER_FILE_ID.toImplicitFile(),
                StickerFormat.Static,
                listOf("\uD83D\uDC4D\uD83C\uDFFB"),
            ),
        ).sendAsync(TG_ID, bot).shouldSuccess()
        addResult.shouldBeTrue()
        val stickerList = getStickerSet(setName).sendAsync(bot).shouldSuccess().stickers
        val fileId = stickerList.last().fileId

        val setEmojiListResult = setStickerEmojiList(
            fileId,
            listOf("\uD83D\uDC40"),
        ).sendAsync(bot).shouldSuccess()
        setEmojiListResult.shouldBeTrue()

        val setStickerKeywordsResult = setStickerKeywords(
            fileId,
            listOf("test"),
        ).sendAsync(bot).shouldSuccess()
        setStickerKeywordsResult.shouldBeTrue()

        val setStickerPositionInSetResult = setStickerPositionInSet(
            fileId,
            2,
        ).sendAsync(bot).shouldSuccess()
        setStickerPositionInSetResult.shouldBeTrue()

        val setStickerSetThumbnailResult = setStickerSetThumbnail(setName, StickerFormat.Static)
            .sendAsync(TG_ID, bot)
            .shouldSuccess()
        setStickerSetThumbnailResult.shouldBeTrue()

        val setStickerMaskPositionResult = setStickerMaskPosition(
            fileId,
            MaskPosition(MaskPoint.Mouth, -0.1F, -0.1F, 2.0F),
        ).sendAsync(bot).await()
        setStickerMaskPositionResult
            .onFailure {
                it.description shouldContain "STICKER_MASK_COORDS_NOT_SUPPORTED"
            }?.shouldBeTrue()

        val delResult = deleteStickerFromSet(fileId).sendAsync(bot).shouldSuccess()
        delResult.shouldBeTrue()
    }

    @Test
    suspend fun `set sticker set title method test`() {
        val botName = getMe()
            .sendAsync(bot)
            .await()
            .getOrNull()
            .shouldNotBeNull()
            .username
            .shouldNotBeNull()
        val setName = "Test_2_by_$botName"

        val result = setStickerSetTitle(setName, "title1").sendAsync(bot).shouldSuccess()
        result.shouldBeTrue()

        setStickerSetTitle(setName, "test_2").send(bot)
    }

    @Test
    @Ignore
    suspend fun `custom emoji sticker set manipulation methods test`() {
        val botName = getMe()
            .sendAsync(bot)
            .await()
            .getOrNull()
            .shouldNotBeNull()
            .username
            .shouldNotBeNull()
        val setName = "Test_3_by_$botName"

        val result = createNewStickerSet(
            setName,
            "test_2",
            listOf(
                InputSticker(
                    TEMP_STICKER_FILE_ID.toImplicitFile(),
                    StickerFormat.Static,
                    listOf("\uD83D\uDCAF"),
                ),
            ),
        ).options {
            stickerType = StickerType.CustomEmoji
        }.sendReturning(TG_ID, bot)
            .shouldSuccess()
        result.shouldBeTrue()

        val setCustomEmojiStickerSetThumbnailResult = setStickerSetThumbnail(
            setName,
            StickerFormat.Static,
            TEMP_STICKER_FILE_ID.toImplicitFile(),
        ).sendAsync(TG_ID, bot).shouldSuccess()
        setCustomEmojiStickerSetThumbnailResult.shouldBeTrue()

        val fileId = getStickerSet(setName)
            .sendAsync(bot)
            .shouldSuccess()
            .stickers
            .last()
            .fileId

        val getCustomEmojiStickersResult = getCustomEmojiStickers(fileId).sendAsync(bot).shouldSuccess()
        getCustomEmojiStickersResult.size shouldBe 1
        with(getCustomEmojiStickersResult.first()) {
            type shouldBe StickerType.CustomEmoji
            customEmojiId.shouldNotBeNull()
        }

        deleteStickerSet(setName).send(bot)
    }
}
