package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.EntityType
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.types.User

/**
 * Entities builder which is used in EntitiesFeature
 */
class EntitiesBuilder {
    private val entities = mutableListOf<MessageEntity>()

    /**
     * Entity optional parameters
     *
     * @property url    String 	Optional. For “text_link” only, URL that will be opened after user taps on the text
     * @property user    User 	Optional. For “text_mention” only, the mentioned user
     * @property language    String 	Optional. For “pre” only, the programming language of the entity text
     */
    data class EntityData internal constructor(
        var url: String? = null,
        var user: User? = null,
        var language: String? = null,
    )

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
            entities += MessageEntity(type, offset, length, it.url, it.user, it.language)
        }
    }

    /**
     * Declare [EntityType.TextLink] entity with given url.
     *
     * @param url
     */
    infix fun Pair<Int, Int>.url(url: String) {
        entities += MessageEntity(EntityType.TextLink, first, second, url)
    }

    /**
     * Declare [EntityType.TextMention] entity with given [User].
     *
     * @param user
     */
    infix fun Pair<Int, Int>.user(user: User) {
        entities += MessageEntity(EntityType.TextMention, first, second, user = user)
    }

    /**
     * Declare [EntityType.Pre] entity with given language.
     *
     * @param language
     */
    infix fun Pair<Int, Int>.language(language: String) {
        entities += MessageEntity(EntityType.Pre, first, second, language = language)
    }

    /**
     * Declare [EntityType.CustomEmoji] entity with given customEmojiId.
     *
     * @param customEmojiId
     */
    infix fun Pair<Int, Int>.customEmoji(customEmojiId: String) {
        entities += MessageEntity(EntityType.CustomEmoji, first, second, customEmojiId = customEmojiId)
    }

    internal companion object {
        fun build(block: EntitiesBuilder.() -> Unit): List<MessageEntity> = EntitiesBuilder().apply(block).entities
    }
}
