@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.bot.BotAccessSettings
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetManagedBotAccessSettingsAction(
    userId: Long,
) : SimpleAction<BotAccessSettings>() {
    @TgAPI.Name("getManagedBotAccessSettings")
    override val method = "getManagedBotAccessSettings"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Use this method to get the access settings of a managed bot. Returns a BotAccessSettings object on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getmanagedbotaccesssettings)
 * @param userId User identifier of the managed bot whose access settings will be returned
 * @returns [BotAccessSettings]
 */
@TgAPI
inline fun getManagedBotAccessSettings(userId: Long) = GetManagedBotAccessSettingsAction(userId)
