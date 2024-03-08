package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.Serializable

/**
 * Describes actions that a non-administrator user is allowed to take in a chat.
 * Api reference: https://core.telegram.org/bots/api#chatpermissions
 * @property canSendMessages Optional. True, if the user is allowed to send text messages, contacts, giveaways, giveaway winners, invoices, locations and venues
 * @property canSendAudios Optional. True, if the user is allowed to send audios
 * @property canSendDocuments Optional. True, if the user is allowed to send documents
 * @property canSendPhotos Optional. True, if the user is allowed to send photos
 * @property canSendVideos Optional. True, if the user is allowed to send videos
 * @property canSendVideoNotes Optional. True, if the user is allowed to send video notes
 * @property canSendVoiceNotes Optional. True, if the user is allowed to send voice notes
 * @property canSendPolls Optional. True, if the user is allowed to send polls
 * @property canSendOtherMessages Optional. True, if the user is allowed to send animations, games, stickers and use inline bots
 * @property canAddWebPagePreviews Optional. True, if the user is allowed to add web page previews to their messages
 * @property canChangeInfo Optional. True, if the user is allowed to change the chat title, photo and other settings. Ignored in public supergroups
 * @property canInviteUsers Optional. True, if the user is allowed to invite new users to the chat
 * @property canPinMessages Optional. True, if the user is allowed to pin messages. Ignored in public supergroups
 * @property canManageTopics Optional. True, if the user is allowed to create forum topics. If omitted defaults to the value of can_pin_messages
*/
@Serializable
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
