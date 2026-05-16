package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * This object represents the content of a poll description or a quiz explanation to be sent. It should be one of:
 * - InputMediaAnimation
 * - InputMediaAudio
 * - InputMediaDocument
 * - InputMediaLivePhoto
 * - InputMediaLocation
 * - InputMediaPhoto
 * - InputMediaVenue
 * - InputMediaVideo
 *
 * [Api reference](https://core.telegram.org/bots/api#inputpollmedia)
 */
@Serializable
sealed interface InputPollMedia
