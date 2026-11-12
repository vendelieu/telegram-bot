package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val DEFAULT_LIMIT = 100
private const val MAX_LIMIT = 100
private const val MAX_CONNECTIONS = 40
private val SECRET_TOKEN = Regex("^[A-Za-z0-9_-]{1,256}$")
private val DEFAULT_EXCLUDED = setOf("chat_member", "message_reaction", "message_reaction_count")

internal object Handlers {
    fun all(): Map<String, Handler> = buildMap {
        fun register(handler: Handler, vararg names: String) = names.forEach { put(it.lowercase(), handler) }

        register(ChatHandlers.getMe, "getMe")
        register(MessageHandlers.send, "sendMediaGroup")
        register(MessageHandlers.editText, "editMessageText")
        register(MessageHandlers.editCaption, "editMessageCaption")
        register(MessageHandlers.editReplyMarkup, "editMessageReplyMarkup")
        register(MessageHandlers.editMedia, "editMessageMedia")
        register(MessageHandlers.editLiveLocation, "editMessageLiveLocation")
        register(MessageHandlers.stopLiveLocation, "stopMessageLiveLocation")
        register(MessageHandlers.stopPoll, "stopPoll")
        register(MessageHandlers.delete, "deleteMessage")
        register(MessageHandlers.deleteMany, "deleteMessages")
        register(MessageHandlers.forward, "forwardMessage")
        register(MessageHandlers.forwardMany, "forwardMessages")
        register(MessageHandlers.copy, "copyMessage")
        register(MessageHandlers.copyMany, "copyMessages")

        register(ChatHandlers.getChat, "getChat")
        register(ChatHandlers.getChatMember, "getChatMember")
        register(ChatHandlers.getChatMemberCount, "getChatMemberCount")
        register(ChatHandlers.getChatAdministrators, "getChatAdministrators")
        register(ChatHandlers.ban, "banChatMember")
        register(ChatHandlers.unban, "unbanChatMember")
        register(ChatHandlers.restrict, "restrictChatMember")
        register(ChatHandlers.promote, "promoteChatMember")
        register(ChatHandlers.leave, "leaveChat")
        register(ChatHandlers.pin, "pinChatMessage")
        register(ChatHandlers.unpin, "unpinChatMessage")
        register(ChatHandlers.unpinAll, "unpinAllChatMessages")
        register(ChatHandlers.chatAction, "sendChatAction")
        register(ChatHandlers.setMyCommands, "setMyCommands")
        register(ChatHandlers.getMyCommands, "getMyCommands")
        register(ChatHandlers.deleteMyCommands, "deleteMyCommands")
        register(ChatHandlers.answerCallback, "answerCallbackQuery")
        register(ChatHandlers.answerInline, "answerInlineQuery")

        register(getUpdates, "getUpdates")
        register(setWebhook, "setWebhook")
        register(deleteWebhook, "deleteWebhook")
        register(getWebhookInfo, "getWebhookInfo")
        register(getFile, "getFile")
        registerExtras()
    }

