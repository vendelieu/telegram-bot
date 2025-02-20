package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.action.TgAction
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.utils.builders.EntitiesBuilder
import eu.vendeli.tgbot.utils.builders.EntitiesCtxBuilder
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.toJsonElement

/**
 * Caption feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Action Action class itself.
 */
interface CaptionFeature<Action> :
    Feature,
    EntitiesCtxBuilder<Action>
    where Action : TgAction<*>, Action : CaptionFeature<Action> {
    @Suppress("UNCHECKED_CAST")
    private val thisAsReturn: Action
        get() = this as Action

    /**
     * DSL for adding captions
     *
     * @param block
     * @return [Action]
     */
    fun caption(block: EntitiesCtxBuilder<Action>.() -> String): Action = thisAsReturn.apply {
        parameters["caption"] = block(thisAsReturn).toJsonElement()
    }

    /**
     * Caption entities
     */
    fun captionEntities(entities: List<MessageEntity>): Action = thisAsReturn.apply {
        parameters["caption_entities"] = entities.encodeWith(MessageEntity.serializer())
    }

    /**
     * Caption entities DSL with [EntitiesBuilder]
     */
    fun captionEntities(block: EntitiesBuilder.() -> Unit): Action = captionEntities(EntitiesBuilder.build(block))
}
