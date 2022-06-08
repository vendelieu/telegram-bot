package com.github.vendelieu.tgbot.utils

import com.github.vendelieu.tgbot.types.EntityType
import com.github.vendelieu.tgbot.types.MessageEntity
import com.github.vendelieu.tgbot.types.User

class EntitiesBuilder {
    internal val listOfEntities = mutableListOf<MessageEntity>()

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
