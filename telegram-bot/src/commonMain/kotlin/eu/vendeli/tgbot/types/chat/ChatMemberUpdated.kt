package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * This object represents changes in the status of a chat member.
 * @property chat Chat the user belongs to
 * @property from Performer of the action, which resulted in the change
 * @property date Date the change was done in Unix time
 * @property oldChatMember Previous information about the chat member
 * @property newChatMember New information about the chat member
 * @property inviteLink Optional. Chat invite link, which was used by the user to join the chat; for joining by invite link events only.
 * @property viaChatFolderInviteLink Optional. True, if the user joined the chat via a chat folder invite link
 * Api reference: https://core.telegram.org/bots/api#chatmemberupdated
*/
@Serializable
data class ChatMemberUpdated(
    val chat: Chat,
    val from: User,
    @Serializable(InstantSerializer::class)
    val date: Instant,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
    val viaChatFolderInviteLink: Boolean? = null,
)
