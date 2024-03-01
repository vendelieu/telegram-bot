@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class DeleteWebhookAction(dropPendingUpdates: Boolean = false) : SimpleAction<Boolean>() {
    override val method = TgMethod("deleteWebhook")
    override val returnType = getReturnType()

    init {
        parameters["drop_pending_updates"] = dropPendingUpdates.toJsonElement()
    }
}

/**
 * Use this method to remove webhook integration if you decide to switch back to getUpdates. Returns True on success.
 * Api reference: https://core.telegram.org/bots/api#deletewebhook
 * @param dropPendingUpdates Pass True to drop all pending updates
 * @returns [Boolean]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun deleteWebhook(dropPendingUpdates: Boolean = false) = DeleteWebhookAction(dropPendingUpdates)
