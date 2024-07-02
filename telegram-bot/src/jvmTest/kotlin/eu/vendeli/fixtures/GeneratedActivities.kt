@file:Suppress(
    "NOTHING_TO_INLINE",
    "ObjectPropertyName",
    "UNUSED_ANONYMOUS_PARAMETER",
    "UnnecessaryVariable",
    "TopLevelPropertyNaming",
    "UNNECESSARY_SAFE_CALL",
    "RedundantNullableReturnType",
    "KotlinConstantConditions",
    "USELESS_ELVIS",
    "ktlint:standard:backing-property-naming",
)

package eu.vendeli.fixtures

import eu.vendeli.tgbot.types.internal.CommonMatcher
import eu.vendeli.tgbot.types.internal.InvocationMeta
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.UpdateType.CALLBACK_QUERY
import eu.vendeli.tgbot.types.internal.UpdateType.MESSAGE
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.Invocable
import eu.vendeli.tgbot.utils.InvocationLambda

private inline fun suspendCall(noinline block: InvocationLambda): InvocationLambda = block

private val zeroRateLimits: RateLimits = RateLimits(0, 0)

private val __TG_COMMANDS0: Map<Pair<String, UpdateType>, Invocable> = mapOf(
    ("test2" to MESSAGE) to (
        suspendCall { classManager, update, user, bot, parameters ->
            ::testMethod.invoke()
        }
            to InvocationMeta(
                "eu.vendeli.fixtures",
                "testMethod",
                zeroRateLimits,
                eu.vendeli.tgbot.utils.DefaultGuard::class,
            )
    ),
    ("test" to MESSAGE) to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(TgAnnotationsModel::class) as
                TgAnnotationsModel
            val param0 = bot
            TgAnnotationsModel::test.invoke(
                inst,
                param0,
            )
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.TgAnnotationsModel",
                "test",
                zeroRateLimits,
                eu.vendeli.tgbot.utils.DefaultGuard::class,
            )
    ),
    ("STOP" to MESSAGE) to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(TgAnnotationsModel::class) as
                TgAnnotationsModel
            val param0 = bot
            TgAnnotationsModel::stopHandling.invoke(
                inst,
                param0,
            )
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.TgAnnotationsModel",
                "stopHandling",
                zeroRateLimits,
                eu.vendeli.tgbot.utils.DefaultGuard::class,
            )
    ),
    ("test3" to MESSAGE) to (
        suspendCall { classManager, update, user, bot, parameters ->
            TestObj::test.invoke()
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.TestObj",
                "test",
                zeroRateLimits,
                eu.vendeli.tgbot.utils.DefaultGuard::class,
            )
    ),
)

private val __TG_INPUTS0: Map<String, Invocable> = mapOf(
    "testInp2" to (
        suspendCall { classManager, update, user, bot, parameters ->
            ::testMethod2.invoke()
        }
            to InvocationMeta(
                "eu.vendeli.fixtures",
                "testMethod2",
                zeroRateLimits,
                eu.vendeli.tgbot.utils.DefaultGuard::class,
            )
    ),
    "testInp" to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(TgAnnotationsModel::class) as
                TgAnnotationsModel
            TgAnnotationsModel::test2.invoke(
                inst,
            )
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.TgAnnotationsModel",
                "test2",
                zeroRateLimits,
                eu.vendeli.tgbot.utils.DefaultGuard::class,
            )
    ),
    "testInp3" to (
        suspendCall { classManager, update, user, bot, parameters ->
            TestObj::test2.invoke()
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.TestObj",
                "test2",
                zeroRateLimits,
                eu.vendeli.tgbot.utils.DefaultGuard::class,
            )
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
            val inst = classManager.getInstance(Conversation.Name::class) as
                Conversation.Name
            inst.beforeAction?.invoke(user, update, bot)
            val nextLink: String? = """eu.vendeli.fixtures.Conversation.Age"""
            val breakPoint = Conversation.Name.breakCondition?.invoke(user, update, bot) ?: false
            if (breakPoint && inst.retryAfterBreak) bot.inputListener[user] =
                "eu.vendeli.fixtures.Conversation.Name"
            if (breakPoint) {
                Conversation.Name.breakAction(user, update, bot)
                return@suspendCall Unit
            }
            Conversation.Name.action(user, update, bot).also {
                if (nextLink != null) bot.inputListener[user] = nextLink
            }
            inst.afterAction?.invoke(user, update, bot)
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.Conversation",
                "Name",
                zeroRateLimits,
                eu.vendeli.tgbot.utils.DefaultGuard::class,
            )
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
            val inst = classManager.getInstance(Conversation.Age::class) as
                Conversation.Age
            inst.beforeAction?.invoke(user, update, bot)
            val nextLink: String? = null
            val breakPoint = Conversation.Age.breakCondition?.invoke(user, update, bot) ?: false
            if (breakPoint && Conversation.Age.retryAfterBreak) bot.inputListener[user] =
                "eu.vendeli.fixtures.Conversation.Age"
            if (breakPoint) {
                Conversation.Age.breakAction(user, update, bot)
                return@suspendCall Unit
            }
            Conversation.Age.action(user, update, bot).also {
                if (nextLink != null) bot.inputListener[user] = nextLink
            }
            inst.afterAction?.invoke(user, update, bot)
        }
            to InvocationMeta(
                "eu.vendeli.fixtures.Conversation",
                "Age",
                zeroRateLimits,
                eu.vendeli.tgbot.utils.DefaultGuard::class,
            )
    ),
)

