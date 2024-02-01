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

inline fun deleteWebhook(dropPendingUpdates: Boolean = false) = DeleteWebhookAction(dropPendingUpdates)
