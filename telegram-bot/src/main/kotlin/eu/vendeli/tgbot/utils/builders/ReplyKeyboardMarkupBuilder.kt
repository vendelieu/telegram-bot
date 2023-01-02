package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.interfaces.KeyboardBuilder
import eu.vendeli.tgbot.types.KeyboardButton
import eu.vendeli.tgbot.types.KeyboardButtonPollType
import eu.vendeli.tgbot.types.ReplyKeyboardMarkup
import eu.vendeli.tgbot.types.WebAppInfo
import eu.vendeli.tgbot.types.internal.options.ReplyKeyboardMarkupOptions

/**
 * Builder which is used to assemble the reply keyboard markup buttons.
 */
class ReplyKeyboardMarkupBuilder : KeyboardBuilder<KeyboardButton>() {
    override val buttons: MutableList<KeyboardButton?> = mutableListOf()
    internal val options = ReplyKeyboardMarkupOptions()

    /**
     * Configure optional markup parameters. See [ReplyKeyboardMarkupOptions].
     */
    fun options(block: ReplyKeyboardMarkupOptions.() -> Unit) = options.block()

    /**
     * Button for requesting contact from user.
     *
     * If True, the user's phone number will be sent as a contact when the button is pressed.
     * Available in private chats only.
     */
    infix fun String.requestContact(value: Boolean) {
        buttons.add(KeyboardButton(this, requestContact = value))
    }

    /**
     * Button for requesting location from user.
     *
     * If True, the user's current location will be sent when the button is pressed.
     * Available in private chats only.
     */
    infix fun String.requestLocation(value: Boolean) {
        buttons.add(KeyboardButton(this, requestLocation = value))
    }

    /**
     * Button for requesting sending new poll from user.
     *
     * If specified, the user will be asked to create a poll and send it to the bot when the button is pressed.
     * Available in private chats only.
     */
    infix fun String.requestPoll(value: KeyboardButtonPollType) {
        buttons.add(KeyboardButton(this, requestPoll = value))
    }

    /**
     * Button for opening webapp for user.
     *
     * If specified, the described Web App will be launched when the button is pressed.
     * The Web App will be able to send a “web_app_data” service message. Available in private chats only.
     */
    infix fun String.webApp(value: WebAppInfo) {
        buttons.add(KeyboardButton(this, webApp = value))
    }
}

/**
 * User-friendly DSL for creating [ReplyKeyboardMarkup]. See [ReplyKeyboardMarkupBuilder].
 */
fun replyKeyboardMarkup(block: ReplyKeyboardMarkupBuilder.() -> Unit): ReplyKeyboardMarkup {
    val builder = ReplyKeyboardMarkupBuilder().apply(block)
    return builder.options.run {
        ReplyKeyboardMarkup(
            builder.build(),
            resizeKeyboard,
            oneTimeKeyboard,
            inputFieldPlaceholder,
            selective,
            isPersistent,
        )
    }
}
