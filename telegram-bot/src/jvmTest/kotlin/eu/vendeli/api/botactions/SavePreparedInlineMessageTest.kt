package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.savePreparedInlineMessage
import eu.vendeli.tgbot.api.getFile
import eu.vendeli.tgbot.api.media.sendAudio
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.utils.toImplicitFile
import eu.vendeli.utils.LOREM
import io.kotest.matchers.nulls.shouldNotBeNull

class SavePreparedInlineMessageTest : BotTestContext() {
    @Test
    suspend fun `save prepared inline message test`() {
        val audio = sendAudio(LOREM.AUDIO.inputFile.toImplicitFile())
            .sendReq()
            .shouldSuccess().audio.shouldNotBeNull()
        val audioFile = getFile(audio.fileId).sendAsync(bot).shouldSuccess()
        val directUrl = bot.getFileDirectUrl(audioFile).shouldNotBeNull()

        savePreparedInlineMessage(TG_ID, InlineQueryResult.Audio(audio.fileId, directUrl, "test")).sendAsync(bot)
            .shouldFailure() shouldContainInDescription "Bad Request: at least one chat type must be allowed"
    }
}
