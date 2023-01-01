@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.PassportElementError
import eu.vendeli.tgbot.types.internal.TgMethod

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
