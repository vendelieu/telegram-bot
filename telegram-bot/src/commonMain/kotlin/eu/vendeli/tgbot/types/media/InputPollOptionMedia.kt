package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * This object represents the content of a poll option to be sent. It should be one of:
 * - InputMediaAnimation
 * - InputMediaLivePhoto
 * - InputMediaLocation
 * - InputMediaPhoto
 * - InputMediaSticker
 * - InputMediaVenue
 * - InputMediaVideo
 *
 * [Api reference](https://core.telegram.org/bots/api#inputpolloptionmedia)
 */
@Serializable
sealed interface InputPollOptionMedia
