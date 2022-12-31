@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.*
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.GetUpdatesOptions

class GetUpdatesAction :
    SimpleAction<List<Update>>,
    OptionAble,
    OptionsFeature<GetUpdatesAction, GetUpdatesOptions>,
    MultiResponseOf<Update> {
    override val method: TgMethod = TgMethod("getUpdates")
    override var options = GetUpdatesOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    override fun <T : MultipleResponse> TgAction.bunchResponseInnerType(): Class<T> = getInnerType() as Class<T>
}

fun getUpdates() = GetUpdatesAction()
