package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.PassportElementError
import com.github.vendelieu.tgbot.types.internal.TgMethod

class SetPassportDataErrorsAction(userId: Long, errors: List<PassportElementError>) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("setPassportDataErrors")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
        parameters["errors"] = errors
    }
}

fun setPassportDataErrors(userId: Long, errors: List<PassportElementError>) =
    SetPassportDataErrorsAction(userId, errors)

fun setPassportDataErrors(userId: Long, errors: MutableList<PassportElementError>.() -> Unit) =
    SetPassportDataErrorsAction(userId, mutableListOf<PassportElementError>().apply(errors))

fun setPassportDataError(userId: Long, vararg error: PassportElementError) =
    SetPassportDataErrorsAction(userId, listOf(*error))
