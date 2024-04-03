package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.Serializable

/**
 * Describes a Web App.
 *
 * Api reference: https://core.telegram.org/bots/api#webappinfo
 * @property url An HTTPS URL of a Web App to be opened with additional data as specified in Initializing Web Apps
 */
@Serializable
data class WebAppInfo(val url: String)
