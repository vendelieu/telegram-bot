@file:Suppress("KotlinRedundantDiagnosticSuppress")

package eu.vendeli.ktgram.extutils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.GetFileAction
import eu.vendeli.tgbot.api.GetGameHighScoresAction
import eu.vendeli.tgbot.api.GetUserChatBoostsAction
import eu.vendeli.tgbot.api.GetUserProfilePhotosAction
import eu.vendeli.tgbot.api.SendContactAction
import eu.vendeli.tgbot.api.SendDiceAction
import eu.vendeli.tgbot.api.SendGameAction
import eu.vendeli.tgbot.api.SendInvoiceAction
import eu.vendeli.tgbot.api.SendLocationAction
import eu.vendeli.tgbot.api.SendPollAction
import eu.vendeli.tgbot.api.SendVenueAction
import eu.vendeli.tgbot.api.SetGameScoreAction
import eu.vendeli.tgbot.api.SetMessageReactionAction
import eu.vendeli.tgbot.api.SetPassportDataErrorsAction
import eu.vendeli.tgbot.api.StopMessageLiveLocationAction
import eu.vendeli.tgbot.api.StopPollAction
import eu.vendeli.tgbot.api.answer.AnswerCallbackQueryAction
import eu.vendeli.tgbot.api.answer.AnswerInlineQueryAction
import eu.vendeli.tgbot.api.answer.AnswerPreCheckoutQueryAction
import eu.vendeli.tgbot.api.answer.AnswerShippingQueryAction
import eu.vendeli.tgbot.api.answer.AnswerWebAppQueryAction
import eu.vendeli.tgbot.api.botactions.CloseAction
import eu.vendeli.tgbot.api.botactions.CreateInvoiceLinkAction
import eu.vendeli.tgbot.api.botactions.DeleteMyCommandsAction
import eu.vendeli.tgbot.api.botactions.DeleteWebhookAction
import eu.vendeli.tgbot.api.botactions.GetBusinessConnectionAction
import eu.vendeli.tgbot.api.botactions.GetMeAction
import eu.vendeli.tgbot.api.botactions.GetMyCommandsAction
import eu.vendeli.tgbot.api.botactions.GetMyDefaultAdministratorRightsAction
import eu.vendeli.tgbot.api.botactions.GetMyDescriptionAction
import eu.vendeli.tgbot.api.botactions.GetMyNameAction
import eu.vendeli.tgbot.api.botactions.GetMyShortDescriptionAction
import eu.vendeli.tgbot.api.botactions.GetStarTransactionsAction
import eu.vendeli.tgbot.api.botactions.GetUpdatesAction
import eu.vendeli.tgbot.api.botactions.GetWebhookInfoAction
import eu.vendeli.tgbot.api.botactions.LogOutAction
import eu.vendeli.tgbot.api.botactions.RefundStarPaymentAction
import eu.vendeli.tgbot.api.botactions.SetMyCommandsAction
import eu.vendeli.tgbot.api.botactions.SetMyDefaultAdministratorRightsAction
import eu.vendeli.tgbot.api.botactions.SetMyDescriptionAction
import eu.vendeli.tgbot.api.botactions.SetMyNameAction
import eu.vendeli.tgbot.api.botactions.SetMyShortDescriptionAction
import eu.vendeli.tgbot.api.botactions.SetWebhookAction
import eu.vendeli.tgbot.api.chat.ApproveChatJoinRequestAction
import eu.vendeli.tgbot.api.chat.BanChatMemberAction
import eu.vendeli.tgbot.api.chat.BanChatSenderChatAction
import eu.vendeli.tgbot.api.chat.CreateChatInviteLinkAction
import eu.vendeli.tgbot.api.chat.DeclineChatJoinRequestAction
import eu.vendeli.tgbot.api.chat.DeleteChatPhotoAction
import eu.vendeli.tgbot.api.chat.DeleteChatStickerSetAction
import eu.vendeli.tgbot.api.chat.EditChatInviteLinkAction
import eu.vendeli.tgbot.api.chat.ExportChatInviteLinkAction
import eu.vendeli.tgbot.api.chat.GetChatAction
import eu.vendeli.tgbot.api.chat.GetChatAdministratorsAction
import eu.vendeli.tgbot.api.chat.GetChatMemberAction
import eu.vendeli.tgbot.api.chat.GetChatMemberCountAction
import eu.vendeli.tgbot.api.chat.GetChatMenuButtonAction
import eu.vendeli.tgbot.api.chat.LeaveChatAction
import eu.vendeli.tgbot.api.chat.PinChatMessageAction
import eu.vendeli.tgbot.api.chat.PromoteChatMemberAction
import eu.vendeli.tgbot.api.chat.RestrictChatMemberAction
import eu.vendeli.tgbot.api.chat.RevokeChatInviteLinkAction
import eu.vendeli.tgbot.api.chat.SendChatAction
import eu.vendeli.tgbot.api.chat.SetChatAdministratorCustomTitleAction
import eu.vendeli.tgbot.api.chat.SetChatDescriptionAction
import eu.vendeli.tgbot.api.chat.SetChatMenuButtonAction
import eu.vendeli.tgbot.api.chat.SetChatPermissionsAction
import eu.vendeli.tgbot.api.chat.SetChatPhotoAction
import eu.vendeli.tgbot.api.chat.SetChatStickerSetAction
import eu.vendeli.tgbot.api.chat.SetChatTitleAction
import eu.vendeli.tgbot.api.chat.UnbanChatMemberAction
import eu.vendeli.tgbot.api.chat.UnbanChatSenderChatAction
import eu.vendeli.tgbot.api.chat.UnpinAllChatMessagesAction
import eu.vendeli.tgbot.api.chat.UnpinChatMessageAction
import eu.vendeli.tgbot.api.forum.CloseForumTopicAction
import eu.vendeli.tgbot.api.forum.CloseGeneralForumTopicAction
import eu.vendeli.tgbot.api.forum.CreateForumTopicAction
import eu.vendeli.tgbot.api.forum.DeleteForumTopicAction
import eu.vendeli.tgbot.api.forum.EditForumTopicAction
import eu.vendeli.tgbot.api.forum.EditGeneralForumTopicAction
import eu.vendeli.tgbot.api.forum.GetForumTopicIconStickersAction
import eu.vendeli.tgbot.api.forum.HideGeneralForumTopicAction
import eu.vendeli.tgbot.api.forum.ReopenForumTopicAction
import eu.vendeli.tgbot.api.forum.ReopenGeneralForumTopicAction
import eu.vendeli.tgbot.api.forum.UnhideGeneralForumTopicAction
import eu.vendeli.tgbot.api.forum.UnpinAllForumTopicMessagesAction
import eu.vendeli.tgbot.api.forum.UnpinAllGeneralForumTopicMessagesAction
import eu.vendeli.tgbot.api.media.SendAnimationAction
import eu.vendeli.tgbot.api.media.SendAudioAction
import eu.vendeli.tgbot.api.media.SendDocumentAction
import eu.vendeli.tgbot.api.media.SendMediaGroupAction
import eu.vendeli.tgbot.api.media.SendPaidMediaAction
import eu.vendeli.tgbot.api.media.SendPhotoAction
import eu.vendeli.tgbot.api.media.SendStickerAction
import eu.vendeli.tgbot.api.media.SendVideoAction
import eu.vendeli.tgbot.api.media.SendVideoNoteAction
import eu.vendeli.tgbot.api.media.SendVoiceAction
import eu.vendeli.tgbot.api.message.CopyMessageAction
import eu.vendeli.tgbot.api.message.CopyMessagesAction
import eu.vendeli.tgbot.api.message.DeleteMessageAction
import eu.vendeli.tgbot.api.message.DeleteMessagesAction
import eu.vendeli.tgbot.api.message.EditMessageCaptionAction
import eu.vendeli.tgbot.api.message.EditMessageLiveLocationAction
import eu.vendeli.tgbot.api.message.EditMessageMediaAction
import eu.vendeli.tgbot.api.message.EditMessageReplyMarkupAction
import eu.vendeli.tgbot.api.message.EditMessageTextAction
import eu.vendeli.tgbot.api.message.ForwardMessageAction
import eu.vendeli.tgbot.api.message.ForwardMessagesAction
import eu.vendeli.tgbot.api.message.SendMessageAction
import eu.vendeli.tgbot.api.stickerset.AddStickerToSetAction
import eu.vendeli.tgbot.api.stickerset.CreateNewStickerSetAction
import eu.vendeli.tgbot.api.stickerset.DeleteStickerFromSetAction
import eu.vendeli.tgbot.api.stickerset.DeleteStickerSetAction
import eu.vendeli.tgbot.api.stickerset.GetCustomEmojiStickersAction
import eu.vendeli.tgbot.api.stickerset.GetStickerSetAction
import eu.vendeli.tgbot.api.stickerset.ReplaceStickerInSetAction
import eu.vendeli.tgbot.api.stickerset.SetCustomEmojiStickerSetThumbnailAction
import eu.vendeli.tgbot.api.stickerset.SetStickerEmojiListAction
import eu.vendeli.tgbot.api.stickerset.SetStickerKeywordsAction
import eu.vendeli.tgbot.api.stickerset.SetStickerMaskPositionAction
import eu.vendeli.tgbot.api.stickerset.SetStickerPositionInSetAction
import eu.vendeli.tgbot.api.stickerset.SetStickerSetThumbnailAction
import eu.vendeli.tgbot.api.stickerset.SetStickerSetTitleAction
import eu.vendeli.tgbot.api.stickerset.UploadStickerFileAction
import eu.vendeli.tgbot.types.ReactionType
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.`inline`.InlineQueryResult
import eu.vendeli.tgbot.types.`internal`.Currency
import eu.vendeli.tgbot.types.`internal`.Identifier
import eu.vendeli.tgbot.types.`internal`.ImplicitFile
import eu.vendeli.tgbot.types.`internal`.InputFile
import eu.vendeli.tgbot.types.bot.BotCommand
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatAction
import eu.vendeli.tgbot.types.chat.ChatAdministratorRights
import eu.vendeli.tgbot.types.chat.ChatPermissions
import eu.vendeli.tgbot.types.forum.IconColor
import eu.vendeli.tgbot.types.keyboard.MenuButton
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.types.media.InputPaidMedia
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.types.media.MaskPosition
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.types.passport.PassportElementError
import eu.vendeli.tgbot.types.payment.LabeledPrice
import eu.vendeli.tgbot.types.payment.ShippingOption
import eu.vendeli.tgbot.types.poll.InputPollOption
import eu.vendeli.tgbot.utils.builders.BotCommandsBuilder
import eu.vendeli.tgbot.utils.builders.EntitiesCtxBuilder
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlinx.datetime.Instant

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.contact(firstName: String, phoneNumber: String): SendContactAction =
    eu.vendeli.tgbot.api.contact(firstName, phoneNumber)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendContact(firstName: String, phoneNumber: String): SendContactAction
    = eu.vendeli.tgbot.api.sendContact(firstName, phoneNumber)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendDice(emoji: String?): SendDiceAction =
    eu.vendeli.tgbot.api.sendDice(emoji)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.dice(emoji: String?): SendDiceAction =
    eu.vendeli.tgbot.api.dice(emoji)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendGame(gameShortName: String): SendGameAction =
    eu.vendeli.tgbot.api.sendGame(gameShortName)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.game(gameShortName: String): SendGameAction =
    eu.vendeli.tgbot.api.game(gameShortName)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getFile(fileId: String): GetFileAction =
    eu.vendeli.tgbot.api.getFile(fileId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getGameHighScores(userId: Long, messageId: Long):
    GetGameHighScoresAction = eu.vendeli.tgbot.api.getGameHighScores(userId, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getGameHighScores(userId: Long): GetGameHighScoresAction =
    eu.vendeli.tgbot.api.getGameHighScores(userId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getGameHighScores(user: User, messageId: Long):
    GetGameHighScoresAction = eu.vendeli.tgbot.api.getGameHighScores(user, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getGameHighScores(user: User): GetGameHighScoresAction =
    eu.vendeli.tgbot.api.getGameHighScores(user)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getUserChatBoosts(userId: Long): GetUserChatBoostsAction =
    eu.vendeli.tgbot.api.getUserChatBoosts(userId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getUserChatBoosts(user: User): GetUserChatBoostsAction =
    eu.vendeli.tgbot.api.getUserChatBoosts(user)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getUserProfilePhotos(
  userId: Long,
  offset: Int?,
  limit: Int?,
): GetUserProfilePhotosAction = eu.vendeli.tgbot.api.getUserProfilePhotos(userId, offset, limit)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getUserProfilePhotos(
  user: User,
  offset: Int?,
  limit: Int?,
): GetUserProfilePhotosAction = eu.vendeli.tgbot.api.getUserProfilePhotos(user, offset, limit)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.invoice(
  title: String,
  description: String,
  payload: String,
  providerToken: String?,
  currency: Currency,
  prices: List<LabeledPrice>,
): SendInvoiceAction = eu.vendeli.tgbot.api.invoice(title, description, payload, providerToken,
    currency, prices)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.invoice(
  title: String,
  description: String,
  providerToken: String?,
  currency: Currency,
  vararg prices: LabeledPrice,
  noinline payload: () -> String,
): SendInvoiceAction = eu.vendeli.tgbot.api.invoice(title, description, providerToken, currency,
    prices = prices, payload)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendInvoice(
  title: String,
  description: String,
  payload: String,
  providerToken: String?,
  currency: Currency,
  prices: List<LabeledPrice>,
): SendInvoiceAction = eu.vendeli.tgbot.api.sendInvoice(title, description, payload, providerToken,
    currency, prices)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendLocation(latitude: Float, longitude: Float): SendLocationAction =
    eu.vendeli.tgbot.api.sendLocation(latitude, longitude)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.location(latitude: Float, longitude: Float): SendLocationAction =
    eu.vendeli.tgbot.api.location(latitude, longitude)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.poll(question: String, options: List<InputPollOption>): SendPollAction
    = eu.vendeli.tgbot.api.poll(question, options)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.poll(question: String, vararg options: InputPollOption):
    SendPollAction = eu.vendeli.tgbot.api.poll(question, options = options)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setGameScore(
  userId: Long,
  messageId: Long,
  score: Long,
): SetGameScoreAction = eu.vendeli.tgbot.api.setGameScore(userId, messageId, score)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setGameScore(userId: Long, score: Long): SetGameScoreAction =
    eu.vendeli.tgbot.api.setGameScore(userId, score)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setGameScore(user: User, score: Long): SetGameScoreAction =
    eu.vendeli.tgbot.api.setGameScore(user, score)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setGameScore(
  user: User,
  messageId: Long,
  score: Long,
): SetGameScoreAction = eu.vendeli.tgbot.api.setGameScore(user, messageId, score)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setMessageReaction(
  messageId: Long,
  reaction: List<ReactionType>?,
  isBig: Boolean?,
): SetMessageReactionAction = eu.vendeli.tgbot.api.setMessageReaction(messageId, reaction, isBig)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setMessageReaction(
  messageId: Long,
  vararg reaction: ReactionType,
  isBig: Boolean?,
): SetMessageReactionAction = eu.vendeli.tgbot.api.setMessageReaction(messageId, reaction =
    reaction, isBig)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setMessageReaction(
  messageId: Long,
  isBig: Boolean?,
  noinline reaction: ListingBuilder<ReactionType>.() -> Unit,
): SetMessageReactionAction = eu.vendeli.tgbot.api.setMessageReaction(messageId, isBig, reaction)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setPassportDataErrors(userId: Long,
    errors: List<PassportElementError>): SetPassportDataErrorsAction =
    eu.vendeli.tgbot.api.setPassportDataErrors(userId, errors)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setPassportDataError(userId: Long, vararg
    error: PassportElementError): SetPassportDataErrorsAction =
    eu.vendeli.tgbot.api.setPassportDataError(userId, error = error)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.stopMessageLiveLocation(messageId: Long):
    StopMessageLiveLocationAction = eu.vendeli.tgbot.api.stopMessageLiveLocation(messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.stopMessageLiveLocation(): StopMessageLiveLocationAction =
    eu.vendeli.tgbot.api.stopMessageLiveLocation()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.stopPoll(messageId: Long): StopPollAction =
    eu.vendeli.tgbot.api.stopPoll(messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.venue(
  latitude: Float,
  longitude: Float,
  title: String,
  address: String,
): SendVenueAction = eu.vendeli.tgbot.api.venue(latitude, longitude, title, address)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendVenue(
  latitude: Float,
  longitude: Float,
  title: String,
  address: String,
): SendVenueAction = eu.vendeli.tgbot.api.sendVenue(latitude, longitude, title, address)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.answerCallbackQuery(callbackQueryId: String):
    AnswerCallbackQueryAction = eu.vendeli.tgbot.api.answer.answerCallbackQuery(callbackQueryId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.answerInlineQuery(inlineQueryId: String,
    results: List<InlineQueryResult>): AnswerInlineQueryAction =
    eu.vendeli.tgbot.api.answer.answerInlineQuery(inlineQueryId, results)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.answerInlineQuery(inlineQueryId: String, noinline
    results: ListingBuilder<InlineQueryResult>.() -> Unit): AnswerInlineQueryAction =
    eu.vendeli.tgbot.api.answer.answerInlineQuery(inlineQueryId, results)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.answerInlineQuery(inlineQueryId: String, vararg
    result: InlineQueryResult): AnswerInlineQueryAction =
    eu.vendeli.tgbot.api.answer.answerInlineQuery(inlineQueryId, result = result)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.answerPreCheckoutQuery(
  preCheckoutQueryId: String,
  ok: Boolean,
  errorMessage: String?,
): AnswerPreCheckoutQueryAction =
    eu.vendeli.tgbot.api.answer.answerPreCheckoutQuery(preCheckoutQueryId, ok, errorMessage)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.answerShippingQuery(
  shippingQueryId: String,
  ok: Boolean,
  shippingOptions: List<ShippingOption>?,
  errorMessage: String?,
): AnswerShippingQueryAction = eu.vendeli.tgbot.api.answer.answerShippingQuery(shippingQueryId, ok,
    shippingOptions, errorMessage)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.answerShippingQuery(
  shippingQueryId: String,
  ok: Boolean,
  errorMessage: String?,
  noinline shippingOptions: ListingBuilder<ShippingOption>.() -> Unit,
): AnswerShippingQueryAction = eu.vendeli.tgbot.api.answer.answerShippingQuery(shippingQueryId, ok,
    errorMessage, shippingOptions)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.answerShippingQuery(
  shippingQueryId: String,
  ok: Boolean,
  errorMessage: String?,
  vararg shippingOption: ShippingOption,
): AnswerShippingQueryAction = eu.vendeli.tgbot.api.answer.answerShippingQuery(shippingQueryId, ok,
    errorMessage, shippingOption = shippingOption)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.answerWebAppQuery(webAppQueryId: String, result: InlineQueryResult):
    AnswerWebAppQueryAction = eu.vendeli.tgbot.api.answer.answerWebAppQuery(webAppQueryId, result)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.close(): CloseAction = eu.vendeli.tgbot.api.botactions.close()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.createInvoiceLink(
  title: String,
  description: String,
  payload: String,
  providerToken: String,
  currency: Currency,
  prices: List<LabeledPrice>,
): CreateInvoiceLinkAction = eu.vendeli.tgbot.api.botactions.createInvoiceLink(title, description,
    payload, providerToken, currency, prices)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.createInvoiceLink(
  title: String,
  description: String,
  providerToken: String,
  currency: Currency,
  vararg prices: LabeledPrice,
  noinline payload: () -> String,
): CreateInvoiceLinkAction = eu.vendeli.tgbot.api.botactions.createInvoiceLink(title, description,
    providerToken, currency, prices = prices, payload)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.deleteMyCommands(languageCode: String?, scope: BotCommandScope?):
    DeleteMyCommandsAction = eu.vendeli.tgbot.api.botactions.deleteMyCommands(languageCode, scope)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.deleteWebhook(dropPendingUpdates: Boolean): DeleteWebhookAction =
    eu.vendeli.tgbot.api.botactions.deleteWebhook(dropPendingUpdates)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getBusinessConnection(businessConnectionId: String):
    GetBusinessConnectionAction =
    eu.vendeli.tgbot.api.botactions.getBusinessConnection(businessConnectionId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getMe(): GetMeAction = eu.vendeli.tgbot.api.botactions.getMe()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getMyCommands(languageCode: String?, scope: BotCommandScope?):
    GetMyCommandsAction = eu.vendeli.tgbot.api.botactions.getMyCommands(languageCode, scope)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getMyDefaultAdministratorRights(forChannel: Boolean?):
    GetMyDefaultAdministratorRightsAction =
    eu.vendeli.tgbot.api.botactions.getMyDefaultAdministratorRights(forChannel)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getMyDescription(languageCode: String?): GetMyDescriptionAction =
    eu.vendeli.tgbot.api.botactions.getMyDescription(languageCode)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getMyName(languageCode: String?): GetMyNameAction =
    eu.vendeli.tgbot.api.botactions.getMyName(languageCode)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getMyShortDescription(languageCode: String?):
    GetMyShortDescriptionAction =
    eu.vendeli.tgbot.api.botactions.getMyShortDescription(languageCode)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getStarTransactions(offset: Int?, limit: Int?):
    GetStarTransactionsAction = eu.vendeli.tgbot.api.botactions.getStarTransactions(offset, limit)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getUpdates(): GetUpdatesAction =
    eu.vendeli.tgbot.api.botactions.getUpdates()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getWebhookInfo(): GetWebhookInfoAction =
    eu.vendeli.tgbot.api.botactions.getWebhookInfo()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.logOut(): LogOutAction = eu.vendeli.tgbot.api.botactions.logOut()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.refundStarPayment(telegramPaymentChargeId: String, userId: Long):
    RefundStarPaymentAction =
    eu.vendeli.tgbot.api.botactions.refundStarPayment(telegramPaymentChargeId, userId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setMyCommands(
  languageCode: String?,
  scope: BotCommandScope?,
  command: List<BotCommand>,
): SetMyCommandsAction = eu.vendeli.tgbot.api.botactions.setMyCommands(languageCode, scope, command)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setMyCommands(
  languageCode: String?,
  scope: BotCommandScope?,
  vararg command: BotCommand,
): SetMyCommandsAction = eu.vendeli.tgbot.api.botactions.setMyCommands(languageCode, scope, command
    = command)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setMyCommands(
  languageCode: String?,
  scope: BotCommandScope?,
  noinline block: BotCommandsBuilder.() -> Unit,
): SetMyCommandsAction = eu.vendeli.tgbot.api.botactions.setMyCommands(languageCode, scope, block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setMyDefaultAdministratorRights(rights: ChatAdministratorRights?,
    forChannel: Boolean?): SetMyDefaultAdministratorRightsAction =
    eu.vendeli.tgbot.api.botactions.setMyDefaultAdministratorRights(rights, forChannel)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setMyDescription(description: String?, languageCode: String?):
    SetMyDescriptionAction = eu.vendeli.tgbot.api.botactions.setMyDescription(description,
    languageCode)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setMyName(name: String?, languageCode: String?): SetMyNameAction =
    eu.vendeli.tgbot.api.botactions.setMyName(name, languageCode)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setMyShortDescription(description: String?, languageCode: String?):
    SetMyShortDescriptionAction = eu.vendeli.tgbot.api.botactions.setMyShortDescription(description,
    languageCode)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setWebhook(url: String): SetWebhookAction =
    eu.vendeli.tgbot.api.botactions.setWebhook(url)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.approveChatJoinRequest(userId: Long): ApproveChatJoinRequestAction =
    eu.vendeli.tgbot.api.chat.approveChatJoinRequest(userId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.approveChatJoinRequest(user: User): ApproveChatJoinRequestAction =
    eu.vendeli.tgbot.api.chat.approveChatJoinRequest(user)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.banChatMember(
  userId: Long,
  untilDate: Instant?,
  revokeMessages: Boolean?,
): BanChatMemberAction = eu.vendeli.tgbot.api.chat.banChatMember(userId, untilDate, revokeMessages)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.banChatMember(
  user: User,
  untilDate: Instant?,
  revokeMessages: Boolean?,
): BanChatMemberAction = eu.vendeli.tgbot.api.chat.banChatMember(user, untilDate, revokeMessages)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.banChatSenderChat(senderChatId: Long): BanChatSenderChatAction =
    eu.vendeli.tgbot.api.chat.banChatSenderChat(senderChatId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.banChatSenderChat(senderChatId: Chat): BanChatSenderChatAction =
    eu.vendeli.tgbot.api.chat.banChatSenderChat(senderChatId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.banChatSenderChat(senderChatId: User): BanChatSenderChatAction =
    eu.vendeli.tgbot.api.chat.banChatSenderChat(senderChatId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.chatAction(action: ChatAction, messageThreadId: Int?): SendChatAction
    = eu.vendeli.tgbot.api.chat.chatAction(action, messageThreadId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.chatAction(messageThreadId: Int?, noinline block: () -> ChatAction):
    SendChatAction = eu.vendeli.tgbot.api.chat.chatAction(messageThreadId, block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendChatAction(messageThreadId: Int?, noinline
    block: () -> ChatAction): SendChatAction =
    eu.vendeli.tgbot.api.chat.sendChatAction(messageThreadId, block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendChatAction(action: ChatAction, messageThreadId: Int?):
    SendChatAction = eu.vendeli.tgbot.api.chat.sendChatAction(action, messageThreadId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.createChatInviteLink(): CreateChatInviteLinkAction =
    eu.vendeli.tgbot.api.chat.createChatInviteLink()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.declineChatJoinRequest(userId: Long): DeclineChatJoinRequestAction =
    eu.vendeli.tgbot.api.chat.declineChatJoinRequest(userId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.declineChatJoinRequest(user: User): DeclineChatJoinRequestAction =
    eu.vendeli.tgbot.api.chat.declineChatJoinRequest(user)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.deleteChatPhoto(): DeleteChatPhotoAction =
    eu.vendeli.tgbot.api.chat.deleteChatPhoto()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.deleteChatStickerSet(): DeleteChatStickerSetAction =
    eu.vendeli.tgbot.api.chat.deleteChatStickerSet()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editChatInviteLink(inviteLink: String): EditChatInviteLinkAction =
    eu.vendeli.tgbot.api.chat.editChatInviteLink(inviteLink)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.exportChatInviteLink(): ExportChatInviteLinkAction =
    eu.vendeli.tgbot.api.chat.exportChatInviteLink()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getChat(): GetChatAction = eu.vendeli.tgbot.api.chat.getChat()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getChatAdministrators(): GetChatAdministratorsAction =
    eu.vendeli.tgbot.api.chat.getChatAdministrators()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getChatMember(userId: Long): GetChatMemberAction =
    eu.vendeli.tgbot.api.chat.getChatMember(userId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getChatMember(user: User): GetChatMemberAction =
    eu.vendeli.tgbot.api.chat.getChatMember(user)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getChatMemberCount(): GetChatMemberCountAction =
    eu.vendeli.tgbot.api.chat.getChatMemberCount()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getChatMenuButton(): GetChatMenuButtonAction =
    eu.vendeli.tgbot.api.chat.getChatMenuButton()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.leaveChat(): LeaveChatAction = eu.vendeli.tgbot.api.chat.leaveChat()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.pinChatMessage(messageId: Long, disableNotification: Boolean?):
    PinChatMessageAction = eu.vendeli.tgbot.api.chat.pinChatMessage(messageId, disableNotification)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.promoteChatMember(userId: Long): PromoteChatMemberAction =
    eu.vendeli.tgbot.api.chat.promoteChatMember(userId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.promoteChatMember(user: User): PromoteChatMemberAction =
    eu.vendeli.tgbot.api.chat.promoteChatMember(user)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.restrictChatMember(
  userId: Long,
  untilDate: Instant?,
  useIndependentChatPermissions: Boolean?,
  noinline chatPermissions: ChatPermissions.() -> Unit,
): RestrictChatMemberAction = eu.vendeli.tgbot.api.chat.restrictChatMember(userId, untilDate,
    useIndependentChatPermissions, chatPermissions)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.restrictChatMember(
  userId: Long,
  chatPermissions: ChatPermissions,
  untilDate: Instant?,
  useIndependentChatPermissions: Boolean?,
): RestrictChatMemberAction = eu.vendeli.tgbot.api.chat.restrictChatMember(userId, chatPermissions,
    untilDate, useIndependentChatPermissions)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.restrictChatMember(
  user: User,
  untilDate: Instant?,
  useIndependentChatPermissions: Boolean?,
  noinline chatPermissions: ChatPermissions.() -> Unit,
): RestrictChatMemberAction = eu.vendeli.tgbot.api.chat.restrictChatMember(user, untilDate,
    useIndependentChatPermissions, chatPermissions)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.restrictChatMember(
  user: User,
  chatPermissions: ChatPermissions,
  untilDate: Instant?,
  useIndependentChatPermissions: Boolean?,
): RestrictChatMemberAction = eu.vendeli.tgbot.api.chat.restrictChatMember(user, chatPermissions,
    untilDate, useIndependentChatPermissions)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.revokeChatInviteLink(inviteLink: String): RevokeChatInviteLinkAction =
    eu.vendeli.tgbot.api.chat.revokeChatInviteLink(inviteLink)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setChatAdministratorCustomTitle(userId: Long, customTitle: String):
    SetChatAdministratorCustomTitleAction =
    eu.vendeli.tgbot.api.chat.setChatAdministratorCustomTitle(userId, customTitle)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setChatAdministratorCustomTitle(user: User, customTitle: String):
    SetChatAdministratorCustomTitleAction =
    eu.vendeli.tgbot.api.chat.setChatAdministratorCustomTitle(user, customTitle)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setChatDescription(title: String?): SetChatDescriptionAction =
    eu.vendeli.tgbot.api.chat.setChatDescription(title)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setChatMenuButton(menuButton: MenuButton): SetChatMenuButtonAction =
    eu.vendeli.tgbot.api.chat.setChatMenuButton(menuButton)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setChatPermissions(permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean?): SetChatPermissionsAction =
    eu.vendeli.tgbot.api.chat.setChatPermissions(permissions, useIndependentChatPermissions)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setChatPermissions(useIndependentChatPermissions: Boolean?, noinline
    permissions: ChatPermissions.() -> Unit): SetChatPermissionsAction =
    eu.vendeli.tgbot.api.chat.setChatPermissions(useIndependentChatPermissions, permissions)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setChatPhoto(`file`: ImplicitFile): SetChatPhotoAction =
    eu.vendeli.tgbot.api.chat.setChatPhoto(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setChatPhoto(noinline block: () -> String): SetChatPhotoAction =
    eu.vendeli.tgbot.api.chat.setChatPhoto(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setChatPhoto(`file`: InputFile): SetChatPhotoAction =
    eu.vendeli.tgbot.api.chat.setChatPhoto(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setChatStickerSet(stickerSetName: String): SetChatStickerSetAction =
    eu.vendeli.tgbot.api.chat.setChatStickerSet(stickerSetName)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setChatTitle(title: String): SetChatTitleAction =
    eu.vendeli.tgbot.api.chat.setChatTitle(title)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.unbanChatMember(userId: Long, onlyIfBanned: Boolean?):
    UnbanChatMemberAction = eu.vendeli.tgbot.api.chat.unbanChatMember(userId, onlyIfBanned)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.unbanChatMember(user: User, onlyIfBanned: Boolean?):
    UnbanChatMemberAction = eu.vendeli.tgbot.api.chat.unbanChatMember(user, onlyIfBanned)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.unbanChatSenderChat(senderChatId: Long): UnbanChatSenderChatAction =
    eu.vendeli.tgbot.api.chat.unbanChatSenderChat(senderChatId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.unbanChatSenderChat(senderChatId: Chat): UnbanChatSenderChatAction =
    eu.vendeli.tgbot.api.chat.unbanChatSenderChat(senderChatId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.unbanChatSenderChat(senderChatId: User): UnbanChatSenderChatAction =
    eu.vendeli.tgbot.api.chat.unbanChatSenderChat(senderChatId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.unpinAllChatMessages(): UnpinAllChatMessagesAction =
    eu.vendeli.tgbot.api.chat.unpinAllChatMessages()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.unpinChatMessage(messageId: Long): UnpinChatMessageAction =
    eu.vendeli.tgbot.api.chat.unpinChatMessage(messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.closeForumTopic(messageThreadId: Int): CloseForumTopicAction =
    eu.vendeli.tgbot.api.forum.closeForumTopic(messageThreadId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.closeGeneralForumTopic(): CloseGeneralForumTopicAction =
    eu.vendeli.tgbot.api.forum.closeGeneralForumTopic()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.createForumTopic(
  name: String,
  iconColor: IconColor?,
  iconCustomEmojiId: String?,
): CreateForumTopicAction = eu.vendeli.tgbot.api.forum.createForumTopic(name, iconColor,
    iconCustomEmojiId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.deleteForumTopic(messageThreadId: Int): DeleteForumTopicAction =
    eu.vendeli.tgbot.api.forum.deleteForumTopic(messageThreadId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editForumTopic(
  messageThreadId: Int,
  name: String?,
  iconCustomEmojiId: String?,
): EditForumTopicAction = eu.vendeli.tgbot.api.forum.editForumTopic(messageThreadId, name,
    iconCustomEmojiId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editGeneralForumTopic(name: String): EditGeneralForumTopicAction =
    eu.vendeli.tgbot.api.forum.editGeneralForumTopic(name)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getForumTopicIconStickers(): GetForumTopicIconStickersAction =
    eu.vendeli.tgbot.api.forum.getForumTopicIconStickers()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.hideGeneralForumTopic(): HideGeneralForumTopicAction =
    eu.vendeli.tgbot.api.forum.hideGeneralForumTopic()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.reopenForumTopic(messageThreadId: Int): ReopenForumTopicAction =
    eu.vendeli.tgbot.api.forum.reopenForumTopic(messageThreadId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.reopenGeneralForumTopic(): ReopenGeneralForumTopicAction =
    eu.vendeli.tgbot.api.forum.reopenGeneralForumTopic()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.unhideGeneralForumTopic(): UnhideGeneralForumTopicAction =
    eu.vendeli.tgbot.api.forum.unhideGeneralForumTopic()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.unpinAllForumTopicMessages(messageThreadId: Int):
    UnpinAllForumTopicMessagesAction =
    eu.vendeli.tgbot.api.forum.unpinAllForumTopicMessages(messageThreadId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.unpinAllGeneralForumTopicMessages():
    UnpinAllGeneralForumTopicMessagesAction =
    eu.vendeli.tgbot.api.forum.unpinAllGeneralForumTopicMessages()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.animation(`file`: ImplicitFile): SendAnimationAction =
    eu.vendeli.tgbot.api.media.animation(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.animation(noinline block: () -> String): SendAnimationAction =
    eu.vendeli.tgbot.api.media.animation(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.animation(ba: ByteArray): SendAnimationAction =
    eu.vendeli.tgbot.api.media.animation(ba)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.animation(`file`: InputFile): SendAnimationAction =
    eu.vendeli.tgbot.api.media.animation(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendAnimation(noinline block: () -> String): SendAnimationAction =
    eu.vendeli.tgbot.api.media.sendAnimation(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendAnimation(`file`: ImplicitFile): SendAnimationAction =
    eu.vendeli.tgbot.api.media.sendAnimation(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.audio(`file`: ImplicitFile): SendAudioAction =
    eu.vendeli.tgbot.api.media.audio(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.audio(noinline block: () -> String): SendAudioAction =
    eu.vendeli.tgbot.api.media.audio(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.audio(ba: ByteArray): SendAudioAction =
    eu.vendeli.tgbot.api.media.audio(ba)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.audio(`file`: InputFile): SendAudioAction =
    eu.vendeli.tgbot.api.media.audio(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendAudio(noinline block: () -> String): SendAudioAction =
    eu.vendeli.tgbot.api.media.sendAudio(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendAudio(`file`: ImplicitFile): SendAudioAction =
    eu.vendeli.tgbot.api.media.sendAudio(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.document(`file`: ImplicitFile): SendDocumentAction =
    eu.vendeli.tgbot.api.media.document(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.document(noinline block: () -> String): SendDocumentAction =
    eu.vendeli.tgbot.api.media.document(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.document(ba: ByteArray): SendDocumentAction =
    eu.vendeli.tgbot.api.media.document(ba)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.document(`file`: InputFile): SendDocumentAction =
    eu.vendeli.tgbot.api.media.document(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendDocument(noinline block: () -> String): SendDocumentAction =
    eu.vendeli.tgbot.api.media.sendDocument(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendDocument(`file`: ImplicitFile): SendDocumentAction =
    eu.vendeli.tgbot.api.media.sendDocument(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendMediaGroup(media: List<InputMedia>): SendMediaGroupAction =
    eu.vendeli.tgbot.api.media.sendMediaGroup(media)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendMediaGroup(vararg media: InputMedia): SendMediaGroupAction =
    eu.vendeli.tgbot.api.media.sendMediaGroup(media = media)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.mediaGroup(vararg media: InputMedia.Audio): SendMediaGroupAction =
    eu.vendeli.tgbot.api.media.mediaGroup(media = media)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.mediaGroup(vararg media: InputMedia.Document): SendMediaGroupAction =
    eu.vendeli.tgbot.api.media.mediaGroup(media = media)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.mediaGroup(vararg media: InputMedia.Photo): SendMediaGroupAction =
    eu.vendeli.tgbot.api.media.mediaGroup(media = media)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.mediaGroup(vararg media: InputMedia.Video): SendMediaGroupAction =
    eu.vendeli.tgbot.api.media.mediaGroup(media = media)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.photo(`file`: ImplicitFile): SendPhotoAction =
    eu.vendeli.tgbot.api.media.photo(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.photo(noinline block: () -> String): SendPhotoAction =
    eu.vendeli.tgbot.api.media.photo(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.photo(ba: ByteArray): SendPhotoAction =
    eu.vendeli.tgbot.api.media.photo(ba)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.photo(`file`: InputFile): SendPhotoAction =
    eu.vendeli.tgbot.api.media.photo(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendPhoto(noinline block: () -> String): SendPhotoAction =
    eu.vendeli.tgbot.api.media.sendPhoto(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendPhoto(`file`: ImplicitFile): SendPhotoAction =
    eu.vendeli.tgbot.api.media.sendPhoto(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendPaidMedia(starCount: Int, media: List<InputPaidMedia>):
    SendPaidMediaAction = eu.vendeli.tgbot.api.media.sendPaidMedia(starCount, media)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendPaidMedia(starCount: Int, noinline
    media: ListingBuilder<InputPaidMedia>.() -> Unit): SendPaidMediaAction =
    eu.vendeli.tgbot.api.media.sendPaidMedia(starCount, media)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendPaidMedia(starCount: Int, vararg media: InputPaidMedia):
    SendPaidMediaAction = eu.vendeli.tgbot.api.media.sendPaidMedia(starCount, media = media)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sticker(`file`: ImplicitFile): SendStickerAction =
    eu.vendeli.tgbot.api.media.sticker(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sticker(noinline block: () -> String): SendStickerAction =
    eu.vendeli.tgbot.api.media.sticker(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sticker(ba: ByteArray): SendStickerAction =
    eu.vendeli.tgbot.api.media.sticker(ba)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sticker(`file`: InputFile): SendStickerAction =
    eu.vendeli.tgbot.api.media.sticker(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendSticker(noinline block: () -> String): SendStickerAction =
    eu.vendeli.tgbot.api.media.sendSticker(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendSticker(`file`: ImplicitFile): SendStickerAction =
    eu.vendeli.tgbot.api.media.sendSticker(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.video(`file`: ImplicitFile): SendVideoAction =
    eu.vendeli.tgbot.api.media.video(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.video(noinline block: () -> String): SendVideoAction =
    eu.vendeli.tgbot.api.media.video(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.video(ba: ByteArray): SendVideoAction =
    eu.vendeli.tgbot.api.media.video(ba)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.video(`file`: InputFile): SendVideoAction =
    eu.vendeli.tgbot.api.media.video(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendVideo(noinline block: () -> String): SendVideoAction =
    eu.vendeli.tgbot.api.media.sendVideo(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendVideo(`file`: ImplicitFile): SendVideoAction =
    eu.vendeli.tgbot.api.media.sendVideo(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.videoNote(`file`: ImplicitFile): SendVideoNoteAction =
    eu.vendeli.tgbot.api.media.videoNote(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.videoNote(noinline block: () -> String): SendVideoNoteAction =
    eu.vendeli.tgbot.api.media.videoNote(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.videoNote(ba: ByteArray): SendVideoNoteAction =
    eu.vendeli.tgbot.api.media.videoNote(ba)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.videoNote(input: InputFile): SendVideoNoteAction =
    eu.vendeli.tgbot.api.media.videoNote(input)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendVideoNote(`file`: ImplicitFile): SendVideoNoteAction =
    eu.vendeli.tgbot.api.media.sendVideoNote(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendVideoNote(noinline block: () -> String): SendVideoNoteAction =
    eu.vendeli.tgbot.api.media.sendVideoNote(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.voice(`file`: ImplicitFile): SendVoiceAction =
    eu.vendeli.tgbot.api.media.voice(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.voice(noinline block: () -> String): SendVoiceAction =
    eu.vendeli.tgbot.api.media.voice(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.voice(`file`: InputFile): SendVoiceAction =
    eu.vendeli.tgbot.api.media.voice(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.voice(ba: ByteArray): SendVoiceAction =
    eu.vendeli.tgbot.api.media.voice(ba)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendVoice(noinline block: () -> String): SendVoiceAction =
    eu.vendeli.tgbot.api.media.sendVoice(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendVoice(`file`: ImplicitFile): SendVoiceAction =
    eu.vendeli.tgbot.api.media.sendVoice(file)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.copyMessage(fromChatId: Identifier, messageId: Long):
    CopyMessageAction = eu.vendeli.tgbot.api.message.copyMessage(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.copyMessage(fromChatId: Long, messageId: Long): CopyMessageAction =
    eu.vendeli.tgbot.api.message.copyMessage(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.copyMessage(fromChatId: String, messageId: Long): CopyMessageAction =
    eu.vendeli.tgbot.api.message.copyMessage(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.copyMessage(fromChatId: User, messageId: Long): CopyMessageAction =
    eu.vendeli.tgbot.api.message.copyMessage(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.copyMessage(fromChatId: Chat, messageId: Long): CopyMessageAction =
    eu.vendeli.tgbot.api.message.copyMessage(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.copyMessages(fromChatId: Identifier, messageIds: List<Long>):
    CopyMessagesAction = eu.vendeli.tgbot.api.message.copyMessages(fromChatId, messageIds)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.copyMessages(fromChatId: Long, vararg messageId: Long):
    CopyMessagesAction = eu.vendeli.tgbot.api.message.copyMessages(fromChatId, messageId =
    messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.copyMessages(fromChatId: String, vararg messageId: Long):
    CopyMessagesAction = eu.vendeli.tgbot.api.message.copyMessages(fromChatId, messageId =
    messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.copyMessages(fromChatId: User, vararg messageId: Long):
    CopyMessagesAction = eu.vendeli.tgbot.api.message.copyMessages(fromChatId, messageId =
    messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.copyMessages(fromChatId: Chat, vararg messageId: Long):
    CopyMessagesAction = eu.vendeli.tgbot.api.message.copyMessages(fromChatId, messageId =
    messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.deleteMessage(messageId: Long): DeleteMessageAction =
    eu.vendeli.tgbot.api.message.deleteMessage(messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.deleteMessages(messageIds: List<Long>): DeleteMessagesAction =
    eu.vendeli.tgbot.api.message.deleteMessages(messageIds)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.deleteMessages(vararg messageId: Long): DeleteMessagesAction =
    eu.vendeli.tgbot.api.message.deleteMessages(messageId = messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMessageCaption(messageId: Long): EditMessageCaptionAction =
    eu.vendeli.tgbot.api.message.editMessageCaption(messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMessageCaption(): EditMessageCaptionAction =
    eu.vendeli.tgbot.api.message.editMessageCaption()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editCaption(): EditMessageCaptionAction =
    eu.vendeli.tgbot.api.message.editCaption()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editCaption(messageId: Long): EditMessageCaptionAction =
    eu.vendeli.tgbot.api.message.editCaption(messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMessageLiveLocation(
  messageId: Long,
  latitude: Float,
  longitude: Float,
): EditMessageLiveLocationAction = eu.vendeli.tgbot.api.message.editMessageLiveLocation(messageId,
    latitude, longitude)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMessageLiveLocation(latitude: Float, longitude: Float):
    EditMessageLiveLocationAction = eu.vendeli.tgbot.api.message.editMessageLiveLocation(latitude,
    longitude)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMessageMedia(messageId: Long, inputMedia: InputMedia):
    EditMessageMediaAction = eu.vendeli.tgbot.api.message.editMessageMedia(messageId, inputMedia)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMessageMedia(inputMedia: InputMedia): EditMessageMediaAction =
    eu.vendeli.tgbot.api.message.editMessageMedia(inputMedia)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMedia(messageId: Long, inputMedia: InputMedia):
    EditMessageMediaAction = eu.vendeli.tgbot.api.message.editMedia(messageId, inputMedia)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMedia(inputMedia: InputMedia): EditMessageMediaAction =
    eu.vendeli.tgbot.api.message.editMedia(inputMedia)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMessageReplyMarkup(): EditMessageReplyMarkupAction =
    eu.vendeli.tgbot.api.message.editMessageReplyMarkup()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMarkup(messageId: Long): EditMessageReplyMarkupAction =
    eu.vendeli.tgbot.api.message.editMarkup(messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMarkup(): EditMessageReplyMarkupAction =
    eu.vendeli.tgbot.api.message.editMarkup()

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMessageReplyMarkup(messageId: Long): EditMessageReplyMarkupAction
    = eu.vendeli.tgbot.api.message.editMessageReplyMarkup(messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMessageText(messageId: Long, noinline block: () -> String):
    EditMessageTextAction = eu.vendeli.tgbot.api.message.editMessageText(messageId, block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editMessageText(noinline
    block: EntitiesCtxBuilder<EditMessageTextAction>.() -> String): EditMessageTextAction =
    eu.vendeli.tgbot.api.message.editMessageText(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editText(messageId: Long, noinline block: () -> String):
    EditMessageTextAction = eu.vendeli.tgbot.api.message.editText(messageId, block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.editText(noinline
    block: EntitiesCtxBuilder<EditMessageTextAction>.() -> String): EditMessageTextAction =
    eu.vendeli.tgbot.api.message.editText(block)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.forwardMessage(fromChatId: Identifier, messageId: Long):
    ForwardMessageAction = eu.vendeli.tgbot.api.message.forwardMessage(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.forwardMessage(fromChatId: Long, messageId: Long):
    ForwardMessageAction = eu.vendeli.tgbot.api.message.forwardMessage(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.forwardMessage(fromChatId: String, messageId: Long):
    ForwardMessageAction = eu.vendeli.tgbot.api.message.forwardMessage(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.forwardMessage(fromChatId: User, messageId: Long):
    ForwardMessageAction = eu.vendeli.tgbot.api.message.forwardMessage(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.forwardMessage(fromChatId: Chat, messageId: Long):
    ForwardMessageAction = eu.vendeli.tgbot.api.message.forwardMessage(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.forwardMessages(fromChatId: Identifier, messageIds: List<Long>):
    ForwardMessagesAction = eu.vendeli.tgbot.api.message.forwardMessages(fromChatId, messageIds)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.forwardMessages(fromChatId: Long, vararg messageId: Long):
    ForwardMessagesAction = eu.vendeli.tgbot.api.message.forwardMessages(fromChatId, messageId =
    messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.forwardMessages(fromChatId: String, vararg messageId: Long):
    ForwardMessagesAction = eu.vendeli.tgbot.api.message.forwardMessages(fromChatId, messageId =
    messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.forwardMessages(fromChatId: User, vararg messageId: Long):
    ForwardMessagesAction = eu.vendeli.tgbot.api.message.forwardMessages(fromChatId, messageId =
    messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.forwardMessages(fromChatId: Chat, vararg messageId: Long):
    ForwardMessagesAction = eu.vendeli.tgbot.api.message.forwardMessages(fromChatId, messageId =
    messageId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.message(text: String): SendMessageAction =
    eu.vendeli.tgbot.api.message.message(text)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.sendMessage(text: String): SendMessageAction =
    eu.vendeli.tgbot.api.message.sendMessage(text)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.addStickerToSet(
  userId: Long,
  name: String,
  input: InputSticker,
): AddStickerToSetAction = eu.vendeli.tgbot.api.stickerset.addStickerToSet(userId, name, input)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.addStickerToSet(
  userId: Long,
  name: String,
  noinline input: () -> InputSticker,
): AddStickerToSetAction = eu.vendeli.tgbot.api.stickerset.addStickerToSet(userId, name, input)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.createNewStickerSet(
  userId: Long,
  name: String,
  title: String,
  stickers: List<InputSticker>,
): CreateNewStickerSetAction = eu.vendeli.tgbot.api.stickerset.createNewStickerSet(userId, name,
    title, stickers)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.deleteStickerFromSet(sticker: String): DeleteStickerFromSetAction =
    eu.vendeli.tgbot.api.stickerset.deleteStickerFromSet(sticker)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.deleteStickerSet(name: String): DeleteStickerSetAction =
    eu.vendeli.tgbot.api.stickerset.deleteStickerSet(name)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getCustomEmojiStickers(customEmojiIds: List<String>):
    GetCustomEmojiStickersAction =
    eu.vendeli.tgbot.api.stickerset.getCustomEmojiStickers(customEmojiIds)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getCustomEmojiStickers(vararg customEmojiId: String):
    GetCustomEmojiStickersAction =
    eu.vendeli.tgbot.api.stickerset.getCustomEmojiStickers(customEmojiId = customEmojiId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.getStickerSet(name: String): GetStickerSetAction =
    eu.vendeli.tgbot.api.stickerset.getStickerSet(name)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.replaceStickerInSet(
  userId: Long,
  name: String,
  oldSticker: String,
  sticker: InputSticker,
): ReplaceStickerInSetAction = eu.vendeli.tgbot.api.stickerset.replaceStickerInSet(userId, name,
    oldSticker, sticker)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setCustomEmojiStickerSetThumbnail(name: String,
    customEmojiId: String?): SetCustomEmojiStickerSetThumbnailAction =
    eu.vendeli.tgbot.api.stickerset.setCustomEmojiStickerSetThumbnail(name, customEmojiId)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setStickerEmojiList(sticker: String, emojiList: List<String>):
    SetStickerEmojiListAction = eu.vendeli.tgbot.api.stickerset.setStickerEmojiList(sticker,
    emojiList)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setStickerKeywords(sticker: String, keywords: List<String>?):
    SetStickerKeywordsAction = eu.vendeli.tgbot.api.stickerset.setStickerKeywords(sticker, keywords)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setStickerMaskPosition(sticker: String, maskPosition: MaskPosition?):
    SetStickerMaskPositionAction = eu.vendeli.tgbot.api.stickerset.setStickerMaskPosition(sticker,
    maskPosition)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setStickerPositionInSet(sticker: String, position: Int):
    SetStickerPositionInSetAction = eu.vendeli.tgbot.api.stickerset.setStickerPositionInSet(sticker,
    position)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setStickerSetThumbnail(
  name: String,
  userId: Long,
  format: StickerFormat,
  thumbnail: ImplicitFile?,
): SetStickerSetThumbnailAction = eu.vendeli.tgbot.api.stickerset.setStickerSetThumbnail(name,
    userId, format, thumbnail)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.setStickerSetTitle(name: String, title: String):
    SetStickerSetTitleAction = eu.vendeli.tgbot.api.stickerset.setStickerSetTitle(name, title)

@Suppress("NOTHING_TO_INLINE")
public inline fun TelegramBot.uploadStickerFile(
  userId: Long,
  sticker: InputFile,
  stickerFormat: StickerFormat,
): UploadStickerFileAction = eu.vendeli.tgbot.api.stickerset.uploadStickerFile(userId, sticker,
    stickerFormat)
