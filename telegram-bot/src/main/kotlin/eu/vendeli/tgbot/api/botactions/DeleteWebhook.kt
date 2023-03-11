@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeleteWebhookAction(dropPendingUpdates: Boolean = false) : SimpleAction<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("deleteWebhook")
    override val returnType = getReturnType()

    init {
        parameters["dropPendingUpdates"] = dropPendingUpdates
    }
}

fun deleteWebhook(dropPendingUpdates: Boolean = false) = DeleteWebhookAction(dropPendingUpdates)