    private fun MutableMap<String, Handler>.registerExtras() {
        fun register(handler: Handler, vararg names: String) = names.forEach { put(it.lowercase(), handler) }

        register(ChatAdminHandlers.setTitle, "setChatTitle")
        register(ChatAdminHandlers.setDescription, "setChatDescription")
        register(ChatAdminHandlers.setPhoto, "setChatPhoto")
        register(ChatAdminHandlers.deletePhoto, "deleteChatPhoto")
        register(ChatAdminHandlers.setPermissions, "setChatPermissions")
        register(ChatAdminHandlers.setStickerSet, "setChatStickerSet")
        register(ChatAdminHandlers.deleteStickerSet, "deleteChatStickerSet")
        register(ChatAdminHandlers.createLink, "createChatInviteLink")
        register(ChatAdminHandlers.createSubscriptionLink, "createChatSubscriptionInviteLink")
        register(ChatAdminHandlers.exportLink, "exportChatInviteLink")
        register(ChatAdminHandlers.editLink, "editChatInviteLink", "editChatSubscriptionInviteLink")
        register(ChatAdminHandlers.revokeLink, "revokeChatInviteLink")
        register(ChatAdminHandlers.approveJoin, "approveChatJoinRequest")
        register(ChatAdminHandlers.declineJoin, "declineChatJoinRequest")
        register(ChatAdminHandlers.createTopic, "createForumTopic")
        register(ChatAdminHandlers.editTopic, "editForumTopic")
        register(ChatAdminHandlers.closeTopic, "closeForumTopic")
        register(ChatAdminHandlers.reopenTopic, "reopenForumTopic")
        register(ChatAdminHandlers.deleteTopic, "deleteForumTopic")
        register(ChatAdminHandlers.unpinAllTopicMessages, "unpinAllForumTopicMessages")
        register(ChatAdminHandlers.editGeneral, "editGeneralForumTopic")
        register(ChatAdminHandlers.closeGeneral, "closeGeneralForumTopic")
        register(ChatAdminHandlers.reopenGeneral, "reopenGeneralForumTopic")
        register(ChatAdminHandlers.hideGeneral, "hideGeneralForumTopic")
        register(ChatAdminHandlers.unhideGeneral, "unhideGeneralForumTopic")
        register(ChatAdminHandlers.iconStickers, "getForumTopicIconStickers")
        register(ChatAdminHandlers.setReaction, "setMessageReaction")
        register(ChatAdminHandlers.deleteReaction, "deleteMessageReaction")
        register(ChatAdminHandlers.deleteAllReactions, "deleteAllMessageReactions")

        register(StickerHandlers.create, "createNewStickerSet")
        register(StickerHandlers.get, "getStickerSet")
        register(StickerHandlers.add, "addStickerToSet")
        register(StickerHandlers.position, "setStickerPositionInSet")
        register(StickerHandlers.delete, "deleteStickerFromSet")
        register(StickerHandlers.replace, "replaceStickerInSet")
        register(StickerHandlers.emojiList, "setStickerEmojiList")
        register(StickerHandlers.keywords, "setStickerKeywords")
        register(StickerHandlers.maskPosition, "setStickerMaskPosition")
        register(StickerHandlers.title, "setStickerSetTitle")
        register(StickerHandlers.thumbnail, "setStickerSetThumbnail")
        register(StickerHandlers.customEmojiThumbnail, "setCustomEmojiStickerSetThumbnail")
        register(StickerHandlers.deleteSet, "deleteStickerSet")
        register(StickerHandlers.customEmoji, "getCustomEmojiStickers")
        register(StickerHandlers.upload, "uploadStickerFile")

        register(AccountHandlers.setName, "setMyName")
        register(AccountHandlers.getName, "getMyName")
        register(AccountHandlers.setDescription, "setMyDescription")
        register(AccountHandlers.getDescription, "getMyDescription")
        register(AccountHandlers.setShortDescription, "setMyShortDescription")
        register(AccountHandlers.getShortDescription, "getMyShortDescription")
        register(AccountHandlers.setProfilePhoto, "setMyProfilePhoto")
        register(AccountHandlers.removeProfilePhoto, "removeMyProfilePhoto")
        register(AccountHandlers.setMenuButton, "setChatMenuButton")
        register(AccountHandlers.getMenuButton, "getChatMenuButton")
        register(AccountHandlers.setDefaultRights, "setMyDefaultAdministratorRights")
        register(AccountHandlers.getDefaultRights, "getMyDefaultAdministratorRights")

        register(AccountHandlers.getConnection, "getBusinessConnection")
        register(BusinessAccountHandlers.setName, "setBusinessAccountName")
        register(BusinessAccountHandlers.setUsername, "setBusinessAccountUsername")
        register(BusinessAccountHandlers.setBio, "setBusinessAccountBio")
        register(BusinessAccountHandlers.setProfilePhoto, "setBusinessAccountProfilePhoto")
        register(BusinessAccountHandlers.removeProfilePhoto, "removeBusinessAccountProfilePhoto")
        register(BusinessAccountHandlers.setGiftSettings, "setBusinessAccountGiftSettings")
        register(BusinessAccountHandlers.read, "readBusinessMessage")
        register(BusinessAccountHandlers.delete, "deleteBusinessMessages")
        register(AccountHandlers.shareThemeBackdrops, "getChatShareThemeBackdrops")
        register(AccountHandlers.businessStarBalance, "getBusinessAccountStarBalance")
        register(AccountHandlers.transferStars, "transferBusinessAccountStars")
        register(AccountHandlers.postStory, "postStory")
        register(AccountHandlers.editStory, "editStory")
        register(AccountHandlers.deleteStory, "deleteStory")
        register(AccountHandlers.repostStory, "repostStory")

        register(AccountHandlers.availableGifts, "getAvailableGifts")
        register(AccountHandlers.sendGift, "sendGift")
        register(AccountHandlers.userGifts, "getUserGifts")
        register(AccountHandlers.chatGifts, "getChatGifts")
        register(AccountHandlers.starBalance, "getMyStarBalance")
        register(AccountHandlers.starTransactions, "getStarTransactions")
        register(AccountHandlers.refund, "refundStarPayment")
        register(AccountHandlers.createInvoiceLink, "createInvoiceLink")
        register(AccountHandlers.answerShipping, "answerShippingQuery")
        register(AccountHandlers.answerPreCheckout, "answerPreCheckoutQuery")
        register(AccountHandlers.setGameScore, "setGameScore")
        register(AccountHandlers.getGameHighScores, "getGameHighScores")
        register(AccountHandlers.managedToken, "getManagedBotToken")
        register(AccountHandlers.replaceManagedToken, "replaceManagedBotToken")
        register(AccountHandlers.managedAccess, "getManagedBotAccessSettings")
        register(AccountHandlers.setManagedAccess, "setManagedBotAccessSettings")
        register(AccountHandlers.preparedInline, "savePreparedInlineMessage")
        register(AccountHandlers.preparedButton, "savePreparedKeyboardButton")
        register(MessageHandlers.editChecklist, "editMessageChecklist")

        register(SocialHandlers.setCustomTitle, "setChatAdministratorCustomTitle")
        register(SocialHandlers.setMemberTag, "setChatMemberTag")
        register(SocialHandlers.banSenderChat, "banChatSenderChat")
        register(SocialHandlers.unbanSenderChat, "unbanChatSenderChat")
        register(SocialHandlers.setEmojiStatus, "setUserEmojiStatus")
        register(SocialHandlers.verifyUser, "verifyUser")
        register(SocialHandlers.verifyChat, "verifyChat")
        register(SocialHandlers.removeUserVerification, "removeUserVerification")
        register(SocialHandlers.removeChatVerification, "removeChatVerification")
        register(SocialHandlers.userBoosts, "getUserChatBoosts")
        register(SocialHandlers.profilePhotos, "getUserProfilePhotos")
        register(SocialHandlers.profileAudios, "getUserProfileAudios")
        register(SocialHandlers.personalChatMessages, "getUserPersonalChatMessages")
        register(SocialHandlers.setPassportErrors, "setPassportDataErrors")
        register(SocialHandlers.giftPremium, "giftPremiumSubscription")
        register(SocialHandlers.editSubscription, "editUserStarSubscription")
        register(SocialHandlers.messageDraft, "sendMessageDraft")
        register(SocialHandlers.richDraft, "sendRichMessageDraft")
        register(SocialHandlers.editEphemeralText, "editEphemeralMessageText")
        register(SocialHandlers.editEphemeralCaption, "editEphemeralMessageCaption")
        register(SocialHandlers.editEphemeralMarkup, "editEphemeralMessageReplyMarkup")
        register(SocialHandlers.editEphemeralMedia, "editEphemeralMessageMedia")
        register(SocialHandlers.deleteEphemeral, "deleteEphemeralMessage")
        register(SocialHandlers.answerWidget, "answerWidgetQuery")
        register(SocialHandlers.editWidgetData, "editMessageWidgetData")
        register(SocialHandlers.editSharedWidget, "editSharedWidgetData")
        register(SocialHandlers.answerInline, "answerInlineQuery")
        register(SocialHandlers.answerWebApp, "answerWebAppQuery")
        register(SocialHandlers.answerGuest, "answerGuestQuery")
        register(SocialHandlers.answerJoinQuery, "answerChatJoinRequestQuery")
        register(SocialHandlers.joinWebApp, "sendChatJoinRequestWebApp")
        register(SocialHandlers.approveSuggested, "approveSuggestedPost")
        register(SocialHandlers.declineSuggested, "declineSuggestedPost")
        register(GiftHandlers.list, "getBusinessAccountGifts")
        register(GiftHandlers.convert, "convertGiftToStars")
        register(GiftHandlers.upgrade, "upgradeGift")
        register(GiftHandlers.transfer, "transferGift")
        register(GiftHandlers.delete, "deleteBusinessAccountGift")
    }

