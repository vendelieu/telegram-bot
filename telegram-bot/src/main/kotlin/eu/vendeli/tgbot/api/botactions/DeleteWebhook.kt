@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod

class DeleteWebhookAction(dropPendingUpdates: Boolean = false) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("deleteWebhook")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["dropPendingUpdates"] = dropPendingUpdates
    }
}

fun deleteWebhook(dropPendingUpdates: Boolean = false) = DeleteWebhookAction(dropPendingUpdates)
