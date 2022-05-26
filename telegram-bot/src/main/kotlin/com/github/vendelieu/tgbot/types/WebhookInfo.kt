package com.github.vendelieu.tgbot.types

data class WebhookInfo(
    val url: String,
    val hasCustomCertificate: Boolean,
    val pendingUpdateCount: Int,
    val ipAddress: String? = null,
    val lastErrorDate: Int? = null,
    val lastErrorMessage: String? = null,
    val lastSynchronizationErrorDate: Int? = null,
    val maxConnections: Int? = null,
    val allowedUpdates: List<String>?,
)