    /** `sendX` methods that return a Message share one handler, the explicit table wins over it. */
    fun resolve(table: Map<String, Handler>, method: MethodSpec): Handler? {
        table[method.name.lowercase()]?.let { return it }
        val isSend = method.name.startsWith("send") && method.returns == listOf(TypeRef.Obj("Message")) &&
            method.fieldsByName.containsKey("chat_id")
        return if (isSend) MessageHandlers.send else null
    }

    private val getUpdates: Handler = {
        if (bot.webhook != null) {
            throw ApiException.conflict(
                "can't use getUpdates method while webhook is active; use deleteWebhook to delete the webhook first",
            )
        }
        val limit = (params.long("limit") ?: DEFAULT_LIMIT.toLong()).toInt().coerceIn(1, MAX_LIMIT)
        val timeout = (params.long("timeout") ?: 0L).toInt().coerceAtLeast(0)
        (params["allowed_updates"] as? JsonArray)?.let { list ->
            bot.pollAllowedUpdates = list.map { (it as JsonPrimitive).content }
        }
        JsonArray(bot.updates.poll(params.long("offset"), limit, timeout))
    }

    private val setWebhook: Handler = {
        val url = params.str("url").orEmpty()
        if (url.isEmpty()) {
            core.webhooks.stop(bot)
            TRUE
        } else {
            if (core.config.requireHttpsWebhook && !url.startsWith("https://")) {
                throw ApiException.badRequest("bad webhook: An HTTPS URL must be provided for webhook")
            }
            if (!url.startsWith("http://") &&
                !url.startsWith("https://")
            ) throw ApiException.badRequest("bad webhook: unsupported URL protocol")
            val secret = params.str("secret_token")
            if (secret != null &&
                !SECRET_TOKEN.matches(secret)
            ) throw ApiException.badRequest("secret token contains illegal characters")
            if (params.bool("drop_pending_updates") == true) bot.updates.clear()
            val allowed = (params["allowed_updates"] as? JsonArray)?.map { (it as JsonPrimitive).content }
            bot.webhook = WebhookState(
                url,
                secret,
                allowed,
                (params.long("max_connections") ?: MAX_CONNECTIONS.toLong()).toInt(),
                params.str("ip_address"),
            )
            core.webhooks.start(bot)
            TRUE
        }
    }

