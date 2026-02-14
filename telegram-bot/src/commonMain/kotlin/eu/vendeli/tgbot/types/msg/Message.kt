package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.marker.MultipleResponse
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.boost.ChatBoostAdded
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatBackground
import eu.vendeli.tgbot.types.chat.ChatOwnerChanged
import eu.vendeli.tgbot.types.chat.ChatOwnerLeft
import eu.vendeli.tgbot.types.chat.ChatShared
import eu.vendeli.tgbot.types.checklist.Checklist
import eu.vendeli.tgbot.types.checklist.ChecklistTasksAdded
import eu.vendeli.tgbot.types.checklist.ChecklistTasksDone
import eu.vendeli.tgbot.types.common.Contact
import eu.vendeli.tgbot.types.common.ExternalReplyInfo
import eu.vendeli.tgbot.types.common.LinkPreviewOptions
import eu.vendeli.tgbot.types.common.Location
import eu.vendeli.tgbot.types.common.TextQuote
import eu.vendeli.tgbot.types.common.Venue
import eu.vendeli.tgbot.types.forum.ForumTopicClosed
import eu.vendeli.tgbot.types.forum.ForumTopicCreated
import eu.vendeli.tgbot.types.forum.ForumTopicEdited
import eu.vendeli.tgbot.types.forum.ForumTopicReopened
import eu.vendeli.tgbot.types.forum.GeneralForumTopicHidden
import eu.vendeli.tgbot.types.forum.GeneralForumTopicUnhidden
import eu.vendeli.tgbot.types.game.Dice
import eu.vendeli.tgbot.types.game.Game
import eu.vendeli.tgbot.types.gift.GiftInfo
import eu.vendeli.tgbot.types.gift.UniqueGift
import eu.vendeli.tgbot.types.giveaway.Giveaway
import eu.vendeli.tgbot.types.giveaway.GiveawayCompleted
import eu.vendeli.tgbot.types.giveaway.GiveawayCreated
import eu.vendeli.tgbot.types.giveaway.GiveawayWinners
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.types.keyboard.WebAppData
import eu.vendeli.tgbot.types.media.Animation
import eu.vendeli.tgbot.types.media.Audio
import eu.vendeli.tgbot.types.media.Document
import eu.vendeli.tgbot.types.media.PaidMediaInfo
import eu.vendeli.tgbot.types.media.PhotoSize
import eu.vendeli.tgbot.types.media.Sticker
import eu.vendeli.tgbot.types.media.Story
import eu.vendeli.tgbot.types.media.Video
import eu.vendeli.tgbot.types.media.VideoChatEnded
import eu.vendeli.tgbot.types.media.VideoChatParticipantsInvited
import eu.vendeli.tgbot.types.media.VideoChatScheduled
import eu.vendeli.tgbot.types.media.VideoChatStarted
import eu.vendeli.tgbot.types.media.VideoNote
import eu.vendeli.tgbot.types.media.Voice
import eu.vendeli.tgbot.types.passport.PassportData
import eu.vendeli.tgbot.types.payment.Invoice
import eu.vendeli.tgbot.types.payment.RefundedPayment
import eu.vendeli.tgbot.types.payment.SuccessfulPayment
import eu.vendeli.tgbot.types.poll.Poll
import eu.vendeli.tgbot.types.user.UsersShared
import eu.vendeli.tgbot.types.webapp.WriteAccessAllowed
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * This object represents a message.
 *
 * [Api reference](https://core.telegram.org/bots/api#message)
 * @property messageId Unique message identifier inside this chat
 * @property messageThreadId Optional. Unique identifier of a message thread to which the message belongs; for supergroups only
 * @property from Optional. Sender of the message; empty for messages sent to channels. For backward compatibility, the field contains a fake sender user in non-channel chats, if the message was sent on behalf of a chat.
 * @property senderChat Optional. Sender of the message, sent on behalf of a chat. For example, the channel itself for channel posts, the supergroup itself for messages from anonymous group administrators, the linked channel for messages automatically forwarded to the discussion group. For backward compatibility, the field from contains a fake sender user in non-channel chats, if the message was sent on behalf of a chat.
 * @property senderBoostCount Optional. If the sender of the message boosted the chat, the number of boosts added by the user
 * @property senderBusinessBot Optional. The bot that actually sent the message on behalf of the business account. Available only for outgoing messages sent on behalf of the connected business account.
 * @property date Date the message was sent in Unix time. It is always a positive number, representing a valid date.
 * @property businessConnectionId Optional. Unique identifier of the business connection from which the message was received. If non-empty, the message belongs to a chat of the corresponding business account that is independent from any potential bot chat which might share the same identifier.
 * @property chat Chat the message belongs to
 * @property forwardOrigin Optional. Information about the original message for forwarded messages
 * @property isTopicMessage Optional. True, if the message is sent to a forum topic
 * @property isAutomaticForward Optional. True, if the message is a channel post that was automatically forwarded to the connected discussion group
 * @property replyToMessage Optional. For replies in the same chat and message thread, the original message. Note that the Message object in this field will not contain further reply_to_message fields even if it itself is a reply.
 * @property externalReply Optional. Information about the message that is being replied to, which may come from another chat or forum topic
 * @property quote Optional. For replies that quote part of the original message, the quoted part of the message
 * @property replyToStory Optional. For replies to a story, the original story
 * @property viaBot Optional. Bot through which the message was sent
 * @property editDate Optional. Date the message was last edited in Unix time
 * @property hasProtectedContent Optional. True, if the message can't be forwarded
 * @property isFromOffline Optional. True, if the message was sent by an implicit action, for example, as an away or a greeting business message, or as a scheduled message
 * @property mediaGroupId Optional. The unique identifier of a media message group this message belongs to
 * @property authorSignature Optional. Signature of the post author for messages in channels, or the custom title of an anonymous group administrator
 * @property text Optional. For text messages, the actual UTF-8 text of the message
 * @property entities Optional. For text messages, special entities like usernames, URLs, bot commands, etc. that appear in the text
 * @property linkPreviewOptions Optional. Options used for link preview generation for the message, if it is a text message and link preview options were changed
 * @property effectId Optional. Unique identifier of the message effect added to the message
 * @property animation Optional. Message is an animation, information about the animation. For backward compatibility, when this field is set, the document field will also be set
 * @property audio Optional. Message is an audio file, information about the file
 * @property document Optional. Message is a general file, information about the file
 * @property paidMedia Optional. Message contains paid media; information about the paid media
 * @property photo Optional. Message is a photo, available sizes of the photo
 * @property sticker Optional. Message is a sticker, information about the sticker
 * @property story Optional. Message is a forwarded story
 * @property video Optional. Message is a video, information about the video
 * @property videoNote Optional. Message is a video note, information about the video message
 * @property voice Optional. Message is a voice message, information about the file
 * @property caption Optional. Caption for the animation, audio, document, paid media, photo, video or voice
 * @property captionEntities Optional. For messages with a caption, special entities like usernames, URLs, bot commands, etc. that appear in the caption
 * @property showCaptionAboveMedia Optional. True, if the caption must be shown above the message media
 * @property hasMediaSpoiler Optional. True, if the message media is covered by a spoiler animation
 * @property contact Optional. Message is a shared contact, information about the contact
 * @property dice Optional. Message is a dice with random value
 * @property game Optional. Message is a game, information about the game. More about games: https://core.telegram.org/bots/api#games
 * @property poll Optional. Message is a native poll, information about the poll
 * @property venue Optional. Message is a venue, information about the venue. For backward compatibility, when this field is set, the location field will also be set
 * @property location Optional. Message is a shared location, information about the location
 * @property newChatMembers Optional. New members that were added to the group or supergroup and information about them (the bot itself may be one of these members)
 * @property leftChatMember Optional. A member was removed from the group, information about them (this member may be the bot itself)
 * @property newChatTitle Optional. A chat title was changed to this value
 * @property newChatPhoto Optional. A chat photo was change to this value
 * @property deleteChatPhoto Optional. Service message: the chat photo was deleted
 * @property groupChatCreated Optional. Service message: the group has been created
 * @property supergroupChatCreated Optional. Service message: the supergroup has been created. This field can't be received in a message coming through updates, because bot can't be a member of a supergroup when it is created. It can only be found in reply_to_message if someone replies to a very first message in a directly created supergroup.
 * @property channelChatCreated Optional. Service message: the channel has been created. This field can't be received in a message coming through updates, because bot can't be a member of a channel when it is created. It can only be found in reply_to_message if someone replies to a very first message in a channel.
 * @property messageAutoDeleteTimerChanged Optional. Service message: auto-delete timer settings changed in the chat
 * @property migrateToChatId Optional. The group has been migrated to a supergroup with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @property migrateFromChatId Optional. The supergroup has been migrated from a group with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @property pinnedMessage Optional. Specified message was pinned. Note that the Message object in this field will not contain further reply_to_message fields even if it itself is a reply.
 * @property invoice Optional. Message is an invoice for a payment, information about the invoice. More about payments: https://core.telegram.org/bots/api#payments
 * @property successfulPayment Optional. Message is a service message about a successful payment, information about the payment. More about payments: https://core.telegram.org/bots/api#payments
 * @property refundedPayment Optional. Message is a service message about a refunded payment, information about the payment. More about payments: https://core.telegram.org/bots/api#payments
 * @property usersShared Optional. Service message: users were shared with the bot
 * @property chatShared Optional. Service message: a chat was shared with the bot
 * @property connectedWebsite Optional. The domain name of the website on which the user has logged in. More about Telegram Login: https://core.telegram.org/widgets/login
 * @property writeAccessAllowed Optional. Service message: the user allowed the bot to write messages after adding it to the attachment or side menu, launching a Web App from a link, or accepting an explicit request from a Web App sent by the method requestWriteAccess
 * @property passportData Optional. Telegram Passport data
 * @property proximityAlertTriggered Optional. Service message. A user in the chat triggered another user's proximity alert while sharing Live Location.
 * @property boostAdded Optional. Service message: user boosted the chat
 * @property chatBackgroundSet Optional. Service message: chat background set
 * @property forumTopicCreated Optional. Service message: forum topic created
 * @property forumTopicEdited Optional. Service message: forum topic edited
 * @property forumTopicClosed Optional. Service message: forum topic closed
 * @property forumTopicReopened Optional. Service message: forum topic reopened
 * @property generalForumTopicHidden Optional. Service message: the 'General' forum topic hidden
 * @property generalForumTopicUnhidden Optional. Service message: the 'General' forum topic unhidden
 * @property giveawayCreated Optional. Service message: a scheduled giveaway was created
 * @property giveaway Optional. The message is a scheduled giveaway message
 * @property giveawayWinners Optional. A giveaway with public winners was completed
 * @property giveawayCompleted Optional. Service message: a giveaway without public winners was completed
 * @property videoChatScheduled Optional. Service message: video chat scheduled
 * @property videoChatStarted Optional. Service message: video chat started
 * @property videoChatEnded Optional. Service message: video chat ended
 * @property videoChatParticipantsInvited Optional. Service message: new participants invited to a video chat
 * @property webAppData Optional. Service message: data sent by a Web App
 * @property replyMarkup Optional. Inline keyboard attached to the message. login_url buttons are represented as ordinary url buttons.
 */
@Serializable
@TgAPI.Name("Message")
data class Message(
    override val messageId: Long,
    val messageThreadId: Int? = null,
    val from: User? = null,
    val senderChat: Chat? = null,
    val senderBoostCount: Int? = null,
    val senderBusinessBot: User? = null,
    @Serializable(InstantSerializer::class)
    override val date: Instant,
    val businessConnectionId: String? = null,
    override val chat: Chat,
    val forwardOrigin: MessageOrigin? = null,
    val isTopicMessage: Boolean? = null,
    val isAutomaticForward: Boolean? = null,
    val replyToMessage: Message? = null,
    val externalReply: ExternalReplyInfo? = null,
    val replyToStory: Story? = null,
    val quote: TextQuote? = null,
    val viaBot: User? = null,
    @Serializable(InstantSerializer::class)
    val editDate: Instant? = null,
    val hasProtectedContent: Boolean? = null,
    val isFromOffline: Boolean? = null,
    val mediaGroupId: String? = null,
    val paidStarCount: Int? = null,
    val authorSignature: String? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    val effectId: String? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null,
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val paidMedia: PaidMediaInfo? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val story: Story? = null,
    val video: Video? = null,
    val videoNote: VideoNote? = null,
    val voice: Voice? = null,
    val caption: String? = null,
    val captionEntities: List<MessageEntity>? = null,
    val showCaptionAboveMedia: Boolean? = null,
    val checklist: Checklist? = null,
    val contact: Contact? = null,
    val dice: Dice? = null,
    val game: Game? = null,
    val poll: Poll? = null,
    val venue: Venue? = null,
    val location: Location? = null,
    val newChatMembers: List<User>? = null,
    val leftChatMember: User? = null,
    val chatOwnerLeft: ChatOwnerLeft? = null,
    val chatOwnerChanged: ChatOwnerChanged? = null,
    val newChatTitle: String? = null,
    val newChatPhoto: List<PhotoSize>? = null,
    val deleteChatPhoto: Boolean? = null,
    val groupChatCreated: Boolean? = null,
    val supergroupChatCreated: Boolean? = null,
    val channelChatCreated: Boolean? = null,
    val messageAutoDeleteTimerChanged: MessageAutoDeleteTimerChanged? = null,
    val migrateToChatId: Long? = null,
    val migrateFromChatId: Long? = null,
    val pinnedMessage: MaybeInaccessibleMessage? = null,
    val invoice: Invoice? = null,
    val successfulPayment: SuccessfulPayment? = null,
    val refundedPayment: RefundedPayment? = null,
    val directMessagesTopic: DirectMessagesTopic? = null,
    val replyToChecklistTaskId: Long? = null,
    val isPaidPost: Boolean? = null,
    val suggestedPostInfo: SuggestedPostInfo? = null,
    val suggestedPostApproved: SuggestedPostApproved? = null,
    val suggestedPostApprovalFailed: SuggestedPostApprovalFailed? = null,
    val suggestedPostDeclined: SuggestedPostDeclined? = null,
    val suggestedPostPaid: SuggestedPostPaid? = null,
    val suggestedPostRefunded: SuggestedPostRefunded? = null,
    val usersShared: UsersShared? = null,
    val chatShared: ChatShared? = null,
    val gift: GiftInfo? = null,
    val giftUpgradeSent: GiftInfo? = null,
    val uniqueGift: UniqueGift? = null,
    val connectedWebsite: String? = null,
    val writeAccessAllowed: WriteAccessAllowed? = null,
    val passportData: PassportData? = null,
    val proximityAlertTriggered: ProximityAlertTriggered? = null,
    val boostAdded: ChatBoostAdded? = null,
    val forumTopicCreated: ForumTopicCreated? = null,
    val forumTopicEdited: ForumTopicEdited? = null,
    val forumTopicClosed: ForumTopicClosed? = null,
    val forumTopicReopened: ForumTopicReopened? = null,
    val generalForumTopicHidden: GeneralForumTopicHidden? = null,
    val generalForumTopicUnhidden: GeneralForumTopicUnhidden? = null,
    val giveawayCreated: GiveawayCreated? = null,
    val chatBackgroundSet: ChatBackground? = null,
    val checklistTasksDone: ChecklistTasksDone? = null,
    val checklistTasksAdded: ChecklistTasksAdded? = null,
    val directMessagePriceChanged: DirectMessagePriceChanged? = null,
    val giveaway: Giveaway? = null,
    val giveawayWinners: GiveawayWinners? = null,
    val giveawayCompleted: GiveawayCompleted? = null,
    val paidMessagePriceChanged: PaidMessagePriceChanged? = null,
    val videoChatScheduled: VideoChatScheduled? = null,
    val videoChatStarted: VideoChatStarted? = null,
    val videoChatEnded: VideoChatEnded? = null,
    val videoChatParticipantsInvited: VideoChatParticipantsInvited? = null,
    val webAppData: WebAppData? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val hasMediaSpoiler: Boolean? = null,
) : MaybeInaccessibleMessage(),
    MultipleResponse
