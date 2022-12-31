@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.File
import eu.vendeli.tgbot.types.internal.TgMethod

class GetFileAction(fileId: String) : SimpleAction<File> {
    override val method: TgMethod = TgMethod("getFile")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["file_id"] = fileId
    }
}

fun getFile(fileId: String) = GetFileAction(fileId)
