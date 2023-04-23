@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetMyNameAction(name: String? = null, languageCode: String? = null) : SimpleAction<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("setMyName")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()

    init {
        if (name != null) parameters["name"] = name
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun setMyName(name: String? = null, languageCode: String? = null) = SetMyNameAction(name, languageCode)
