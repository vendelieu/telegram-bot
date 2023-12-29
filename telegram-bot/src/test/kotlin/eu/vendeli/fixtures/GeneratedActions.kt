@file:Suppress(
    "NOTHING_TO_INLINE",
    "ObjectPropertyName",
    "UNUSED_ANONYMOUS_PARAMETER",
    "UnnecessaryVariable",
    "TopLevelPropertyNaming",
    "UNNECESSARY_SAFE_CALL",
    "RedundantNullableReturnType",
)

package eu.vendeli.fixtures

import eu.vendeli.tgbot.types.internal.InvocationMeta
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.UpdateType.CALLBACK_QUERY
import eu.vendeli.tgbot.types.internal.UpdateType.MESSAGE
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.Invocable
import eu.vendeli.tgbot.utils.InvocationLambda

private inline fun suspendCall(noinline block: InvocationLambda): InvocationLambda = block

private val zeroRateLimits: RateLimits = RateLimits(0, 0)

private val `TG_$COMMANDS`: Map<Pair<String, UpdateType>, Invocable> = mapOf(
    ("test" to MESSAGE) to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class.java) as
                eu.vendeli.fixtures.TgAnnotationsModel
            eu.vendeli.fixtures.TgAnnotationsModel::test.invoke(
                inst,
            )
        }
            to InvocationMeta("eu.vendeli.fixtures.TgAnnotationsModel", "test", zeroRateLimits)
    ),
    ("test" to CALLBACK_QUERY) to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class.java) as
                eu.vendeli.fixtures.TgAnnotationsModel
            eu.vendeli.fixtures.TgAnnotationsModel::test.invoke(
                inst,
            )
        }
            to InvocationMeta("eu.vendeli.fixtures.TgAnnotationsModel", "test", zeroRateLimits)
    ),
    ("test2" to MESSAGE) to (
        suspendCall { classManager, update, user, bot, parameters ->
            ::testMethod.invoke()
        }
            to InvocationMeta("eu.vendeli.fixtures", "testMethod", zeroRateLimits)
    ),
    ("test2" to CALLBACK_QUERY) to (
        suspendCall { classManager, update, user, bot, parameters ->
            ::testMethod.invoke()
        }
            to InvocationMeta("eu.vendeli.fixtures", "testMethod", zeroRateLimits)
    ),
    ("test3" to MESSAGE) to (
        suspendCall { classManager, update, user, bot, parameters ->
            eu.vendeli.fixtures.TestObj::test.invoke()
        }
            to InvocationMeta("eu.vendeli.fixtures.TestObj", "test", zeroRateLimits)
    ),
    ("test3" to CALLBACK_QUERY) to (
        suspendCall { classManager, update, user, bot, parameters ->
            eu.vendeli.fixtures.TestObj::test.invoke()
        }
            to InvocationMeta("eu.vendeli.fixtures.TestObj", "test", zeroRateLimits)
    ),
)

