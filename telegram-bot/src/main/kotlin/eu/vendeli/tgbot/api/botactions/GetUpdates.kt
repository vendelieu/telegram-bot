@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.GetUpdatesOptions
import eu.vendeli.tgbot.utils.getCollectionReturnType

class GetUpdatesAction :
    SimpleAction<List<Update>>(),
    OptionsFeature<GetUpdatesAction, GetUpdatesOptions> {
    override val method = TgMethod("getUpdates")
    override val collectionReturnType = getCollectionReturnType()
    override val options = GetUpdatesOptions()
}

@Suppress("NOTHING_TO_INLINE")
inline fun getUpdates() = GetUpdatesAction()
