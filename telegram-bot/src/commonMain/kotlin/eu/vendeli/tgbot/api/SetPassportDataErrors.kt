@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.passport.PassportElementError
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.serialization.builtins.ListSerializer

class SetPassportDataErrorsAction(
    userId: Long,
    errors: List<PassportElementError>,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setPassportDataErrors")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["errors"] = errors.encodeWith(ListSerializer(DynamicLookupSerializer))
    }
}

inline fun setPassportDataErrors(userId: Long, errors: List<PassportElementError>) =
    SetPassportDataErrorsAction(userId, errors)
fun setPassportDataErrors(userId: Long, errors: ListingBuilder<PassportElementError>.() -> Unit) =
    setPassportDataErrors(userId, ListingBuilder.build(errors))

inline fun setPassportDataError(userId: Long, vararg error: PassportElementError) =
    setPassportDataErrors(userId, error.asList())
