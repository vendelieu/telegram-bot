@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.passport

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.passport.PassportElementError
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement
import kotlinx.serialization.builtins.ListSerializer

@TgAPI
class SetPassportDataErrorsAction(
    userId: Long,
    errors: List<PassportElementError>,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setPassportDataErrors")
    override val method = "setPassportDataErrors"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["errors"] = errors.encodeWith(ListSerializer(PassportElementError.serializer()))
    }
}

/**
 * Informs a user that some of the Telegram Passport elements they provided contains errors. The user will not be able to re-submit their Passport to you until the errors are fixed (the contents of the field for which you returned the error must change). Returns True on success.
 * Use this if the data submitted by the user doesn't satisfy the standards your service requires for any reason. For example, if a birthday date seems invalid, a submitted document is blurry, a scan shows evidence of tampering, etc. Supply some details in the error message to make sure the user knows how to correct the issues.
 *
 * [Api reference](https://core.telegram.org/bots/api#setpassportdataerrors)
 * @param userId User identifier
 * @param errors A JSON-serialized array describing the errors
 * @returns [Boolean]
 */
@TgAPI
inline fun setPassportDataErrors(userId: Long, errors: List<PassportElementError>) =
    SetPassportDataErrorsAction(userId, errors)

@TgAPI
fun setPassportDataErrors(userId: Long, errors: ListingBuilder<PassportElementError>.() -> Unit) =
    setPassportDataErrors(userId, ListingBuilder.build(errors))

@TgAPI
inline fun setPassportDataError(userId: Long, vararg error: PassportElementError) =
    setPassportDataErrors(userId, error.asList())
