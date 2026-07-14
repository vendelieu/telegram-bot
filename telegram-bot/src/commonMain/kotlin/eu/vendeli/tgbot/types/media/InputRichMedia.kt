package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.interfaces.helper.ImplicitMediaData
import kotlinx.serialization.Serializable

/**
 * This object represents the media of a rich message to be sent. It should be one of:
 * - InputMediaAnimation
 * - InputMediaAudio
 * - InputMediaPhoto
 * - InputMediaVideo
 * - InputMediaVoiceNote
 *
 * [Api reference](https://core.telegram.org/bots/api#inputrichmessagemedia)
 */
@Serializable
sealed interface InputRichMedia : ImplicitMediaData