private val `TG_$INPUTS`: Map<String, Invocable> = mapOf(
    "testInp" to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class.java) as
                eu.vendeli.fixtures.TgAnnotationsModel
            eu.vendeli.fixtures.TgAnnotationsModel::test2.invoke(
                inst,
            )
        }
            to InvocationMeta("eu.vendeli.fixtures.TgAnnotationsModel", "test2", zeroRateLimits)
    ),
    "testInp2" to (
        suspendCall { classManager, update, user, bot, parameters ->
            ::testMethod2.invoke()
        }
            to InvocationMeta("eu.vendeli.fixtures", "testMethod2", zeroRateLimits)
    ),
    "testInp3" to (
        suspendCall { classManager, update, user, bot, parameters ->
            eu.vendeli.fixtures.TestObj::test2.invoke()
        }
            to InvocationMeta("eu.vendeli.fixtures.TestObj", "test2", zeroRateLimits)
    ),
    "eu.vendeli.fixtures.Conversation.Name" to (
        suspendCall {
                classManager,
                update,
                user,
                bot,
                parameters,
            ->
            if (user == null) return@suspendCall Unit
            val inst = classManager.getInstance(eu.vendeli.fixtures.Conversation.Name::class.java) as
                eu.vendeli.fixtures.Conversation.Name
            val nextLink: String? = """eu.vendeli.fixtures.Conversation.Age"""
            val breakPoint = inst.breakCondition?.invoke(user, update, bot) ?: false
            if (breakPoint && inst.retryAfterBreak) bot.inputListener[user] =
                "eu.vendeli.fixtures.Conversation.Name"
            if (breakPoint) {
                inst.breakAction(user, update, bot)
                return@suspendCall Unit
            }
            inst.action(user, update, bot).also {
                if (nextLink != null) bot.inputListener[user] = nextLink
            }
        }
            to InvocationMeta("eu.vendeli.fixtures.Conversation", "Name", zeroRateLimits)
    ),
    "eu.vendeli.fixtures.Conversation.Age" to (
        suspendCall {
                classManager,
                update,
                user,
                bot,
                parameters,
            ->
            if (user == null) return@suspendCall Unit
            val inst = classManager.getInstance(eu.vendeli.fixtures.Conversation.Age::class.java) as
                eu.vendeli.fixtures.Conversation.Age
            val nextLink: String? = null
            val breakPoint = inst.breakCondition?.invoke(user, update, bot) ?: false
            if (breakPoint && inst.retryAfterBreak) bot.inputListener[user] =
                "eu.vendeli.fixtures.Conversation.Age"
            if (breakPoint) {
                inst.breakAction(user, update, bot)
                return@suspendCall Unit
            }
            inst.action(user, update, bot).also {
                if (nextLink != null) bot.inputListener[user] = nextLink
            }
        }
            to InvocationMeta("eu.vendeli.fixtures.Conversation", "Age", zeroRateLimits)
    ),
)

private val `TG_$REGEX`: Map<Regex, Invocable> = mapOf(
    Regex("test colou?r") to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(eu.vendeli.fixtures.RegexCommands::class.java) as
                eu.vendeli.fixtures.RegexCommands
            val param0 = bot
            eu.vendeli.fixtures.RegexCommands::testR.invoke(
                inst, param0,
            )
        }
            to InvocationMeta("eu.vendeli.fixtures.RegexCommands", "testR", zeroRateLimits)
    ),
)

private val `TG_$UPDATE_TYPES`: Map<UpdateType, InvocationLambda> = mapOf(
    MESSAGE to suspendCall { classManager, update, user, bot, parameters ->
        val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class.java) as
            eu.vendeli.fixtures.TgAnnotationsModel
        eu.vendeli.fixtures.TgAnnotationsModel::updateHandler.invoke(
            inst,
        )
    },
    CALLBACK_QUERY to suspendCall { classManager, update, user, bot, parameters ->
        val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class.java) as
            eu.vendeli.fixtures.TgAnnotationsModel
        eu.vendeli.fixtures.TgAnnotationsModel::updateHandler.invoke(
            inst,
        )
    },
)

private val `TG_$UNPROCESSED`: InvocationLambda? = suspendCall {
        classManager,
        update,
        user,
        bot,
        parameters,
    ->
    val inst = classManager.getInstance(eu.vendeli.fixtures.TgAnnotationsModel::class.java) as
        eu.vendeli.fixtures.TgAnnotationsModel
    eu.vendeli.fixtures.TgAnnotationsModel::test3.invoke(
        inst,
    )
}

internal val `$ACTIONS_eu_vendeli_fixtures`: List<Any?> = listOf(
    `TG_$COMMANDS`,
    `TG_$INPUTS`,
    `TG_$REGEX`,
    `TG_$UPDATE_TYPES`,
    `TG_$UNPROCESSED`,
)
