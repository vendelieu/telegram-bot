@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.ChatPermissions
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.datetime.Instant

class RestrictChatMemberAction(
    userId: Long,
    permissions: ChatPermissions,
    untilDate: Instant? = null,
    useIndependentChatPermissions: Boolean? = null,
) : Action<Boolean>() {
    override val method = TgMethod("restrictChatMember")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["permissions"] = permissions.encodeWith(ChatPermissions.serializer())
        if (untilDate != null) parameters["until_date"] = untilDate.epochSeconds.toJsonElement()
        if (useIndependentChatPermissions != null)
            parameters["use_independent_chat_permissions"] = useIndependentChatPermissions.toJsonElement()
    }
}/**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId  Required
 * @param userId  Required
 * @param permissions  Required
 * @param useIndependentChatPermissions  Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate  Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*//**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId * 
 * @param userId * 
 * @param permissions * 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*//**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId Required* 
 * @param userId Required* 
 * @param permissions Required* 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*//**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @param permissions Required 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*//**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @param permissions Required 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*//**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @param permissions Required 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*//**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @param permissions Required 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*//**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @param permissions Required 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*//**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @param permissions Required 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*//**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @param permissions Required 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*//**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @param permissions Required 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*//**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @param permissions Required 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#restrictchatmember
*/













inline fun restrictChatMember(
    userId: Long,
    untilDate: Instant? = null,
    useIndependentChatPermissions: Boolean? = null,
    chatPermissions: ChatPermissions.() -> Unit,
) = RestrictChatMemberAction(userId, ChatPermissions().apply(chatPermissions), untilDate, useIndependentChatPermissions)

@Suppress("NOTHING_TO_INLINE")
inline fun restrictChatMember(
    userId: Long,
    chatPermissions: ChatPermissions,
    untilDate: Instant? = null,
    useIndependentChatPermissions: Boolean? = null,
) = RestrictChatMemberAction(userId, chatPermissions, untilDate, useIndependentChatPermissions)

inline fun restrictChatMember(
    user: User,
    untilDate: Instant? = null,
    useIndependentChatPermissions: Boolean? = null,
    chatPermissions: ChatPermissions.() -> Unit,
) = restrictChatMember(user.id, untilDate, useIndependentChatPermissions, chatPermissions)

@Suppress("NOTHING_TO_INLINE")
inline fun restrictChatMember(
    user: User,
    chatPermissions: ChatPermissions,
    untilDate: Instant? = null,
    useIndependentChatPermissions: Boolean? = null,
) = restrictChatMember(user.id, chatPermissions, untilDate, useIndependentChatPermissions)
