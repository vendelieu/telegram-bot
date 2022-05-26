package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.File
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetFileAction(fileId: String) : SimpleAction<File> {
    override val method: TgMethod = TgMethod("getFile")
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["file_id"] = fileId
    }
}

fun getFile(fileId: String) = GetFileAction(fileId)
