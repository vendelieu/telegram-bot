package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.EntityType
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.types.User

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
        url: String? = null,
        user: User? = null,
        language: String? = null,
    ) {
        listOfEntities.add(MessageEntity(type, offset, length, url, user, language))
    }
}
