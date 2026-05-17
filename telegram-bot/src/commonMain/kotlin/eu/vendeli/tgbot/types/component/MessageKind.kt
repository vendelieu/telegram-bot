@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.types.msg.Message

private val serviceMessageKinds = setOf(
    MessageKind.NEW_CHAT_MEMBERS,
    MessageKind.LEFT_CHAT_MEMBER,
    MessageKind.CHAT_OWNER_LEFT,
    MessageKind.CHAT_OWNER_CHANGED,
    MessageKind.NEW_CHAT_TITLE,
    MessageKind.NEW_CHAT_PHOTO,
    MessageKind.DELETE_CHAT_PHOTO,
    MessageKind.GROUP_CHAT_CREATED,
    MessageKind.SUPERGROUP_CHAT_CREATED,
    MessageKind.CHANNEL_CHAT_CREATED,
    MessageKind.MESSAGE_AUTO_DELETE_TIMER_CHANGED,
    MessageKind.MIGRATE_TO_CHAT,
    MessageKind.MIGRATE_FROM_CHAT,
    MessageKind.PINNED_MESSAGE,
    MessageKind.SUCCESSFUL_PAYMENT,
    MessageKind.REFUNDED_PAYMENT,
    MessageKind.SUGGESTED_POST_INFO,
    MessageKind.SUGGESTED_POST_APPROVED,
    MessageKind.SUGGESTED_POST_APPROVAL_FAILED,
    MessageKind.SUGGESTED_POST_DECLINED,
    MessageKind.SUGGESTED_POST_PAID,
    MessageKind.SUGGESTED_POST_REFUNDED,
    MessageKind.USERS_SHARED,
    MessageKind.CHAT_SHARED,
    MessageKind.GIFT,
    MessageKind.GIFT_UPGRADE_SENT,
    MessageKind.UNIQUE_GIFT,
    MessageKind.CONNECTED_WEBSITE,
    MessageKind.WRITE_ACCESS_ALLOWED,
    MessageKind.PASSPORT_DATA,
    MessageKind.PROXIMITY_ALERT_TRIGGERED,
    MessageKind.BOOST_ADDED,
    MessageKind.FORUM_TOPIC_CREATED,
    MessageKind.FORUM_TOPIC_EDITED,
    MessageKind.FORUM_TOPIC_CLOSED,
    MessageKind.FORUM_TOPIC_REOPENED,
    MessageKind.GENERAL_FORUM_TOPIC_HIDDEN,
    MessageKind.GENERAL_FORUM_TOPIC_UNHIDDEN,
    MessageKind.GIVEAWAY_CREATED,
    MessageKind.GIVEAWAY,
    MessageKind.GIVEAWAY_WINNERS,
    MessageKind.GIVEAWAY_COMPLETED,
    MessageKind.CHAT_BACKGROUND_SET,
    MessageKind.CHECKLIST_TASKS_DONE,
    MessageKind.CHECKLIST_TASKS_ADDED,
    MessageKind.DIRECT_MESSAGE_PRICE_CHANGED,
    MessageKind.PAID_MESSAGE_PRICE_CHANGED,
    MessageKind.MANAGED_BOT_CREATED,
    MessageKind.POLL_OPTION_ADDED,
    MessageKind.POLL_OPTION_DELETED,
    MessageKind.VIDEO_CHAT_SCHEDULED,
    MessageKind.VIDEO_CHAT_STARTED,
    MessageKind.VIDEO_CHAT_ENDED,
    MessageKind.VIDEO_CHAT_PARTICIPANTS_INVITED,
)

/**
 * Kind of a [Message] — derived from its populated content or service field rather than from the
 * sender action, so a mixed media group resolves each child to its actual kind (e.g. [PHOTO] +
 * [VIDEO]). Covers every content and service-message field Telegram defines; falls back to
 * [OTHER] for messages the server surfaces with no known payload.
 */
@Suppress("EnumEntryName", "EnumNaming")
enum class MessageKind {
    // --- content ---
    TEXT,
    PHOTO,
    VIDEO,
    ANIMATION,
    AUDIO,
    VOICE,
    VIDEO_NOTE,
    DOCUMENT,
    STICKER,
    PAID_MEDIA,
    POLL,
    INVOICE,
    LOCATION,
    VENUE,
    CONTACT,
    DICE,
    GAME,
    CHECKLIST,
    STORY,

