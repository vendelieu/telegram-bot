package eu.vendeli.fixtures

import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.types.internal.Action
import eu.vendeli.tgbot.types.internal.BreakCondition
import eu.vendeli.tgbot.types.internal.ChainLink
import eu.vendeli.tgbot.types.internal.InputChain

@OptIn(ExperimentalFeature::class)
object Conversation : InputChain(firstLink = Name) {
    object Name : ChainLink(
        action = Action { user, update, bot ->
            bot.userData[user, "name"] = update.text

            message { "Oh, ${update.text}, nice to meet you!" }
            message { "How old are you?" }.send(user, bot)
        },
        nextLink = Age,
        breakCondition = BreakCondition { _, update, _ -> update.text.isEmpty() },
        breakingAction = Action { user, _, bot ->
            message {
                "Please say your name, because that's what well-mannered people do :)"
            }.send(user, bot)
        },
    )

    object Age : ChainLink(
        action = Action { user, update, bot ->
            val name = bot.userData[user, "name"]
            message {
                "I'm not good at remembering, but I remembered you! You're $name and you're ${update.text} years old."
            }.send(user, bot)
        },
        breakCondition = BreakCondition { _, update, _ -> update.text.toIntOrNull() == null },
        breakingAction = Action { user, _, bot ->
            message {
                "Perhaps it's not nice to ask your age, but maybe you can tell me anyway."
            }.send(user, bot)
        },
    )
}
