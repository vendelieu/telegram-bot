@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class LogoutAction : SimpleAction<Boolean>() {
    override val method = TgMethod("logOut")
    override val returnType = getReturnType()
}

@Suppress("NOTHING_TO_INLINE")
inline fun logout() = LogoutAction()
