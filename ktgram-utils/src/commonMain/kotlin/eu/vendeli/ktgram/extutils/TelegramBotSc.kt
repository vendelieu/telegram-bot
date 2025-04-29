@file:Suppress("KotlinRedundantDiagnosticSuppress")

package eu.vendeli.ktgram.extutils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.EditMessageTextAction
import eu.vendeli.tgbot.api.message.SendMessageAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.bot.BotCommand
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatAction
import eu.vendeli.tgbot.types.chat.ChatAdministratorRights
import eu.vendeli.tgbot.types.chat.ChatPermissions
import eu.vendeli.tgbot.types.common.ReactionType
import eu.vendeli.tgbot.types.component.Currency
import eu.vendeli.tgbot.types.component.DiceEmoji
import eu.vendeli.tgbot.types.component.Identifier
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.component.InputFile
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.forum.IconColor
import eu.vendeli.tgbot.types.gift.AcceptedGiftTypes
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.types.keyboard.MenuButton
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.types.media.InputPaidMedia
import eu.vendeli.tgbot.types.media.InputProfilePhoto
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.types.media.MaskPosition
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.types.passport.PassportElementError
import eu.vendeli.tgbot.types.payment.LabeledPrice
import eu.vendeli.tgbot.types.payment.ShippingOption
import eu.vendeli.tgbot.types.poll.InputPollOption
import eu.vendeli.tgbot.types.story.InputStoryContent
import eu.vendeli.tgbot.utils.builders.BotCommandsBuilder
import eu.vendeli.tgbot.utils.builders.EntitiesCtxBuilder
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.builders.PollOptionsBuilder
import kotlinx.datetime.Instant
import kotlin.time.Duration

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getBusinessAccountGifts(businessConnectionId: String): Unit =
    eu.vendeli.tgbot.api.business
        .getBusinessAccountGifts(businessConnectionId)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setBusinessAccountUsername(
    businessConnectionId: String,
    username: String? = null,
): Unit = eu.vendeli.tgbot.api.business
    .setBusinessAccountUsername(businessConnectionId, username)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteBusinessMessages(
    businessConnectionId: String,
    messageIds: List<Long>,
): Unit = eu.vendeli.tgbot.api.business
    .deleteBusinessMessages(businessConnectionId, messageIds)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.removeBusinessAccountProfilePhoto(
    businessConnectionId: String,
    isPublic: Boolean? = null,
): Unit = eu.vendeli.tgbot.api.business
    .removeBusinessAccountProfilePhoto(businessConnectionId, isPublic)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setBusinessAccountProfilePhoto(
    businessConnectionId: String,
    photo: InputProfilePhoto,
    isPublic: Boolean? = null,
): Unit = eu.vendeli.tgbot.api.business
    .setBusinessAccountProfilePhoto(businessConnectionId, photo, isPublic)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setBusinessAccountGiftSettings(
    businessConnectionId: String,
    showGiftButton: Boolean,
    acceptedGiftTypes: AcceptedGiftTypes,
): Unit = eu.vendeli.tgbot.api.business
    .setBusinessAccountGiftSettings(
        businessConnectionId,
        showGiftButton,
        acceptedGiftTypes,
    ).send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setBusinessAccountBio(businessConnectionId: String, bio: String? = null): Unit =
    eu.vendeli.tgbot.api.business
        .setBusinessAccountBio(businessConnectionId, bio)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setBusinessAccountName(
    businessConnectionId: String,
    firstName: String,
    lastName: String? = null,
): Unit = eu.vendeli.tgbot.api.business
    .setBusinessAccountName(businessConnectionId, firstName, lastName)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.transferBusinessAccountStars(businessConnectionId: String, starCount: Int): Unit =
    eu.vendeli.tgbot.api.business
        .transferBusinessAccountStars(businessConnectionId, starCount)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.readBusinessMessage(
    businessConnectionId: String,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.business
    .readBusinessMessage(businessConnectionId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getBusinessAccountStarBalance(businessConnectionId: String): Unit =
    eu.vendeli.tgbot.api.business
        .getBusinessAccountStarBalance(businessConnectionId)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendDice(emoji: String? = null, chatId: Long): Unit =
    eu.vendeli.tgbot.api.common
        .sendDice(emoji)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.dice(emoji: String? = null, chatId: Long): Unit =
    eu.vendeli.tgbot.api.common
        .dice(emoji)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.dice(noinline block: () -> DiceEmoji, chatId: Long): Unit =
    eu.vendeli.tgbot.api.common
        .dice(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.poll(
    question: String,
    options: List<InputPollOption>,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.common
    .poll(question, options)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.poll(
    question: String,
    noinline options: PollOptionsBuilder.() -> Unit,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.common
    .poll(question, options)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.poll(
    question: String,
    vararg options: InputPollOption,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.common
    .poll(question, options = options)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendPoll(
    question: String,
    noinline options: PollOptionsBuilder.() -> Unit,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.common
    .sendPoll(question, options)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.venue(
    latitude: Float,
    longitude: Float,
    title: String,
    address: String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.common
    .venue(latitude, longitude, title, address)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendVenue(
    latitude: Float,
    longitude: Float,
    title: String,
    address: String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.common
    .sendVenue(latitude, longitude, title, address)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.stopPoll(messageId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.common
        .stopPoll(messageId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendLocation(
    latitude: Float,
    longitude: Float,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.common
    .sendLocation(latitude, longitude)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.location(
    latitude: Float,
    longitude: Float,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.common
    .location(latitude, longitude)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.contact(
    firstName: String,
    phoneNumber: String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.common
    .contact(firstName, phoneNumber)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendContact(
    firstName: String,
    phoneNumber: String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.common
    .sendContact(firstName, phoneNumber)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.answerCallbackQuery(callbackQueryId: String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.answer
        .answerCallbackQuery(callbackQueryId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.answerWebAppQuery(webAppQueryId: String, result: InlineQueryResult): Unit =
    eu.vendeli.tgbot.api.answer
        .answerWebAppQuery(webAppQueryId, result)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.answerPreCheckoutQuery(
    preCheckoutQueryId: String,
    ok: Boolean,
    errorMessage: String? = null,
): Unit = eu.vendeli.tgbot.api.answer
    .answerPreCheckoutQuery(preCheckoutQueryId, ok, errorMessage)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean,
    shippingOptions: List<ShippingOption>? = null,
    errorMessage: String? = null,
): Unit = eu.vendeli.tgbot.api.answer
    .answerShippingQuery(shippingQueryId, ok, shippingOptions, errorMessage)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean,
    errorMessage: String? = null,
    noinline shippingOptions: ListingBuilder<ShippingOption>.() -> Unit,
): Unit = eu.vendeli.tgbot.api.answer
    .answerShippingQuery(shippingQueryId, ok, errorMessage, shippingOptions)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean,
    errorMessage: String? = null,
    vararg shippingOption: ShippingOption,
): Unit =
    eu.vendeli.tgbot.api.answer
        .answerShippingQuery(shippingQueryId, ok, errorMessage, shippingOption = shippingOption)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.answerInlineQuery(inlineQueryId: String, results: List<InlineQueryResult>): Unit =
    eu.vendeli.tgbot.api.answer
        .answerInlineQuery(inlineQueryId, results)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.answerInlineQuery(
    inlineQueryId: String,
    noinline results: ListingBuilder<InlineQueryResult>.() -> Unit,
): Unit = eu.vendeli.tgbot.api.answer
    .answerInlineQuery(inlineQueryId, results)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.answerInlineQuery(inlineQueryId: String, vararg result: InlineQueryResult): Unit =
    eu.vendeli.tgbot.api.answer
        .answerInlineQuery(inlineQueryId, result = result)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.removeChatVerification(chatId: Long): Unit =
    eu.vendeli.tgbot.api.verification
        .removeChatVerification(chatId)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.removeChatVerification(chat: Chat): Unit =
    eu.vendeli.tgbot.api.verification
        .removeChatVerification(chat)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.removeUserVerification(userId: Long): Unit =
    eu.vendeli.tgbot.api.verification
        .removeUserVerification(userId)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.removeUserVerification(user: User): Unit =
    eu.vendeli.tgbot.api.verification
        .removeUserVerification(user)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.verifyChat(chatId: Long, customDescription: String? = null): Unit =
    eu.vendeli.tgbot.api.verification
        .verifyChat(chatId, customDescription)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.verifyChat(chatId: Long, noinline customDescription: () -> String?): Unit =
    eu.vendeli.tgbot.api.verification
        .verifyChat(chatId, customDescription)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.verifyChat(chat: Chat, noinline customDescription: () -> String?): Unit =
    eu.vendeli.tgbot.api.verification
        .verifyChat(chat, customDescription)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.verifyUser(userId: Long, customDescription: String? = null): Unit =
    eu.vendeli.tgbot.api.verification
        .verifyUser(userId, customDescription)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.verifyUser(userId: Long, noinline customDescription: () -> String?): Unit =
    eu.vendeli.tgbot.api.verification
        .verifyUser(userId, customDescription)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.verifyUser(user: User, noinline customDescription: () -> String?): Unit =
    eu.vendeli.tgbot.api.verification
        .verifyUser(user, customDescription)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getStarTransactions(offset: Int? = null, limit: Int? = null): Unit =
    eu.vendeli.tgbot.api.payments
        .getStarTransactions(offset, limit)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.refundStarPayment(telegramPaymentChargeId: String, userId: Long): Unit =
    eu.vendeli.tgbot.api.payments
        .refundStarPayment(telegramPaymentChargeId, userId)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.createInvoiceLink(
    title: String,
    description: String,
    payload: String,
    currency: Currency,
    prices: List<LabeledPrice>,
): Unit = eu.vendeli.tgbot.api.payments
    .createInvoiceLink(title, description, payload, currency, prices)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.createInvoiceLink(
    title: String,
    description: String,
    currency: Currency,
    vararg prices: LabeledPrice,
    noinline payload: () -> String,
): Unit =
    eu.vendeli.tgbot.api.payments
        .createInvoiceLink(title, description, currency, prices = prices, payload)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.invoice(
    title: String,
    description: String,
    payload: String,
    providerToken: String? = null,
    currency: Currency,
    prices: List<LabeledPrice>,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.payments
    .invoice(title, description, payload, providerToken, currency, prices)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.invoice(
    title: String,
    description: String,
    providerToken: String? = null,
    currency: Currency,
    vararg prices: LabeledPrice,
    noinline payload: () -> String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.payments
    .invoice(title, description, providerToken, currency, prices = prices, payload)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendInvoice(
    title: String,
    description: String,
    payload: String,
    providerToken: String? = null,
    currency: Currency,
    prices: List<LabeledPrice>,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.payments
    .sendInvoice(title, description, payload, providerToken, currency, prices)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editUserStarSubscription(
    userId: Long,
    telegramPaymentChargeId: String,
    isCanceled: Boolean,
): Unit = eu.vendeli.tgbot.api.payments
    .editUserStarSubscription(userId, telegramPaymentChargeId, isCanceled)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setPassportDataErrors(userId: Long, errors: List<PassportElementError>): Unit =
    eu.vendeli.tgbot.api.passport
        .setPassportDataErrors(userId, errors)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setPassportDataErrors(
    userId: Long,
    noinline errors: ListingBuilder<PassportElementError>.() -> Unit,
): Unit = eu.vendeli.tgbot.api.passport
    .setPassportDataErrors(userId, errors)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setPassportDataError(userId: Long, vararg error: PassportElementError): Unit =
    eu.vendeli.tgbot.api.passport
        .setPassportDataError(userId, error = error)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setMyDefaultAdministratorRights(
    rights: ChatAdministratorRights? = null,
    forChannel: Boolean? = null,
): Unit = eu.vendeli.tgbot.api.botactions
    .setMyDefaultAdministratorRights(rights, forChannel)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.savePreparedInlineMessage(userId: Long, result: InlineQueryResult): Unit =
    eu.vendeli.tgbot.api.botactions
        .savePreparedInlineMessage(userId, result)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.savePreparedInlineMessage(
    userId: Long,
    noinline result: () -> InlineQueryResult,
): Unit = eu.vendeli.tgbot.api.botactions
    .savePreparedInlineMessage(userId, result)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getMe(): Unit = eu.vendeli.tgbot.api.botactions
    .getMe()
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setUserEmojiStatus(
    userId: Long,
    emojiStatusCustomEmojiId: String? = null,
    emojiStatusExpirationDate: Instant? = null,
): Unit =
    eu.vendeli.tgbot.api.botactions
        .setUserEmojiStatus(userId, emojiStatusCustomEmojiId, emojiStatusExpirationDate)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setMyName(name: String? = null, languageCode: String? = null): Unit =
    eu.vendeli.tgbot.api.botactions
        .setMyName(name, languageCode)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.logOut(): Unit = eu.vendeli.tgbot.api.botactions
    .logOut()
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendGift(
    giftId: String,
    userId: Long? = null,
    payForUpgrade: Boolean? = null,
    textParseMode: ParseMode? = null,
    noinline text: () -> String?,
): Unit = eu.vendeli.tgbot.api.botactions
    .sendGift(giftId, userId, payForUpgrade, textParseMode, text)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getBusinessConnection(businessConnectionId: String): Unit =
    eu.vendeli.tgbot.api.botactions
        .getBusinessConnection(businessConnectionId)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setMyShortDescription(
    description: String? = null,
    languageCode: String? = null,
): Unit = eu.vendeli.tgbot.api.botactions
    .setMyShortDescription(description, languageCode)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getMyDescription(languageCode: String? = null): Unit =
    eu.vendeli.tgbot.api.botactions
        .getMyDescription(languageCode)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getMyDefaultAdministratorRights(forChannel: Boolean? = null): Unit =
    eu.vendeli.tgbot.api.botactions
        .getMyDefaultAdministratorRights(forChannel)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getMyCommands(
    languageCode: String? = null,
    scope: BotCommandScope? = null,
): Unit = eu.vendeli.tgbot.api.botactions
    .getMyCommands(languageCode, scope)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getUpdates(): Unit = eu.vendeli.tgbot.api.botactions
    .getUpdates()
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteMyCommands(
    languageCode: String? = null,
    scope: BotCommandScope? = null,
): Unit = eu.vendeli.tgbot.api.botactions
    .deleteMyCommands(languageCode, scope)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteWebhook(dropPendingUpdates: Boolean): Unit =
    eu.vendeli.tgbot.api.botactions
        .deleteWebhook(dropPendingUpdates)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getWebhookInfo(): Unit =
    eu.vendeli.tgbot.api.botactions
        .getWebhookInfo()
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getMyName(languageCode: String? = null): Unit =
    eu.vendeli.tgbot.api.botactions
        .getMyName(languageCode)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.close(): Unit = eu.vendeli.tgbot.api.botactions
    .close()
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setWebhook(url: String): Unit =
    eu.vendeli.tgbot.api.botactions
        .setWebhook(url)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getAvailableGifts(): Unit =
    eu.vendeli.tgbot.api.botactions
        .getAvailableGifts()
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setMyDescription(
    description: String? = null,
    languageCode: String? = null,
): Unit = eu.vendeli.tgbot.api.botactions
    .setMyDescription(description, languageCode)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getMyShortDescription(languageCode: String? = null): Unit =
    eu.vendeli.tgbot.api.botactions
        .getMyShortDescription(languageCode)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setMyCommands(
    languageCode: String? = null,
    scope: BotCommandScope? = null,
    command: List<BotCommand>,
): Unit = eu.vendeli.tgbot.api.botactions
    .setMyCommands(languageCode, scope, command)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setMyCommands(
    languageCode: String? = null,
    scope: BotCommandScope? = null,
    vararg command: BotCommand,
): Unit = eu.vendeli.tgbot.api.botactions
    .setMyCommands(languageCode, scope, command = command)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setMyCommands(
    languageCode: String? = null,
    scope: BotCommandScope? = null,
    noinline block: BotCommandsBuilder.() -> Unit,
): Unit = eu.vendeli.tgbot.api.botactions
    .setMyCommands(languageCode, scope, block)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.photo(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .photo(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.photo(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .photo(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.photo(ba: ByteArray, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .photo(ba)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.photo(`file`: InputFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .photo(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendPhoto(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendPhoto(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendPhoto(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendPhoto(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendPaidMedia(
    starCount: Int,
    media: List<InputPaidMedia>,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.media
    .sendPaidMedia(starCount, media)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendPaidMedia(
    starCount: Int,
    noinline media: ListingBuilder<InputPaidMedia>.() -> Unit,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.media
    .sendPaidMedia(starCount, media)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendPaidMedia(
    starCount: Int,
    vararg media: InputPaidMedia,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.media
    .sendPaidMedia(starCount, media = media)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.voice(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .voice(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.voice(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .voice(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.voice(`file`: InputFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .voice(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.voice(ba: ByteArray, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .voice(ba)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendVoice(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendVoice(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendVoice(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendVoice(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getUserProfilePhotos(
    userId: Long,
    offset: Int? = null,
    limit: Int? = null,
): Unit = eu.vendeli.tgbot.api.media
    .getUserProfilePhotos(userId, offset, limit)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getUserProfilePhotos(
    user: User,
    offset: Int? = null,
    limit: Int? = null,
): Unit = eu.vendeli.tgbot.api.media
    .getUserProfilePhotos(user, offset, limit)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.document(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .document(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.document(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .document(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.document(ba: ByteArray, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .document(ba)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.document(`file`: InputFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .document(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendDocument(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendDocument(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendDocument(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendDocument(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.video(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .video(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.video(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .video(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.video(ba: ByteArray, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .video(ba)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.video(`file`: InputFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .video(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendVideo(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendVideo(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendVideo(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendVideo(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sticker(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sticker(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sticker(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sticker(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sticker(ba: ByteArray, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sticker(ba)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sticker(`file`: InputFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sticker(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendSticker(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendSticker(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendSticker(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendSticker(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendMediaGroup(media: List<InputMedia>, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendMediaGroup(media)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendMediaGroup(vararg media: InputMedia, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendMediaGroup(media = media)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.mediaGroup(vararg media: InputMedia.Audio, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .mediaGroup(media = media)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.mediaGroup(vararg media: InputMedia.Document, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .mediaGroup(media = media)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.mediaGroup(vararg media: InputMedia.Photo, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .mediaGroup(media = media)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.mediaGroup(vararg media: InputMedia.Video, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .mediaGroup(media = media)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.videoNote(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .videoNote(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.videoNote(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .videoNote(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.videoNote(ba: ByteArray, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .videoNote(ba)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.videoNote(input: InputFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .videoNote(input)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendVideoNote(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendVideoNote(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendVideoNote(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendVideoNote(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.audio(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .audio(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.audio(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .audio(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.audio(ba: ByteArray, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .audio(ba)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.audio(`file`: InputFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .audio(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendAudio(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendAudio(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendAudio(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendAudio(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.animation(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .animation(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.animation(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .animation(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.animation(ba: ByteArray, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .animation(ba)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.animation(`file`: InputFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .animation(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendAnimation(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendAnimation(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendAnimation(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.media
        .sendAnimation(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getFile(fileId: String): Unit =
    eu.vendeli.tgbot.api.media
        .getFile(fileId)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getChatMemberCount(chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .getChatMemberCount()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setChatMenuButton(menuButton: MenuButton, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .setChatMenuButton(menuButton)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.unpinAllChatMessages(chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .unpinAllChatMessages()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getUserChatBoosts(userId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .getUserChatBoosts(userId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getUserChatBoosts(user: User, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .getUserChatBoosts(user)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.unpinChatMessage(messageId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .unpinChatMessage(messageId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteChatStickerSet(chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .deleteChatStickerSet()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.unbanChatSenderChat(senderChatId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .unbanChatSenderChat(senderChatId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.unbanChatSenderChat(senderChatId: Chat, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .unbanChatSenderChat(senderChatId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.unbanChatSenderChat(senderChatId: User, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .unbanChatSenderChat(senderChatId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getChatMenuButton(chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .getChatMenuButton()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.banChatSenderChat(senderChatId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .banChatSenderChat(senderChatId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.banChatSenderChat(senderChatId: Chat, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .banChatSenderChat(senderChatId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.banChatSenderChat(senderChatId: User, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .banChatSenderChat(senderChatId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setChatStickerSet(stickerSetName: String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .setChatStickerSet(stickerSetName)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getChat(chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .getChat()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setChatAdministratorCustomTitle(
    userId: Long,
    customTitle: String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .setChatAdministratorCustomTitle(userId, customTitle)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setChatAdministratorCustomTitle(
    user: User,
    customTitle: String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .setChatAdministratorCustomTitle(user, customTitle)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editChatInviteLink(inviteLink: String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .editChatInviteLink(inviteLink)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.leaveChat(chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .leaveChat()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setChatDescription(title: String? = null, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .setChatDescription(title)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getChatAdministrators(chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .getChatAdministrators()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteChatPhoto(chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .deleteChatPhoto()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.approveChatJoinRequest(userId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .approveChatJoinRequest(userId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.approveChatJoinRequest(user: User, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .approveChatJoinRequest(user)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editChatSubscriptionInviteLink(
    inviteLink: String,
    name: String? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .editChatSubscriptionInviteLink(inviteLink, name)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setChatPhoto(`file`: ImplicitFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .setChatPhoto(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setChatPhoto(noinline block: () -> String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .setChatPhoto(block)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setChatPhoto(`file`: InputFile, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .setChatPhoto(file)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setChatPermissions(
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .setChatPermissions(permissions, useIndependentChatPermissions)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setChatPermissions(
    useIndependentChatPermissions: Boolean? = null,
    noinline permissions: ChatPermissions.() -> Unit,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .setChatPermissions(useIndependentChatPermissions, permissions)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.createChatSubscriptionInviteLink(
    subscriptionPrice: Int,
    name: String? = null,
    subscriptionPeriod: Duration,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .createChatSubscriptionInviteLink(subscriptionPrice, name, subscriptionPeriod)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.exportChatInviteLink(chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .exportChatInviteLink()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.pinChatMessage(
    messageId: Long,
    disableNotification: Boolean? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .pinChatMessage(messageId, disableNotification)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getChatMember(userId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .getChatMember(userId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getChatMember(user: User, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .getChatMember(user)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.unbanChatMember(
    userId: Long,
    onlyIfBanned: Boolean? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .unbanChatMember(userId, onlyIfBanned)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.unbanChatMember(
    user: User,
    onlyIfBanned: Boolean? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .unbanChatMember(user, onlyIfBanned)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.promoteChatMember(userId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .promoteChatMember(userId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.promoteChatMember(user: User, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .promoteChatMember(user)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.chatAction(
    action: ChatAction,
    messageThreadId: Int? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .chatAction(action, messageThreadId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.chatAction(
    messageThreadId: Int? = null,
    noinline block: () -> ChatAction,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .chatAction(messageThreadId, block)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendChatAction(
    messageThreadId: Int? = null,
    noinline block: () -> ChatAction,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .sendChatAction(messageThreadId, block)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendChatAction(
    action: ChatAction,
    messageThreadId: Int? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .sendChatAction(action, messageThreadId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.banChatMember(
    userId: Long,
    untilDate: Instant? = null,
    revokeMessages: Boolean? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .banChatMember(userId, untilDate, revokeMessages)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.banChatMember(
    user: User,
    untilDate: Instant? = null,
    revokeMessages: Boolean? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .banChatMember(user, untilDate, revokeMessages)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.restrictChatMember(
    userId: Long,
    untilDate: Instant? = null,
    useIndependentChatPermissions: Boolean? = null,
    noinline chatPermissions: ChatPermissions.() -> Unit,
    chatId: Long,
): Unit =
    eu.vendeli.tgbot.api.chat
        .restrictChatMember(userId, untilDate, useIndependentChatPermissions, chatPermissions)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.restrictChatMember(
    userId: Long,
    chatPermissions: ChatPermissions,
    untilDate: Instant? = null,
    useIndependentChatPermissions: Boolean? = null,
    chatId: Long,
): Unit =
    eu.vendeli.tgbot.api.chat
        .restrictChatMember(userId, chatPermissions, untilDate, useIndependentChatPermissions)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.restrictChatMember(
    user: User,
    untilDate: Instant? = null,
    useIndependentChatPermissions: Boolean? = null,
    noinline chatPermissions: ChatPermissions.() -> Unit,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .restrictChatMember(user, untilDate, useIndependentChatPermissions, chatPermissions)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.restrictChatMember(
    user: User,
    chatPermissions: ChatPermissions,
    untilDate: Instant? = null,
    useIndependentChatPermissions: Boolean? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.chat
    .restrictChatMember(user, chatPermissions, untilDate, useIndependentChatPermissions)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.revokeChatInviteLink(inviteLink: String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .revokeChatInviteLink(inviteLink)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.declineChatJoinRequest(userId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .declineChatJoinRequest(userId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.declineChatJoinRequest(user: User, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .declineChatJoinRequest(user)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setChatTitle(title: String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .setChatTitle(title)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.createChatInviteLink(chatId: Long): Unit =
    eu.vendeli.tgbot.api.chat
        .createChatInviteLink()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editStory(
    businessConnectionId: String,
    storyId: String,
    content: InputStoryContent,
): Unit = eu.vendeli.tgbot.api.story
    .editStory(businessConnectionId, storyId, content)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteStory(businessConnectionId: String, storyId: String): Unit =
    eu.vendeli.tgbot.api.story
        .deleteStory(businessConnectionId, storyId)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.postStory(
    businessConnectionId: String,
    content: InputStoryContent,
    activePeriod: Duration,
): Unit = eu.vendeli.tgbot.api.story
    .postStory(businessConnectionId, content, activePeriod)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMessageCaption(messageId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .editMessageCaption(messageId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMessageCaption(chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .editMessageCaption()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editCaption(chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .editCaption()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editCaption(messageId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .editCaption(messageId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMessageReplyMarkup(chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .editMessageReplyMarkup()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMarkup(messageId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .editMarkup(messageId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMarkup(chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .editMarkup()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMessageReplyMarkup(messageId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .editMessageReplyMarkup(messageId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMessageMedia(
    messageId: Long,
    inputMedia: InputMedia,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .editMessageMedia(messageId, inputMedia)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMessageMedia(inputMedia: InputMedia, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .editMessageMedia(inputMedia)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMedia(
    messageId: Long,
    inputMedia: InputMedia,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .editMedia(messageId, inputMedia)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMedia(inputMedia: InputMedia, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .editMedia(inputMedia)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMessageLiveLocation(
    messageId: Long,
    latitude: Float,
    longitude: Float,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .editMessageLiveLocation(messageId, latitude, longitude)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMessageLiveLocation(
    latitude: Float,
    longitude: Float,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .editMessageLiveLocation(latitude, longitude)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.copyMessage(
    fromChatId: Identifier,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .copyMessage(fromChatId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.copyMessage(
    fromChatId: Long,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .copyMessage(fromChatId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.copyMessage(
    fromChatId: String,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .copyMessage(fromChatId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.copyMessage(
    fromChatId: User,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .copyMessage(fromChatId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.copyMessage(
    fromChatId: Chat,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .copyMessage(fromChatId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.copyMessages(
    fromChatId: Identifier,
    messageIds: List<Long>,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .copyMessages(fromChatId, messageIds)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.copyMessages(
    fromChatId: Long,
    vararg messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .copyMessages(fromChatId, messageId = messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.copyMessages(
    fromChatId: String,
    vararg messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .copyMessages(fromChatId, messageId = messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.copyMessages(
    fromChatId: User,
    vararg messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .copyMessages(fromChatId, messageId = messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.copyMessages(
    fromChatId: Chat,
    vararg messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .copyMessages(fromChatId, messageId = messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.stopMessageLiveLocation(messageId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .stopMessageLiveLocation(messageId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.stopMessageLiveLocation(chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .stopMessageLiveLocation()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setMessageReaction(
    messageId: Long,
    reaction: List<ReactionType>? = null,
    isBig: Boolean? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .setMessageReaction(messageId, reaction, isBig)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setMessageReaction(
    messageId: Long,
    vararg reaction: ReactionType,
    isBig: Boolean? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .setMessageReaction(messageId, reaction = reaction, isBig)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setMessageReaction(
    messageId: Long,
    isBig: Boolean? = null,
    noinline reaction: ListingBuilder<ReactionType>.() -> Unit,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .setMessageReaction(messageId, isBig, reaction)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.forwardMessages(
    fromChatId: Identifier,
    messageIds: List<Long>,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .forwardMessages(fromChatId, messageIds)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.forwardMessages(
    fromChatId: Long,
    vararg messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .forwardMessages(fromChatId, messageId = messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.forwardMessages(
    fromChatId: String,
    vararg messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .forwardMessages(fromChatId, messageId = messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.forwardMessages(
    fromChatId: User,
    vararg messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .forwardMessages(fromChatId, messageId = messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.forwardMessages(
    fromChatId: Chat,
    vararg messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .forwardMessages(fromChatId, messageId = messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteMessages(messageIds: List<Long>, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .deleteMessages(messageIds)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteMessages(vararg messageId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .deleteMessages(messageId = messageId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.message(text: String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .message(text)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.message(
    noinline block: EntitiesCtxBuilder<SendMessageAction>.() -> String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .message(block)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendMessage(text: String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .sendMessage(text)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendMessage(
    noinline block: EntitiesCtxBuilder<SendMessageAction>.() -> String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .sendMessage(block)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMessageText(
    messageId: Long,
    noinline block: () -> String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .editMessageText(messageId, block)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editMessageText(
    noinline block: EntitiesCtxBuilder<EditMessageTextAction>.() -> String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .editMessageText(block)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editText(
    messageId: Long,
    noinline block: () -> String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .editText(messageId, block)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editText(
    noinline block: EntitiesCtxBuilder<EditMessageTextAction>.() -> String,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .editText(block)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteMessage(messageId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.message
        .deleteMessage(messageId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.forwardMessage(
    fromChatId: Identifier,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .forwardMessage(fromChatId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.forwardMessage(
    fromChatId: Long,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .forwardMessage(fromChatId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.forwardMessage(
    fromChatId: String,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .forwardMessage(fromChatId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.forwardMessage(
    fromChatId: User,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .forwardMessage(fromChatId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.forwardMessage(
    fromChatId: Chat,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.message
    .forwardMessage(fromChatId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.createForumTopic(
    name: String,
    iconColor: IconColor? = null,
    iconCustomEmojiId: String? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.forum
    .createForumTopic(name, iconColor, iconCustomEmojiId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.closeGeneralForumTopic(chatId: Long): Unit =
    eu.vendeli.tgbot.api.forum
        .closeGeneralForumTopic()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.closeForumTopic(messageThreadId: Int, chatId: Long): Unit =
    eu.vendeli.tgbot.api.forum
        .closeForumTopic(messageThreadId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.reopenForumTopic(messageThreadId: Int, chatId: Long): Unit =
    eu.vendeli.tgbot.api.forum
        .reopenForumTopic(messageThreadId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.unhideGeneralForumTopic(chatId: Long): Unit =
    eu.vendeli.tgbot.api.forum
        .unhideGeneralForumTopic()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.unpinAllForumTopicMessages(messageThreadId: Int, chatId: Long): Unit =
    eu.vendeli.tgbot.api.forum
        .unpinAllForumTopicMessages(messageThreadId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteForumTopic(messageThreadId: Int, chatId: Long): Unit =
    eu.vendeli.tgbot.api.forum
        .deleteForumTopic(messageThreadId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.unpinAllGeneralForumTopicMessages(chatId: Long): Unit =
    eu.vendeli.tgbot.api.forum
        .unpinAllGeneralForumTopicMessages()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.hideGeneralForumTopic(chatId: Long): Unit =
    eu.vendeli.tgbot.api.forum
        .hideGeneralForumTopic()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.reopenGeneralForumTopic(chatId: Long): Unit =
    eu.vendeli.tgbot.api.forum
        .reopenGeneralForumTopic()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editForumTopic(
    messageThreadId: Int,
    name: String? = null,
    iconCustomEmojiId: String? = null,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.forum
    .editForumTopic(messageThreadId, name, iconCustomEmojiId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getForumTopicIconStickers(chatId: Long): Unit =
    eu.vendeli.tgbot.api.forum
        .getForumTopicIconStickers()
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.editGeneralForumTopic(name: String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.forum
        .editGeneralForumTopic(name)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setGameScore(
    userId: Long,
    messageId: Long,
    score: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.game
    .setGameScore(userId, messageId, score)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setGameScore(
    userId: Long,
    score: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.game
    .setGameScore(userId, score)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setGameScore(
    user: User,
    score: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.game
    .setGameScore(user, score)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setGameScore(
    user: User,
    messageId: Long,
    score: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.game
    .setGameScore(user, messageId, score)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.sendGame(gameShortName: String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.game
        .sendGame(gameShortName)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.game(gameShortName: String, chatId: Long): Unit =
    eu.vendeli.tgbot.api.game
        .game(gameShortName)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getGameHighScores(
    userId: Long,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.game
    .getGameHighScores(userId, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getGameHighScores(userId: Long, chatId: Long): Unit =
    eu.vendeli.tgbot.api.game
        .getGameHighScores(userId)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getGameHighScores(
    user: User,
    messageId: Long,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.game
    .getGameHighScores(user, messageId)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getGameHighScores(user: User, chatId: Long): Unit =
    eu.vendeli.tgbot.api.game
        .getGameHighScores(user)
        .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.transferGift(
    businessConnectionId: String,
    ownedGiftId: String,
    newOwnerChatId: Long,
    starCount: Int? = null,
): Unit =
    eu.vendeli.tgbot.api.gift
        .transferGift(businessConnectionId, ownedGiftId, newOwnerChatId, starCount)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.upgradeGift(
    businessConnectionId: String,
    ownedGiftId: String,
    keepOriginalDetails: Boolean? = null,
    starCount: Int? = null,
): Unit =
    eu.vendeli.tgbot.api.gift
        .upgradeGift(businessConnectionId, ownedGiftId, keepOriginalDetails, starCount)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.convertGiftToStars(businessConnectionId: String, ownedGiftId: String): Unit =
    eu.vendeli.tgbot.api.gift
        .convertGiftToStars(businessConnectionId, ownedGiftId)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.giftPremiumSubscription(
    userId: Long,
    monthCount: Int,
    starCount: Int,
    textParseMode: ParseMode? = null,
    noinline text: (() -> String)? = null,
): Unit =
    eu.vendeli.tgbot.api.gift
        .giftPremiumSubscription(userId, monthCount, starCount, textParseMode, text)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteStickerFromSet(sticker: String): Unit =
    eu.vendeli.tgbot.api.stickerset
        .deleteStickerFromSet(sticker)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setStickerMaskPosition(
    sticker: String,
    maskPosition: MaskPosition? = null,
): Unit = eu.vendeli.tgbot.api.stickerset
    .setStickerMaskPosition(sticker, maskPosition)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setStickerEmojiList(sticker: String, emojiList: List<String>): Unit =
    eu.vendeli.tgbot.api.stickerset
        .setStickerEmojiList(sticker, emojiList)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.uploadStickerFile(
    userId: Long,
    sticker: InputFile,
    stickerFormat: StickerFormat,
): Unit = eu.vendeli.tgbot.api.stickerset
    .uploadStickerFile(userId, sticker, stickerFormat)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.createNewStickerSet(
    userId: Long,
    name: String,
    title: String,
    stickers: List<InputSticker>,
): Unit = eu.vendeli.tgbot.api.stickerset
    .createNewStickerSet(userId, name, title, stickers)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setStickerPositionInSet(sticker: String, position: Int): Unit =
    eu.vendeli.tgbot.api.stickerset
        .setStickerPositionInSet(sticker, position)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.replaceStickerInSet(
    userId: Long,
    name: String,
    oldSticker: String,
    sticker: InputSticker,
    chatId: Long,
): Unit = eu.vendeli.tgbot.api.stickerset
    .replaceStickerInSet(userId, name, oldSticker, sticker)
    .send(chatId, this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setStickerSetTitle(name: String, title: String): Unit =
    eu.vendeli.tgbot.api.stickerset
        .setStickerSetTitle(name, title)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.addStickerToSet(
    userId: Long,
    name: String,
    input: InputSticker,
): Unit = eu.vendeli.tgbot.api.stickerset
    .addStickerToSet(userId, name, input)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.addStickerToSet(
    userId: Long,
    name: String,
    noinline input: () -> InputSticker,
): Unit = eu.vendeli.tgbot.api.stickerset
    .addStickerToSet(userId, name, input)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setStickerSetThumbnail(
    name: String,
    userId: Long,
    format: StickerFormat,
    thumbnail: ImplicitFile? = null,
): Unit = eu.vendeli.tgbot.api.stickerset
    .setStickerSetThumbnail(name, userId, format, thumbnail)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setStickerKeywords(sticker: String, keywords: List<String>? = null): Unit =
    eu.vendeli.tgbot.api.stickerset
        .setStickerKeywords(sticker, keywords)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getStickerSet(name: String): Unit =
    eu.vendeli.tgbot.api.stickerset
        .getStickerSet(name)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.setCustomEmojiStickerSetThumbnail(
    name: String,
    customEmojiId: String? = null,
): Unit = eu.vendeli.tgbot.api.stickerset
    .setCustomEmojiStickerSetThumbnail(name, customEmojiId)
    .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.deleteStickerSet(name: String): Unit =
    eu.vendeli.tgbot.api.stickerset
        .deleteStickerSet(name)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getCustomEmojiStickers(customEmojiIds: List<String>): Unit =
    eu.vendeli.tgbot.api.stickerset
        .getCustomEmojiStickers(customEmojiIds)
        .send(this)

@Suppress("NOTHING_TO_INLINE")
public suspend inline fun TelegramBot.getCustomEmojiStickers(vararg customEmojiId: String): Unit =
    eu.vendeli.tgbot.api.stickerset
        .getCustomEmojiStickers(customEmojiId = customEmojiId)
        .send(this)
