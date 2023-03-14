@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.GetUpdatesOptions
import eu.vendeli.tgbot.utils.getInnerType
import eu.vendeli.tgbot.utils.getReturnType

class GetUpdatesAction :
    SimpleAction<List<Update>>,
    ActionState(),
    OptionsFeature<GetUpdatesAction, GetUpdatesOptions> {
    override val TgAction<List<Update>>.method: TgMethod
        get() = TgMethod("getUpdates")
    override val TgAction<List<Update>>.returnType: Class<List<Update>>
        get() = getReturnType()
    override val OptionsFeature<GetUpdatesAction, GetUpdatesOptions>.options: GetUpdatesOptions
        get() = GetUpdatesOptions()
    override val TgAction<List<Update>>.wrappedDataType: Class<Update>
        get() = getInnerType()
}

fun getUpdates() = GetUpdatesAction()
