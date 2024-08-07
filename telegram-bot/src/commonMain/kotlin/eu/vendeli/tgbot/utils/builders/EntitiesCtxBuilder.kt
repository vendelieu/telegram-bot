package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.EntityType
import eu.vendeli.tgbot.types.EntityType.Blockquote
import eu.vendeli.tgbot.types.EntityType.Bold
import eu.vendeli.tgbot.types.EntityType.BotCommand
import eu.vendeli.tgbot.types.EntityType.Cashtag
import eu.vendeli.tgbot.types.EntityType.Code
import eu.vendeli.tgbot.types.EntityType.CustomEmoji
import eu.vendeli.tgbot.types.EntityType.Email
import eu.vendeli.tgbot.types.EntityType.ExpandableBlockQuote
import eu.vendeli.tgbot.types.EntityType.Hashtag
import eu.vendeli.tgbot.types.EntityType.Italic
import eu.vendeli.tgbot.types.EntityType.Mention
import eu.vendeli.tgbot.types.EntityType.PhoneNumber
import eu.vendeli.tgbot.types.EntityType.Pre
import eu.vendeli.tgbot.types.EntityType.Spoiler
import eu.vendeli.tgbot.types.EntityType.Strikethrough
import eu.vendeli.tgbot.types.EntityType.TextLink
import eu.vendeli.tgbot.types.EntityType.TextMention
import eu.vendeli.tgbot.types.EntityType.Underline
import eu.vendeli.tgbot.types.EntityType.Url
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.encodeWith
import kotlinx.serialization.json.JsonArray

@Suppress("TooManyFunctions")
interface EntitiesExtBuilder {
    fun EntitiesExtBuilder.addEntity(entity: MessageEntity)

    operator fun String.minus(other: String): String = this + other

    // operators
    operator fun String.minus(other: Pair<EntityType, String>): String {
        addEntity(MessageEntity(other.first, length, other.second.length))
        return this + other.second
    }

    operator fun Pair<EntityType, String>.minus(other: String): String {
        addEntity(MessageEntity(first, 0, second.length))
        return second + other
    }

    operator fun <T> String.minus(other: Triple<EntityType, String, T?>): String {
        val entity = when (other.first) {
            Pre -> MessageEntity(
                Pre,
                length,
                other.second.length,
                language = other.third?.toString(),
            )

            TextLink -> MessageEntity(
                TextLink,
                length,
                other.second.length,
                url = other.third?.toString(),
            )

            CustomEmoji -> MessageEntity(
                CustomEmoji,
                length,
                other.second.length,
                customEmojiId = other.third?.toString(),
            )

            TextMention -> MessageEntity(
                TextMention,
                length,
                other.second.length,
                user = other.third as? User,
            )

            else -> throw IllegalArgumentException("An unexpected EntityType - ${other.first}.")
        }
        addEntity(entity)
        return this + other.second
    }

    operator fun <T> Triple<EntityType, String, T?>.minus(other: String): String {
        val entity = when (first) {
            Pre -> MessageEntity(Pre, 0, second.length, language = third?.toString())
            TextLink -> MessageEntity(TextLink, 0, second.length, url = third?.toString())
            CustomEmoji -> MessageEntity(CustomEmoji, 0, second.length, customEmojiId = third?.toString())
            TextMention -> MessageEntity(TextMention, 0, second.length, user = third as? User)
            else -> throw IllegalArgumentException("An unexpected EntityType - $first.")
        }
        addEntity(entity)
        return this.second + other
    }

    // functions
    fun EntitiesExtBuilder.mention(block: () -> String) = Mention to block()
    fun EntitiesExtBuilder.hashtag(block: () -> String) = Hashtag to block()
    fun EntitiesExtBuilder.cashtag(block: () -> String) = Cashtag to block()
    fun EntitiesExtBuilder.botCommand(block: () -> String) = BotCommand to block()
    fun EntitiesExtBuilder.url(block: () -> String) = Url to block()
    fun EntitiesExtBuilder.email(block: () -> String) = Email to block()
    fun EntitiesExtBuilder.phoneNumber(block: () -> String) = PhoneNumber to block()
    fun EntitiesExtBuilder.bold(block: () -> String) = Bold to block()
    fun EntitiesExtBuilder.italic(block: () -> String) = Italic to block()
    fun EntitiesExtBuilder.underline(block: () -> String) = Underline to block()
    fun EntitiesExtBuilder.strikethrough(block: () -> String) = Strikethrough to block()
    fun EntitiesExtBuilder.spoiler(block: () -> String) = Spoiler to block()
    fun EntitiesExtBuilder.blockquote(block: () -> String) = Blockquote to block()
    fun EntitiesExtBuilder.expandableBlockquote(block: () -> String) = ExpandableBlockQuote to block()
    fun EntitiesExtBuilder.code(block: () -> String) = Code to block()

    fun EntitiesExtBuilder.customEmoji(
        customEmojiId: String? = null,
        block: () -> String,
    ) = Triple(CustomEmoji, block(), customEmojiId)

    fun EntitiesExtBuilder.pre(
        language: String? = null,
        block: () -> String,
    ) = Triple(Pre, block(), language)

    fun EntitiesExtBuilder.textLink(
        url: String? = null,
        block: () -> String,
    ) = Triple(TextLink, block(), url)

    fun EntitiesExtBuilder.textMention(
        user: User? = null,
        block: () -> String,
    ) = Triple(TextMention, block(), user)
}

interface EntitiesCtxBuilder<Action : TgAction<*>> : EntitiesExtBuilder {
    @Suppress("unused")
    override fun EntitiesExtBuilder.addEntity(entity: MessageEntity) {
        this as TgAction<*>
        val oldEntity = (parameters[entitiesFieldName] as? JsonArray)?.toMutableList() ?: mutableListOf()

        parameters[entitiesFieldName] = oldEntity
            .also {
                it.add(entity.encodeWith(MessageEntity.serializer()))
            }.let { JsonArray(it) }
    }
}