    private val deleteWebhook: Handler = {
        core.webhooks.stop(bot)
        if (params.bool("drop_pending_updates") == true) bot.updates.clear()
        TRUE
    }

    private val getWebhookInfo: Handler = {
        val webhook = bot.webhook
        buildJsonObject {
            put("url", webhook?.url.orEmpty())
            put("has_custom_certificate", false)
            put("pending_update_count", bot.updates.size)
            webhook?.ipAddress?.let { put("ip_address", it) }
            webhook?.lastErrorDate?.let { put("last_error_date", it) }
            webhook?.lastErrorMessage?.let { put("last_error_message", it) }
            if (webhook != null) put("max_connections", webhook.maxConnections)
            webhook?.allowedUpdates?.let { put("allowed_updates", JsonArray(it.map(::JsonPrimitive))) }
        }
    }

    private val getFile: Handler = {
        val id = params.str("file_id").orEmpty()
        val file =
            bot.files[id] ?: throw ApiException.badRequest("wrong file_id or the file is temporarily unavailable")
        buildJsonObject {
            put("file_id", file.fileId)
            put("file_unique_id", file.uniqueId)
            file.bytes?.let { put("file_size", it.size) }
            put("file_path", file.path)
        }
    }

    /** Update types the bot did not ask for are never queued (Telegram filters at delivery, the effect is the same). */
    fun isAllowed(bot: BotState, update: JsonObject): Boolean {
        val type = update.keys.firstOrNull { it != "update_id" } ?: return true
        val allowed = bot.webhook?.allowedUpdates ?: bot.pollAllowedUpdates
        return if (allowed == null || allowed.isEmpty()) type !in DEFAULT_EXCLUDED else type in allowed
    }
}

internal fun JsonElement.asString(): String? = (this as? JsonPrimitive)?.content
