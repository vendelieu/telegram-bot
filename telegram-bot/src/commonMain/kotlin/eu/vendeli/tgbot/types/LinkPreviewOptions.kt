package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * Describes the options used for link preview generation.
 * @property isDisabled Optional. True, if the link preview is disabled
 * @property url Optional. URL to use for the link preview. If empty, then the first URL found in the message text will be used
 * @property preferSmallMedia Optional. True, if the media in the link preview is supposed to be shrunk; ignored if the URL isn't explicitly specified or media size change isn't supported for the preview
 * @property preferLargeMedia Optional. True, if the media in the link preview is supposed to be enlarged; ignored if the URL isn't explicitly specified or media size change isn't supported for the preview
 * @property showAboveText Optional. True, if the link preview must be shown above the message text; otherwise, the link preview will be shown below the message text
 * Api reference: https://core.telegram.org/bots/api#linkpreviewoptions
*/
@Serializable
data class LinkPreviewOptions(
    var isDisabled: Boolean? = null,
    var url: String? = null,
    var preferSmallMedia: Boolean? = null,
    var preferLargeMedia: Boolean? = null,
    var showAboveText: Boolean? = null,
)
