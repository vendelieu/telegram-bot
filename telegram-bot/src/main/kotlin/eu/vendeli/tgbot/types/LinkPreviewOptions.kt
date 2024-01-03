package eu.vendeli.tgbot.types

data class LinkPreviewOptions(
    var isDisabled: Boolean? = null,
    var url: String? = null,
    var preferSmallMedia: Boolean? = null,
    var preferLargeMedia: Boolean? = null,
    var showAboveText: Boolean? = null,
)
