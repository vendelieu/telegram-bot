@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.File
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetFileAction(fileId: String) : SimpleAction<File>, ActionState() {
    override val method: TgMethod = TgMethod("getFile")
    override val returnType = getReturnType()

    init {
        parameters["file_id"] = fileId
    }
}

fun getFile(fileId: String) = GetFileAction(fileId)
