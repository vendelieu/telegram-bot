package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.IActionState
import eu.vendeli.tgbot.interfaces.Keyboard
import eu.vendeli.tgbot.types.keyboard.ForceReply
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.types.keyboard.ReplyKeyboardMarkup
import eu.vendeli.tgbot.utils.builders.InlineKeyboardMarkupBuilder
import eu.vendeli.tgbot.utils.builders.ReplyKeyboardMarkupBuilder

/**
 * Markup feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Return Action itself.
 */
interface MarkupFeature<Return> : IActionState, Feature {
    @Suppress("UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    /**
     * Add Markup directly
     *
     * @param keyboard
     * @return Action itself.
     */
    fun markup(keyboard: Keyboard): Return {
        parameters["reply_markup"] = keyboard
        return thisAsReturn
    }

    /**
     * Add Markup via lambda
     *
     * @param block
     * @return action itself.
     */
    fun markup(block: () -> Keyboard): Return = markup(block())

    /**
     * Add InlineKeyboard markup to the Action<[Return]>.
     *
     * @param block Builder [InlineKeyboardMarkupBuilder] context.
     * @return Action[Return] itself.
     */
    fun inlineKeyboardMarkup(block: InlineKeyboardMarkupBuilder.() -> Unit): Return =
        markup(InlineKeyboardMarkup(InlineKeyboardMarkupBuilder().apply(block).build()))

    /**
     * Add ReplyKeyboard markup to the Action<[Return]>.
     *
     * @param block Builder [ReplyKeyboardMarkupBuilder] context.
     * @return Action[Return] itself.
     */
    fun replyKeyboardMarkup(block: ReplyKeyboardMarkupBuilder.() -> Unit): Return =
        markup(ReplyKeyboardMarkup(ReplyKeyboardMarkupBuilder().apply(block).build()))

    /**
     * Add ForceReply markup to the Action<[Return]>.
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
}
