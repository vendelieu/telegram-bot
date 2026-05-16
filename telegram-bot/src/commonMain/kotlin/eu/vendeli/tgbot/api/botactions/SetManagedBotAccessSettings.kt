@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

@TgAPI
class SetManagedBotAccessSettingsAction(
    userId: Long,
    isAccessRestricted: Boolean,
    addedUserIds: List<Long>? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setManagedBotAccessSettings")
    override val method = "setManagedBotAccessSettings"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["is_access_restricted"] = isAccessRestricted.toJsonElement()
        if (addedUserIds != null) {
            parameters["added_user_ids"] = addedUserIds.encodeWith(ListSerializer(Long.serializer()))
        }
    }
}

/**
 * Use this method to change the access settings of a managed bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setmanagedbotaccesssettings)
 * @param userId User identifier of the managed bot whose access settings will be changed
 * @param isAccessRestricted Pass True, if only selected users can access the bot. The bot's owner can always access it.
 * @param addedUserIds A JSON-serialized list of up to 10 identifiers of users who will have access to the bot in addition to its owner. Ignored if is_access_restricted is False.
 * @returns [Boolean]
 */
@TgAPI
inline fun setManagedBotAccessSettings(
    userId: Long,
    isAccessRestricted: Boolean,
    addedUserIds: List<Long>? = null,
) = SetManagedBotAccessSettingsAction(userId, isAccessRestricted, addedUserIds)
