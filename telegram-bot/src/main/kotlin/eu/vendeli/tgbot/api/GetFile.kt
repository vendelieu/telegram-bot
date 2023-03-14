@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.getReturnType

class GetFileAction(fileId: String) : SimpleAction<File>, ActionState() {
    override val TgAction<File>.method: TgMethod
        get() = TgMethod("getFile")
    override val TgAction<File>.returnType: Class<File>
        get() = getReturnType()

    init {
        parameters["file_id"] = fileId
    }
}

fun getFile(fileId: String) = GetFileAction(fileId)
