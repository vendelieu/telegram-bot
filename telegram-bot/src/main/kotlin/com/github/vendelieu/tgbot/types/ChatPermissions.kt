package com.github.vendelieu.tgbot.types

data class ChatPermissions(
    val canSendMessages: Boolean? = null,
    val canSendMediaMessages: Boolean? = null,
    val canSendPolls: Boolean? = null,
    val canSendOtherMessages: Boolean? = null,
    val canAddWebPagePreviews: Boolean? = null,
    val canChangeInfo: Boolean? = null,
    val canInviteUsers: Boolean? = null,
    val canPinMessages: Boolean? = null
)
