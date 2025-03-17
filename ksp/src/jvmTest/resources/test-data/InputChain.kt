import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.InputChain
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chain.BaseStatefulLink
import eu.vendeli.tgbot.types.chain.BreakCondition
import eu.vendeli.tgbot.types.chain.ChainAction
import eu.vendeli.tgbot.types.chain.ChainLink
import eu.vendeli.tgbot.types.chain.ChainingStrategy
import eu.vendeli.tgbot.types.component.ProcessedUpdate

@InputChain(autoClean = true)
class Conversation {
    object Name : BaseStatefulLink() {
        override val breakCondition = BreakCondition { _, update, _ -> update.text.isEmpty() }

        override suspend fun action(user: User, update: ProcessedUpdate, bot: TelegramBot): String {
            message { "Oh, ${update.text}, nice to meet you!" }.send(user, bot)
            message { "How old are you?" }.send(user, bot)

            return update.text
        }

        override suspend fun breakAction(user: User, update: ProcessedUpdate, bot: TelegramBot) {
            message {
                "Please say your name, because that's what well-mannered people do :)"
            }.send(user, bot)
        }
    }

    object Age : ChainLink() {
        override val breakCondition = BreakCondition { _, update, _ -> update.text.toIntOrNull() == null }
        override val chainingStrategy = ChainingStrategy.LinkTo { Name }
        override val afterAction = ChainAction { user: User, update: ProcessedUpdate, bot: TelegramBot ->
            println(user.id)
        }

        override val beforeAction = ChainAction { user: User, update: ProcessedUpdate, bot: TelegramBot ->
            println(user.id)
        }

        override val retryAfterBreak = false

        override suspend fun action(user: User, update: ProcessedUpdate, bot: TelegramBot) {
            val name = Name.state.get(user)
            message {
                "I'm not good at remembering, but I remembered you! You're $name and you're ${update.text} years old."
            }.send(user, bot)
        }

        override suspend fun breakAction(user: User, update: ProcessedUpdate, bot: TelegramBot) {
            message {
                "Perhaps it's not nice to ask your age, but maybe you can tell me anyway."
            }.send(user, bot)
        }
    }
}
