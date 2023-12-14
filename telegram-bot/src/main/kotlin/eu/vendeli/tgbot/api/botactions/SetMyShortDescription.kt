@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetMyShortDescriptionAction(
    shortDescription: String? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setMyShortDescription")
    override val returnType = getReturnType()

    init {
        if (shortDescription != null) parameters["short_description"] = shortDescription
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setMyShortDescription(
    description: String? = null,
    languageCode: String? = null,
) = SetMyShortDescriptionAction(description, languageCode)
