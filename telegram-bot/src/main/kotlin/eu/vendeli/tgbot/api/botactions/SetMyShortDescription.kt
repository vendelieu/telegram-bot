@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetMyShortDescriptionAction(
    shortDescription: String? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setMyShortDescription")
    override val returnType = getReturnType()

    init {
        if (shortDescription != null) parameters["short_description"] = shortDescription.toJsonElement()
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setMyShortDescription(
    description: String? = null,
    languageCode: String? = null,
) = SetMyShortDescriptionAction(description, languageCode)
