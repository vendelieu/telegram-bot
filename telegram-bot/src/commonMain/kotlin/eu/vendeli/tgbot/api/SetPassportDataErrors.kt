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

/**
 * Informs a user that some of the Telegram Passport elements they provided contains errors. The user will not be able to re-submit their Passport to you until the errors are fixed (the contents of the field for which you returned the error must change). Returns True on success.
 * Use this if the data submitted by the user doesn't satisfy the standards your service requires for any reason. For example, if a birthday date seems invalid, a submitted document is blurry, a scan shows evidence of tampering, etc. Supply some details in the error message to make sure the user knows how to correct the issues.
 * @param userId  Required
 * @param errors  Required
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#setpassportdataerrors
*/
@Suppress("NOTHING_TO_INLINE")
inline fun setPassportDataErrors(userId: Long, errors: List<PassportElementError>) =
    SetPassportDataErrorsAction(userId, errors)
fun setPassportDataErrors(userId: Long, errors: ListingBuilder<PassportElementError>.() -> Unit) =
    setPassportDataErrors(userId, ListingBuilder.build(errors))

@Suppress("NOTHING_TO_INLINE")
inline fun setPassportDataError(userId: Long, vararg error: PassportElementError) =
    setPassportDataErrors(userId, error.asList())