    // --- service ---
    NEW_CHAT_MEMBERS,
    LEFT_CHAT_MEMBER,
    CHAT_OWNER_LEFT,
    CHAT_OWNER_CHANGED,
    NEW_CHAT_TITLE,
    NEW_CHAT_PHOTO,
    DELETE_CHAT_PHOTO,
    GROUP_CHAT_CREATED,
    SUPERGROUP_CHAT_CREATED,
    CHANNEL_CHAT_CREATED,
    MESSAGE_AUTO_DELETE_TIMER_CHANGED,
    MIGRATE_TO_CHAT,
    MIGRATE_FROM_CHAT,
    PINNED_MESSAGE,
    SUCCESSFUL_PAYMENT,
    REFUNDED_PAYMENT,
    SUGGESTED_POST_INFO,
    SUGGESTED_POST_APPROVED,
    SUGGESTED_POST_APPROVAL_FAILED,
    SUGGESTED_POST_DECLINED,
    SUGGESTED_POST_PAID,
    SUGGESTED_POST_REFUNDED,
    USERS_SHARED,
    CHAT_SHARED,
    GIFT,
    GIFT_UPGRADE_SENT,
    UNIQUE_GIFT,
    CONNECTED_WEBSITE,
    WRITE_ACCESS_ALLOWED,
    PASSPORT_DATA,
    PROXIMITY_ALERT_TRIGGERED,
    BOOST_ADDED,
    FORUM_TOPIC_CREATED,
    FORUM_TOPIC_EDITED,
    FORUM_TOPIC_CLOSED,
    FORUM_TOPIC_REOPENED,
    GENERAL_FORUM_TOPIC_HIDDEN,
    GENERAL_FORUM_TOPIC_UNHIDDEN,
    GIVEAWAY_CREATED,
    GIVEAWAY,
    GIVEAWAY_WINNERS,
    GIVEAWAY_COMPLETED,
    CHAT_BACKGROUND_SET,
    CHECKLIST_TASKS_DONE,
    CHECKLIST_TASKS_ADDED,
    DIRECT_MESSAGE_PRICE_CHANGED,
    PAID_MESSAGE_PRICE_CHANGED,
    MANAGED_BOT_CREATED,
    POLL_OPTION_ADDED,
    POLL_OPTION_DELETED,
    VIDEO_CHAT_SCHEDULED,
    VIDEO_CHAT_STARTED,
    VIDEO_CHAT_ENDED,
    VIDEO_CHAT_PARTICIPANTS_INVITED,
    WEB_APP_DATA,

    // --- fallback ---
    OTHER,

    ;

    fun isServiceMessage(): Boolean = this in serviceMessageKinds
}

/**
 * Detect the [MessageKind] of this [Message] by inspecting its populated content and service
 * fields. Content fields take precedence over [MessageKind.TEXT]; [MessageKind.VENUE] wins over
 * [MessageKind.LOCATION] because venue messages carry both. Unknown payloads fall back to
 * [MessageKind.OTHER].
 */
