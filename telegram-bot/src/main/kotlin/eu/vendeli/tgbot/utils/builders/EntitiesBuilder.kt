package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.EntityType
import eu.vendeli.tgbot.types.MessageEntity

/**
 * Entities builder which is used in EntitiesFeature
 */
class EntitiesBuilder {
    internal val listOfEntities = mutableListOf<MessageEntity>()

    /**
     * Add new entity
     *
     * @param type
     * @param offset
     * @param length
     * @param url
     * @param user
     * @param language
     */
    fun entity(
        type: EntityType,
        offset: Int,
        length: Int,
        block: EntityData.() -> Unit = {},
    ) {
        EntityData().apply(block).also {
            listOfEntities.add(MessageEntity(type, offset, length, it.url, it.user, it.language))
        }
    }
}
