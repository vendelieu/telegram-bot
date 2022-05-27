package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.internal.TgMethod

class DeleteWebhookAction(dropPendingUpdates: Boolean = false) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("deleteWebhook")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["dropPendingUpdates"] = dropPendingUpdates
    }
}

fun deleteWebhook(dropPendingUpdates: Boolean = false) = DeleteWebhookAction(dropPendingUpdates)
