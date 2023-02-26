package eu.vendeli.tgbot.types

data class ChatPermissions(
    var canSendMessages: Boolean? = null,
    var canSendAudios: Boolean? = null,
    var canSendDocuments: Boolean? = null,
    var canSendPhotos: Boolean? = null,
    var canSendVideos: Boolean? = null,
    var canSendVideoNotes: Boolean? = null,
    var canSendVoiceNotes: Boolean? = null,
    var canSendPolls: Boolean? = null,
    var canSendOtherMessages: Boolean? = null,
    var canAddWebPagePreviews: Boolean? = null,
    var canChangeInfo: Boolean? = null,
    var canInviteUsers: Boolean? = null,
    var canPinMessages: Boolean? = null,
    var canManageTopics: Boolean? = null,
)
