@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.bot.BotCommand
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.utils.builders.BotCommandsBuilder
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SetMyCommandsAction(
    languageCode: String? = null,
    scope: BotCommandScope? = null,
    commands: List<BotCommand>,
) : SimpleAction<Boolean>() {
    @TgAPI.Method("setMyCommands")
    override val method = "setMyCommands"
    override val returnType = getReturnType()

    init {
        parameters["commands"] = commands.encodeWith(BotCommand.serializer())
        if (scope != null) parameters["scope"] = scope.encodeWith(BotCommandScope.serializer())
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

/**
 * Use this method to change the list of the bot's commands. See this manual for more details about bot commands. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setmycommands)
 * @param commands A JSON-serialized list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified.
 * @param scope A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to BotCommandScopeDefault.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setMyCommands(languageCode: String? = null, scope: BotCommandScope? = null, command: List<BotCommand>) =
    SetMyCommandsAction(languageCode, scope, command)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setMyCommands(languageCode: String? = null, scope: BotCommandScope? = null, vararg command: BotCommand) =
    setMyCommands(languageCode, scope, command.asList())

@TgAPI
fun setMyCommands(languageCode: String? = null, scope: BotCommandScope? = null, block: BotCommandsBuilder.() -> Unit) =
    setMyCommands(languageCode, scope, BotCommandsBuilder.build(block))