@Suppress("CyclomaticComplexMethod", "LongMethod", "ComplexCondition")
fun Message.detectKind(): MessageKind = when {
    // Content (media first; venue before location because venues carry both)
    animation != null -> MessageKind.ANIMATION

    audio != null -> MessageKind.AUDIO

    document != null -> MessageKind.DOCUMENT

    paidMedia != null -> MessageKind.PAID_MEDIA

    photo != null -> MessageKind.PHOTO

    sticker != null -> MessageKind.STICKER

    video != null -> MessageKind.VIDEO

    videoNote != null -> MessageKind.VIDEO_NOTE

    voice != null -> MessageKind.VOICE

    story != null -> MessageKind.STORY

    checklist != null -> MessageKind.CHECKLIST

    contact != null -> MessageKind.CONTACT

    dice != null -> MessageKind.DICE

    game != null -> MessageKind.GAME

    poll != null -> MessageKind.POLL

    venue != null -> MessageKind.VENUE

    location != null -> MessageKind.LOCATION

    invoice != null -> MessageKind.INVOICE

    // Service
    newChatMembers != null -> MessageKind.NEW_CHAT_MEMBERS

    leftChatMember != null -> MessageKind.LEFT_CHAT_MEMBER

    chatOwnerLeft != null -> MessageKind.CHAT_OWNER_LEFT

    chatOwnerChanged != null -> MessageKind.CHAT_OWNER_CHANGED

    newChatTitle != null -> MessageKind.NEW_CHAT_TITLE

    newChatPhoto != null -> MessageKind.NEW_CHAT_PHOTO

    deleteChatPhoto == true -> MessageKind.DELETE_CHAT_PHOTO

    groupChatCreated == true -> MessageKind.GROUP_CHAT_CREATED

    supergroupChatCreated == true -> MessageKind.SUPERGROUP_CHAT_CREATED

    channelChatCreated == true -> MessageKind.CHANNEL_CHAT_CREATED

    messageAutoDeleteTimerChanged != null -> MessageKind.MESSAGE_AUTO_DELETE_TIMER_CHANGED

    migrateToChatId != null -> MessageKind.MIGRATE_TO_CHAT

    migrateFromChatId != null -> MessageKind.MIGRATE_FROM_CHAT

    pinnedMessage != null -> MessageKind.PINNED_MESSAGE

    successfulPayment != null -> MessageKind.SUCCESSFUL_PAYMENT

    refundedPayment != null -> MessageKind.REFUNDED_PAYMENT

    suggestedPostInfo != null -> MessageKind.SUGGESTED_POST_INFO

    suggestedPostApproved != null -> MessageKind.SUGGESTED_POST_APPROVED

    suggestedPostApprovalFailed != null -> MessageKind.SUGGESTED_POST_APPROVAL_FAILED

    suggestedPostDeclined != null -> MessageKind.SUGGESTED_POST_DECLINED

    suggestedPostPaid != null -> MessageKind.SUGGESTED_POST_PAID

    suggestedPostRefunded != null -> MessageKind.SUGGESTED_POST_REFUNDED

    usersShared != null -> MessageKind.USERS_SHARED

    chatShared != null -> MessageKind.CHAT_SHARED

    gift != null -> MessageKind.GIFT

    giftUpgradeSent != null -> MessageKind.GIFT_UPGRADE_SENT

    uniqueGift != null -> MessageKind.UNIQUE_GIFT

    connectedWebsite != null -> MessageKind.CONNECTED_WEBSITE

    writeAccessAllowed != null -> MessageKind.WRITE_ACCESS_ALLOWED

    passportData != null -> MessageKind.PASSPORT_DATA

    proximityAlertTriggered != null -> MessageKind.PROXIMITY_ALERT_TRIGGERED

    boostAdded != null -> MessageKind.BOOST_ADDED

    forumTopicCreated != null -> MessageKind.FORUM_TOPIC_CREATED

    forumTopicEdited != null -> MessageKind.FORUM_TOPIC_EDITED

    forumTopicClosed != null -> MessageKind.FORUM_TOPIC_CLOSED

    forumTopicReopened != null -> MessageKind.FORUM_TOPIC_REOPENED

    generalForumTopicHidden != null -> MessageKind.GENERAL_FORUM_TOPIC_HIDDEN

    generalForumTopicUnhidden != null -> MessageKind.GENERAL_FORUM_TOPIC_UNHIDDEN

    giveawayCreated != null -> MessageKind.GIVEAWAY_CREATED

    giveaway != null -> MessageKind.GIVEAWAY

    giveawayWinners != null -> MessageKind.GIVEAWAY_WINNERS

    giveawayCompleted != null -> MessageKind.GIVEAWAY_COMPLETED

    chatBackgroundSet != null -> MessageKind.CHAT_BACKGROUND_SET

    checklistTasksDone != null -> MessageKind.CHECKLIST_TASKS_DONE

    checklistTasksAdded != null -> MessageKind.CHECKLIST_TASKS_ADDED

    directMessagePriceChanged != null -> MessageKind.DIRECT_MESSAGE_PRICE_CHANGED

    paidMessagePriceChanged != null -> MessageKind.PAID_MESSAGE_PRICE_CHANGED

    managedBotCreated != null -> MessageKind.MANAGED_BOT_CREATED

    pollOptionAdded != null -> MessageKind.POLL_OPTION_ADDED

    pollOptionDeleted != null -> MessageKind.POLL_OPTION_DELETED

    videoChatScheduled != null -> MessageKind.VIDEO_CHAT_SCHEDULED

    videoChatStarted != null -> MessageKind.VIDEO_CHAT_STARTED

    videoChatEnded != null -> MessageKind.VIDEO_CHAT_ENDED

    videoChatParticipantsInvited != null -> MessageKind.VIDEO_CHAT_PARTICIPANTS_INVITED

    webAppData != null -> MessageKind.WEB_APP_DATA

    // Text comes last so a photo-with-caption is PHOTO, not TEXT.
    !text.isNullOrEmpty() -> MessageKind.TEXT

    else -> MessageKind.OTHER
}
