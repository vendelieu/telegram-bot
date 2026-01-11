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
@file:OptIn(KtGramInternal::class)

package eu.vendeli.fixtures

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.core.Activity
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.interfaces.helper.ContextLoader
import eu.vendeli.tgbot.types.component.CommonMatcher
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.configuration.RateLimits
import kotlin.reflect.KClass
import eu.vendeli.tgbot.types.component.userOrNull

class TestActivity(
    override val id: Int,
    override val qualifier: String,
    override val function: String,
    override val rateLimits: RateLimits = RateLimits.NOT_LIMITED,
    private val block: suspend (ProcessingContext) -> Any?,
) : Activity {
    override val guardClass: KClass<out Guard> = DefaultGuard::class
    override val argParser: KClass<out ArgumentParser> = DefaultArgParser::class

    override suspend fun invoke(context: ProcessingContext): Any? = block(context)
}

class TestActivitiesLoader : ContextLoader {
    override fun load(bot: TelegramBot): Unit = bot.update.registry.run {
        bot.update.____ctxUtils = __CtxUtils

        // Commands
        val testMethodActivity = TestActivity(1, "eu.vendeli.fixtures", "testMethod") { testMethod() }
        registerActivity(testMethodActivity)
        registerCommand("test2", UpdateType.MESSAGE, testMethodActivity.id)

        val tgAnnotationsModelTestActivity =
            TestActivity(2, "eu.vendeli.fixtures.TgAnnotationsModel", "test") { context ->
                val inst = context.bot.config.classManager
                    .getInstance(TgAnnotationsModel::class) as TgAnnotationsModel
                inst.test(context.bot)
            }
        registerActivity(tgAnnotationsModelTestActivity)
        registerCommand("test", UpdateType.MESSAGE, tgAnnotationsModelTestActivity.id)

        val stopHandlingActivity =
            TestActivity(3, "eu.vendeli.fixtures.TgAnnotationsModel", "stopHandling") { context ->
                val inst = context.bot.config.classManager
                    .getInstance(TgAnnotationsModel::class) as TgAnnotationsModel
                inst.stopHandling(context.bot)
            }
        registerActivity(stopHandlingActivity)
        registerCommand("STOP", UpdateType.MESSAGE, stopHandlingActivity.id)

        val testObjTestActivity = TestActivity(4, "eu.vendeli.fixtures.TestObj", "test") { TestObj.test() }
        registerActivity(testObjTestActivity)
        registerCommand("test3", UpdateType.MESSAGE, testObjTestActivity.id)

        // Inputs
        val testMethod2Activity = TestActivity(5, "eu.vendeli.fixtures", "testMethod2") { testMethod2() }
        registerActivity(testMethod2Activity)
        registerInput("testInp2", testMethod2Activity.id)

        val tgAnnotationsModelTest2Activity =
            TestActivity(6, "eu.vendeli.fixtures.TgAnnotationsModel", "test2") { context ->
                val inst = context.bot.config.classManager
                    .getInstance(TgAnnotationsModel::class) as TgAnnotationsModel
                inst.test2()
            }
        registerActivity(tgAnnotationsModelTest2Activity)
        registerInput("testInp", tgAnnotationsModelTest2Activity.id)

        val testObjTest2Activity = TestActivity(7, "eu.vendeli.fixtures.TestObj", "test2") { TestObj.test2() }
        registerActivity(testObjTest2Activity)
        registerInput("testInp3", testObjTest2Activity.id)

        // Commons
        val commonActivity = TestActivity(10, "eu.vendeli.fixtures.TgAnnotationsModel", "common") { context ->
            val inst = context.bot.config.classManager
                .getInstance(TgAnnotationsModel::class) as TgAnnotationsModel
            inst.common()
        }
        registerActivity(commonActivity)
        registerMatcher(CommonMatcher.String("common"), UpdateType.MESSAGE, commonActivity.id)
        registerMatcher(CommonMatcher.String("common2"), UpdateType.MESSAGE, commonActivity.id)

        val testColorActivity = TestActivity(11, "eu.vendeli.fixtures.RegexCommands", "testR") { context ->
            val inst = context.bot.config.classManager
                .getInstance(RegexCommands::class) as RegexCommands
            inst.testR(context.bot)
        }
        registerActivity(testColorActivity)
        registerMatcher(CommonMatcher.Regex(Regex("test colou?r")), UpdateType.MESSAGE, testColorActivity.id)

        // Update handlers
        val updateHandlerActivity =
            TestActivity(12, "eu.vendeli.fixtures.TgAnnotationsModel", "updateHandler") { context ->
                val inst = context.bot.config.classManager
                    .getInstance(TgAnnotationsModel::class) as TgAnnotationsModel
                inst.updateHandler()
            }
        registerActivity(updateHandlerActivity)
        registerUpdateTypeHandler(UpdateType.MESSAGE, updateHandlerActivity.id)
        registerUpdateTypeHandler(UpdateType.CALLBACK_QUERY, updateHandlerActivity.id)

        // Unprocessed
        val unprocessedActivity = TestActivity(13, "eu.vendeli.fixtures.TgAnnotationsModel", "test3") { context ->
            val inst = context.bot.config.classManager
                .getInstance(TgAnnotationsModel::class) as TgAnnotationsModel
            inst.test3()
        }
        registerActivity(unprocessedActivity)
        registerUnprocessed(unprocessedActivity.id)
    }
}
