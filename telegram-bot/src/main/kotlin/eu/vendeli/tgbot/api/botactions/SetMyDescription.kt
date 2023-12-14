@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetMyDescriptionAction(
    description: String? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setMyDescription")
    override val returnType = getReturnType()

    init {
        if (description != null) parameters["description"] = description
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setMyDescription(
    description: String? = null,
    languageCode: String? = null,
) = SetMyDescriptionAction(description, languageCode)
