@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.GetUpdatesOptions
import eu.vendeli.tgbot.utils.getInnerType
import eu.vendeli.tgbot.utils.getReturnType

class GetUpdatesAction :
    SimpleAction<List<Update>>(),
    OptionsFeature<GetUpdatesAction, GetUpdatesOptions> {
    override val method = TgMethod("getUpdates")
    override val returnType = getReturnType()
    override val wrappedDataType = getInnerType()
    override val OptionsFeature<GetUpdatesAction, GetUpdatesOptions>.options: GetUpdatesOptions
        get() = GetUpdatesOptions()
}

fun getUpdates() = GetUpdatesAction()
