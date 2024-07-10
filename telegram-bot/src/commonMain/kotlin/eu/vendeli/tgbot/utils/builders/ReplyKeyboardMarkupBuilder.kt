package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.interfaces.KeyboardBuilder
import eu.vendeli.tgbot.types.internal.options.ReplyKeyboardMarkupOptions
import eu.vendeli.tgbot.types.keyboard.KeyboardButton
import eu.vendeli.tgbot.types.keyboard.KeyboardButtonPollType
import eu.vendeli.tgbot.types.keyboard.KeyboardButtonRequestChat
import eu.vendeli.tgbot.types.keyboard.KeyboardButtonRequestUsers
import eu.vendeli.tgbot.types.keyboard.ReplyKeyboardMarkup
import eu.vendeli.tgbot.types.keyboard.WebAppInfo

/**
 * Builder which is used to assemble the reply keyboard markup buttons.
 */
class ReplyKeyboardMarkupBuilder : KeyboardBuilder<KeyboardButton>() {
    override val buttons: MutableList<KeyboardButton?> = mutableListOf()
    internal val options = ReplyKeyboardMarkupOptions()

    /**
     * Use this function to add buttons without parameters, e.g. +"Button name".
     */
    operator fun String.unaryPlus() {
        buttons += KeyboardButton(this)
    }

    /**
     * Configure optional markup parameters. See [ReplyKeyboardMarkupOptions].
     */
    fun options(block: ReplyKeyboardMarkupOptions.() -> Unit) = options.block()

    /**
     * Button for requesting user.
     *
     * If specified, pressing the button will open a list of suitable users.
     * Tapping on any user will send their identifier to the bot in a “user_shared” service message.
     * Available in private chats only.
     */
    infix fun String.requestUser(value: KeyboardButtonRequestUsers) {
        buttons += KeyboardButton(this, requestUsers = value)
    }

    /**
     * Button for requesting chat.
     *
     * If specified, pressing the button will open a list of suitable users.
     * Tapping on any user will send their identifier to the bot in a “user_shared” service message.
     * Available in private chats only.
     */
    infix fun String.requestChat(value: KeyboardButtonRequestChat) {
        buttons += KeyboardButton(this, requestChat = value)
    }

    /**
     * Button for requesting contact from user.
     *
     * If True, the user's phone number will be sent as a contact when the button is pressed.
     * Available in private chats only.
     */
    infix fun String.requestContact(value: Boolean) {
        buttons += KeyboardButton(this, requestContact = value)
    }

    /**
     * Button for requesting location from user.
     *
     * If True, the user's current location will be sent when the button is pressed.
     * Available in private chats only.
     */
    infix fun String.requestLocation(value: Boolean) {
        buttons += KeyboardButton(this, requestLocation = value)
    }

    /**
     * Button for requesting sending new poll from user.
     *
     * If specified, the user will be asked to create a poll and send it to the bot when the button is pressed.
     * Available in private chats only.
     */
    infix fun String.requestPoll(value: KeyboardButtonPollType) {
        buttons += KeyboardButton(this, requestPoll = value)
    }

    /**
     * Button for opening webapp for user.
     *
     * If specified, the described Web App will be launched when the button is pressed.
     * The Web App will be able to send a “web_app_data” service message. Available in private chats only.
     */
    infix fun String.webApp(url: String) {
        buttons += KeyboardButton(this, webApp = WebAppInfo(url))
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
