package eu.vendeli.tgbot.interfaces

/**
 * Media action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */
abstract class MediaAction<ReturnType> : Action<ReturnType>() {
    override val entitiesFieldName: String = "caption_entities"
}
