@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class LogoutAction : SimpleAction<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("logOut")
    override val returnType = getReturnType()
}

fun logout() = LogoutAction()
