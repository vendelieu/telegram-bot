package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.Keyboard
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.keyboard.ForceReply
import eu.vendeli.tgbot.types.keyboard.ReplyKeyboardRemove
import eu.vendeli.tgbot.utils.builders.InlineKeyboardMarkupBuilder
import eu.vendeli.tgbot.utils.builders.ReplyKeyboardMarkupBuilder
import eu.vendeli.tgbot.utils.encodeWith

/**
 * Markup feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Return Action itself.
 */
interface MarkupFeature<Return : TgAction<*>> : Feature {
    /**
     * Add Markup directly.
     *
     * @param keyboard
     * @return Action itself.
     */
    @Suppress("UNCHECKED_CAST")
    fun markup(keyboard: Keyboard): Return = (this as Return).apply {
        parameters["reply_markup"] = keyboard.encodeWith(Keyboard.Companion)
    }

    /**
     * Add Markup via lambda.
     *
     * @param block
     * @return action itself.
     */
    fun markup(block: () -> Keyboard): Return = markup(block())

    /**
     * Add InlineKeyboard markup to the [TgAction]<[Return]>.
     *
     * @param block Builder [InlineKeyboardMarkupBuilder] context.
     * @return Action[Return] itself.
     */
    fun inlineKeyboardMarkup(block: InlineKeyboardMarkupBuilder.() -> Unit): Return =
        markup(eu.vendeli.tgbot.utils.builders.inlineKeyboardMarkup(block))

    /**
     * Add ReplyKeyboard markup to the [TgAction]<[Return]>.
     *
     * @param block Builder [ReplyKeyboardMarkupBuilder] context.
     * @return Action[Return] itself.
     */
    fun replyKeyboardMarkup(block: ReplyKeyboardMarkupBuilder.() -> Unit): Return =
        markup(eu.vendeli.tgbot.utils.builders.replyKeyboardMarkup(block))

    /**
     * Add ForceReply markup to the [TgAction]<[Return]>.
     *
     * @param inputFieldPlaceholder The placeholder to be shown in the input field when the reply is active;
     * 1-64 characters
     * @param selective Use this parameter if you want to force reply from specific users only.
     * Targets: 1) users that are @mentioned in the text of the Message object;
     * 2) if the bot's message is a reply (has reply_to_message_id), sender of the original message.
     *
     * @return Action[Return] itself.
     */
    fun forceReply(
        inputFieldPlaceholder: String? = null,
        selective: Boolean? = null,
    ): Return = markup(ForceReply(inputFieldPlaceholder, selective))

    /**
     * Add [ReplyKeyboardRemove] markup to the [Action]<[Return]>
     *
     */
    fun replyKeyboardRemove(selective: Boolean? = null) = markup(ReplyKeyboardRemove(selective))
}
