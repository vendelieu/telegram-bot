@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetMyNameAction(name: String? = null, languageCode: String? = null) : SimpleAction<Boolean>() {
    override val method = TgMethod("setMyName")
    override val returnType = getReturnType()

    init {
        if (name != null) parameters["name"] = name
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setMyName(name: String? = null, languageCode: String? = null) = SetMyNameAction(name, languageCode)