private val __TG_COMMONS0: Map<CommonMatcher, Invocable> = mapOf(
    CommonMatcher.String(
        value = "common",
        filter = eu.vendeli.tgbot.utils.DefaultFilter::class,
        setOf(MESSAGE),
    ) to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(TgAnnotationsModel::class) as
                TgAnnotationsModel
            TgAnnotationsModel::common.invoke(
                inst,
            )
        }
            to InvocationMeta("eu.vendeli.fixtures.TgAnnotationsModel", "common", zeroRateLimits)
    ),
    CommonMatcher.String(
        value = "common2",
        filter = eu.vendeli.tgbot.utils.DefaultFilter::class,
        setOf(MESSAGE),
    ) to (
        suspendCall { classManager, update, user, bot, parameters ->
            val inst = classManager.getInstance(TgAnnotationsModel::class) as
                TgAnnotationsModel
            TgAnnotationsModel::common.invoke(
                inst,
            )
        }
            to InvocationMeta("eu.vendeli.fixtures.TgAnnotationsModel", "common", zeroRateLimits)
    ),
    CommonMatcher.Regex(
        value = Regex("test colou?r"),
        filter =
            eu.vendeli.tgbot.utils.DefaultFilter::class,
        setOf(MESSAGE),
    ) to (
        suspendCall {
                classManager,
                update,
                user,
                bot,
                parameters,
            ->
            val inst = classManager.getInstance(RegexCommands::class) as
                RegexCommands
            val param0 = bot
            RegexCommands::testR.invoke(
                inst,
                param0,
            )
        }
            to InvocationMeta("eu.vendeli.fixtures.RegexCommands", "testR", zeroRateLimits)
    ),
)

private val __TG_UPDATE_TYPES0: Map<UpdateType, InvocationLambda> = mapOf(
    MESSAGE to suspendCall { classManager, update, user, bot, parameters ->
        val inst = classManager.getInstance(TgAnnotationsModel::class) as
            TgAnnotationsModel
        TgAnnotationsModel::updateHandler.invoke(
            inst,
        )
    },
    CALLBACK_QUERY to suspendCall { classManager, update, user, bot, parameters ->
        val inst = classManager.getInstance(TgAnnotationsModel::class) as
            TgAnnotationsModel
        TgAnnotationsModel::updateHandler.invoke(
            inst,
        )
    },
)

private val __TG_UNPROCESSED0: InvocationLambda? = suspendCall {
        classManager,
        update,
        user,
        bot,
        parameters,
    ->
    val inst = classManager.getInstance(TgAnnotationsModel::class) as
        TgAnnotationsModel
    TgAnnotationsModel::test3.invoke(
        inst,
    )
}

internal val __ACTIVITIES: Map<String, List<Any?>> = mapOf(
    "default" to listOf(
        __TG_COMMANDS0,
        __TG_INPUTS0,
        __TG_COMMONS0,
        __TG_UPDATE_TYPES0,
        __TG_UNPROCESSED0,
    ),
)
