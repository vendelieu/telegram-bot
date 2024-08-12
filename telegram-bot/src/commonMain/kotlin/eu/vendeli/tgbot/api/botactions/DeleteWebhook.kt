@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class DeleteWebhookAction(
    dropPendingUpdates: Boolean = false,
) : SimpleAction<Boolean>() {
    override val method = "deleteWebhook"
    override val returnType = getReturnType()

    init {
        parameters["drop_pending_updates"] = dropPendingUpdates.toJsonElement()
    }
}

/**
 * Use this method to remove webhook integration if you decide to switch back to getUpdates. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deletewebhook)
 * @param dropPendingUpdates Pass True to drop all pending updates
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun deleteWebhook(dropPendingUpdates: Boolean = false) = DeleteWebhookAction(dropPendingUpdates)
