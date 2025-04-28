package eu.vendeli.tgbot.types.business

import kotlinx.serialization.Serializable

/**
 * Represents the rights of a business bot.
 *
 * [Api reference](https://core.telegram.org/bots/api#businessbotrights)
 * @property canReply Optional. True, if the bot can send and edit messages in the private chats that had incoming messages in the last 24 hours
 * @property canReadMessages Optional. True, if the bot can mark incoming private messages as read
 * @property canDeleteSentMessages Optional. True, if the bot can delete messages sent by the bot
 * @property canDeleteAllMessages Optional. True, if the bot can delete all private messages in managed chats
 * @property canEditName Optional. True, if the bot can edit the first and last name of the business account
 * @property canEditBio Optional. True, if the bot can edit the bio of the business account
 * @property canEditProfilePhoto Optional. True, if the bot can edit the profile photo of the business account
 * @property canEditUsername Optional. True, if the bot can edit the username of the business account
 * @property canChangeGiftSettings Optional. True, if the bot can change the privacy settings pertaining to gifts for the business account
 * @property canViewGiftsAndStars Optional. True, if the bot can view gifts and the amount of Telegram Stars owned by the business account
 * @property canConvertGiftsToStars Optional. True, if the bot can convert regular gifts owned by the business account to Telegram Stars
 * @property canTransferAndUpgradeGifts Optional. True, if the bot can transfer and upgrade gifts owned by the business account
 * @property canTransferStars Optional. True, if the bot can transfer Telegram Stars received by the business account to its own account, or use them to upgrade and transfer gifts
 * @property canManageStories Optional. True, if the bot can post, edit and delete stories on behalf of the business account
 */
@Serializable
data class BusinessBotRights(
    val canReply: Boolean? = null,
    val canReadMessages: Boolean? = null,
    val canDeleteSentMessages: Boolean? = null,
    val canDeleteAllMessages: Boolean? = null,
    val canEditName: Boolean? = null,
    val canEditBio: Boolean? = null,
    val canEditProfilePhoto: Boolean? = null,
    val canEditUsername: Boolean? = null,
    val canChangeGiftSettings: Boolean? = null,
    val canViewGiftsAndStars: Boolean? = null,
    val canConvertGiftsToStars: Boolean? = null,
    val canTransferAndUpgradeGifts: Boolean? = null,
    val canTransferStars: Boolean? = null,
    val canManageStories: Boolean? = null,
)
