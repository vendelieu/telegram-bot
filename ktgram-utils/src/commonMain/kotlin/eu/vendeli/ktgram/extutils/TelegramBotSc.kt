@file:Suppress("KotlinRedundantDiagnosticSuppress")

package eu.vendeli.ktgram.extutils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.answer.answerPreCheckoutQuery
import eu.vendeli.tgbot.api.botactions.deleteWebhook
import eu.vendeli.tgbot.api.botactions.getMe
import eu.vendeli.tgbot.api.botactions.getMyCommands
import eu.vendeli.tgbot.api.botactions.getMyDefaultAdministratorRights
import eu.vendeli.tgbot.api.botactions.getMyDescription
import eu.vendeli.tgbot.api.botactions.getUpdates
import eu.vendeli.tgbot.api.botactions.getWebhookInfo
import eu.vendeli.tgbot.api.botactions.setMyDefaultAdministratorRights
import eu.vendeli.tgbot.api.chat.approveChatJoinRequest
import eu.vendeli.tgbot.api.chat.banChatMember
import eu.vendeli.tgbot.api.chat.createChatInviteLink
import eu.vendeli.tgbot.api.chat.declineChatJoinRequest
import eu.vendeli.tgbot.api.chat.deleteChatPhoto
import eu.vendeli.tgbot.api.chat.deleteChatStickerSet
import eu.vendeli.tgbot.api.chat.editChatInviteLink
import eu.vendeli.tgbot.api.chat.getChat
import eu.vendeli.tgbot.api.chat.getChatAdministrators
import eu.vendeli.tgbot.api.chat.getChatMemberCount
import eu.vendeli.tgbot.api.chat.getChatMenuButton
import eu.vendeli.tgbot.api.chat.pinChatMessage
import eu.vendeli.tgbot.api.chat.promoteChatMember
import eu.vendeli.tgbot.api.chat.restrictChatMember
import eu.vendeli.tgbot.api.chat.revokeChatInviteLink
import eu.vendeli.tgbot.api.chat.setChatAdministratorCustomTitle
import eu.vendeli.tgbot.api.chat.setChatDescription
import eu.vendeli.tgbot.api.chat.setChatMenuButton
import eu.vendeli.tgbot.api.chat.setChatPermissions
import eu.vendeli.tgbot.api.chat.setChatPhoto
import eu.vendeli.tgbot.api.chat.setChatStickerSet
import eu.vendeli.tgbot.api.chat.setChatTitle
import eu.vendeli.tgbot.api.chat.unbanChatMember
import eu.vendeli.tgbot.api.chat.unbanChatSenderChat
import eu.vendeli.tgbot.api.contact
import eu.vendeli.tgbot.api.forum.closeGeneralForumTopic
import eu.vendeli.tgbot.api.forum.editGeneralForumTopic
import eu.vendeli.tgbot.api.forum.getForumTopicIconStickers
import eu.vendeli.tgbot.api.forum.hideGeneralForumTopic
import eu.vendeli.tgbot.api.forum.unhideGeneralForumTopic
import eu.vendeli.tgbot.api.forum.unpinAllForumTopicMessages
import eu.vendeli.tgbot.api.forum.unpinAllGeneralForumTopicMessages
import eu.vendeli.tgbot.api.getFile
import eu.vendeli.tgbot.api.getUserChatBoosts
import eu.vendeli.tgbot.api.getUserProfilePhotos
import eu.vendeli.tgbot.api.invoice
import eu.vendeli.tgbot.api.media.animation
import eu.vendeli.tgbot.api.media.document
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.api.media.sendMediaGroup
import eu.vendeli.tgbot.api.media.sticker
import eu.vendeli.tgbot.api.media.video
import eu.vendeli.tgbot.api.media.videoNote
import eu.vendeli.tgbot.api.message.deleteMessage
import eu.vendeli.tgbot.api.message.deleteMessages
import eu.vendeli.tgbot.api.message.editMessageCaption
import eu.vendeli.tgbot.api.message.editMessageReplyMarkup
import eu.vendeli.tgbot.api.message.editMessageText
import eu.vendeli.tgbot.api.sendDice
import eu.vendeli.tgbot.api.sendGame
import eu.vendeli.tgbot.api.sendLocation
import eu.vendeli.tgbot.api.setMessageReaction
import eu.vendeli.tgbot.api.setPassportDataErrors
import eu.vendeli.tgbot.api.stickerset.addStickerToSet
import eu.vendeli.tgbot.api.stickerset.createNewStickerSet
import eu.vendeli.tgbot.api.stickerset.deleteStickerFromSet
import eu.vendeli.tgbot.api.stickerset.deleteStickerSet
import eu.vendeli.tgbot.api.stickerset.getCustomEmojiStickers
import eu.vendeli.tgbot.api.stickerset.getStickerSet
import eu.vendeli.tgbot.api.stickerset.setCustomEmojiStickerSetThumbnail
import eu.vendeli.tgbot.api.stickerset.setStickerEmojiList
import eu.vendeli.tgbot.api.stickerset.setStickerKeywords
import eu.vendeli.tgbot.api.stickerset.setStickerMaskPosition
import eu.vendeli.tgbot.api.stickerset.setStickerPositionInSet
import eu.vendeli.tgbot.api.stickerset.setStickerSetThumbnail
import eu.vendeli.tgbot.api.stickerset.setStickerSetTitle
import eu.vendeli.tgbot.api.stickerset.uploadStickerFile
import eu.vendeli.tgbot.api.stopMessageLiveLocation
import eu.vendeli.tgbot.api.venue
import eu.vendeli.tgbot.types.ReactionType
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.chat.ChatAdministratorRights
import eu.vendeli.tgbot.types.chat.ChatPermissions
import eu.vendeli.tgbot.types.internal.Currency
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.keyboard.MenuButton
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.types.media.MaskPosition
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.types.passport.PassportElementError
import eu.vendeli.tgbot.types.payment.LabeledPrice
import kotlinx.datetime.Instant

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setStickerPositionInSetSc(sticker: String, position: Int) =
    setStickerPositionInSet(sticker, position)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getMyDefaultAdministratorRightsSc(forChannel: Boolean? = null) =
    getMyDefaultAdministratorRights(forChannel)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.unbanChatMemberSc(userId: Long, onlyIfBanned: Boolean? = null) =
    unbanChatMember(userId, onlyIfBanned)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.editMessageCaptionSc(messageId: Long) = editMessageCaption(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.revokeChatInviteLinkSc(inviteLink: String) = revokeChatInviteLink(inviteLink)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getForumTopicIconStickersSc() = getForumTopicIconStickers()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getChatMenuButtonSc() = getChatMenuButton()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.deleteMessagesSc(messageIds: List<Long>) = deleteMessages(messageIds)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setChatDescriptionSc(title: String? = null) = setChatDescription(title)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.pinChatMessageSc(messageId: Long, disableNotification: Boolean? = null) =
    pinChatMessage(messageId, disableNotification)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.stopMessageLiveLocationSc(messageId: Long) = stopMessageLiveLocation(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.deleteMessageSc(messageId: Long) = deleteMessage(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.sendGameSc(gameShortName: String) = sendGame(gameShortName)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setMessageReactionSc(
    messageId: Long,
    reaction: List<ReactionType>? = null,
    isBig: Boolean? = null,
) = setMessageReaction(messageId, reaction, isBig)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getMyDescriptionSc(languageCode: String? = null) = getMyDescription(languageCode)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.declineChatJoinRequestSc(userId: Long) = declineChatJoinRequest(userId)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.documentSc(file: ImplicitFile) = document(file)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.promoteChatMemberSc(userId: Long) = promoteChatMember(userId)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.restrictChatMemberSc(
    userId: Long,
    untilDate: Instant? = null,
    useIndependentChatPermissions: Boolean? = null,
    chatPermissions: ChatPermissions.() -> Unit,
) = restrictChatMember(
    userId,
    untilDate,
    useIndependentChatPermissions,
    chatPermissions,
)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.animationSc(file: ImplicitFile) = animation(file)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.createNewStickerSetSc(
    name: String,
    title: String,
    stickerFormat: StickerFormat,
    stickers: List<InputSticker>,
) = createNewStickerSet(
    name,
    title,
    stickerFormat,
    stickers,
)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getChatSc() = getChat()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.banChatMemberSc(userId: Long, untilDate: Instant? = null, revokeMessages: Boolean? = null) =
    banChatMember(userId, untilDate, revokeMessages)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.editMessageReplyMarkupSc() = editMessageReplyMarkup()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.editChatInviteLinkSc(inviteLink: String) = editChatInviteLink(inviteLink)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.stickerSc(file: ImplicitFile) = sticker(file)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setChatPermissionsSc(
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
) = setChatPermissions(
    permissions,
    useIndependentChatPermissions,
)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.videoSc(file: ImplicitFile) = video(file)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.approveChatJoinRequestSc(userId: Long) = approveChatJoinRequest(userId)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setChatStickerSetSc(stickerSetName: String) = setChatStickerSet(stickerSetName)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setChatMenuButtonSc(menuButton: MenuButton) = setChatMenuButton(menuButton)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getUpdatesSc() = getUpdates()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getCustomEmojiStickersSc(customEmojiIds: List<String>) = getCustomEmojiStickers(customEmojiIds)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getStickerSetSc(name: String) = getStickerSet(name)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.editMessageTextSc(messageId: Long, block: () -> String) = editMessageText(messageId,  block)
@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.deleteChatStickerSetSc() = deleteChatStickerSet()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getMyCommandsSc(languageCode: String? = null, scope: BotCommandScope? = null) =
    getMyCommands(languageCode, scope)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.videoNoteSc(file: ImplicitFile) = videoNote(file)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getChatMemberCountSc() = getChatMemberCount()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.photoSc(file: ImplicitFile) = photo(file)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setStickerKeywordsSc(sticker: String, keywords: List<String>? = null) =
    setStickerKeywords(sticker, keywords)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.deleteStickerSetSc(name: String) = deleteStickerSet(name)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.sendMediaGroupSc(media: List<InputMedia>) = sendMediaGroup(media)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.addStickerToSetSc(name: String, input: InputSticker) = addStickerToSet(name, input)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.hideGeneralForumTopicSc() = hideGeneralForumTopic()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.contactSc(firstName: String, phoneNumber: String) = contact(firstName, phoneNumber)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setCustomEmojiStickerSetThumbnailSc(name: String, customEmojiId: String? = null) =
    setCustomEmojiStickerSetThumbnail(name, customEmojiId)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.unbanChatSenderChatSc(senderChatId: Long) = unbanChatSenderChat(senderChatId)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setStickerSetThumbnailSc(name: String, thumbnail: ImplicitFile? = null) =
    setStickerSetThumbnail(name, thumbnail)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.createChatInviteLinkSc() = createChatInviteLink()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.sendLocationSc(latitude: Float, longitude: Float) = sendLocation(latitude, longitude)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.deleteChatPhotoSc() = deleteChatPhoto()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setPassportDataErrorsSc(userId: Long, errors: List<PassportElementError>) =
    setPassportDataErrors(userId, errors)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getChatAdministratorsSc() = getChatAdministrators()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setStickerSetTitleSc(name: String, title: String) = setStickerSetTitle(name, title)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setStickerEmojiListSc(sticker: String, emojiList: List<String>) =
    setStickerEmojiList(sticker, emojiList)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setChatAdministratorCustomTitleSc(userId: Long, customTitle: String) =
    setChatAdministratorCustomTitle(userId, customTitle)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getFileSc(fileId: String) = getFile(fileId)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setStickerMaskPositionSc(sticker: String, maskPosition: MaskPosition? = null) =
    setStickerMaskPosition(sticker, maskPosition)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setChatTitleSc(title: String) = setChatTitle(title)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setChatPhotoSc(file: ImplicitFile) = setChatPhoto(file)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.unpinAllForumTopicMessagesSc(messageThreadId: Int) = unpinAllForumTopicMessages(messageThreadId)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.editGeneralForumTopicSc(name: String) = editGeneralForumTopic(name)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.uploadStickerFileSc(sticker: InputFile, stickerFormat: StickerFormat) =
    uploadStickerFile(sticker, stickerFormat)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.deleteStickerFromSetSc(sticker: String) = deleteStickerFromSet(sticker)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.setMyDefaultAdministratorRightsSc(
    rights: ChatAdministratorRights? = null,
    forChannel: Boolean? = null,
) = setMyDefaultAdministratorRights(rights, forChannel)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.venueSc(latitude: Float, longitude: Float, title: String, address: String) =
    venue(latitude, longitude, title, address)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.answerPreCheckoutQuerySc(
    preCheckoutQueryId: String,
    ok: Boolean = true,
    errorMessage: String? = null,
) = answerPreCheckoutQuery(preCheckoutQueryId, ok, errorMessage)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.unpinAllGeneralForumTopicMessagesSc() = unpinAllGeneralForumTopicMessages()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.invoiceSc(
    title: String,
    description: String,
    payload: String,
    providerToken: String,
    currency: Currency,
    prices: List<LabeledPrice>,
) = invoice(
    title,
    description,
    payload,
    providerToken,
    currency,
    prices,
)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getUserProfilePhotosSc(userId: Long, offset: Int? = null, limit: Int? = null) =
    getUserProfilePhotos(userId, offset, limit)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.unhideGeneralForumTopicSc() = unhideGeneralForumTopic()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.closeGeneralForumTopicSc() = closeGeneralForumTopic()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.sendDiceSc(emoji: String? = null) = sendDice(emoji)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getMeSc() = getMe()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getWebhookInfoSc() = getWebhookInfo()

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.deleteWebhookSc(dropPendingUpdates: Boolean = false) = deleteWebhook(dropPendingUpdates)

@Suppress("NOTHING_TO_INLINE")
inline fun TelegramBot.getUserChatBoostsSc(userId: Long) = getUserChatBoosts(userId)
