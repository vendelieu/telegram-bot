package eu.vendeli

import BotTestContext
import eu.vendeli.fixtures.classData
import eu.vendeli.fixtures.get
import eu.vendeli.fixtures.userData
import eu.vendeli.tgbot.types.User
import eu.vendeli.utils.MockUpdate
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class ContextTest : BotTestContext() {
    private val user = User(TG_ID, false, "")

    @Test
    suspend fun `user data test`() {
        val empty = bot.userData[TG_ID, "test"]
        empty.shouldBeNull()

        bot.userData[TG_ID, "test"] = "value"
        val set = TG_ID.asUser()["test"]
        set.shouldNotBeNull()
        set shouldBe "value"

        val suspendGet = bot.userData.getAsync(TG_ID, "test2").await()
        suspendGet.shouldBeNull()

        bot.userData.setAsync(TG_ID, "test2", "value2").await()
        val setSuspendGet = bot.userData.getAsync(TG_ID, "test2").await()
        setSuspendGet.shouldNotBeNull()
        setSuspendGet shouldBe "value2"

        bot.userData[TG_ID, "test0"] = "value0"
        bot.userData[TG_ID, "test0"].shouldNotBeNull()
        bot.userData.del(TG_ID, "test0")
        bot.userData[TG_ID, "test0"].shouldBeNull()

        bot.userData[TG_ID, "test01"] = "value01"
        bot.userData[TG_ID, "test01"].shouldNotBeNull()
        bot.userData.delAsync(TG_ID, "test01").await()
        bot.userData[TG_ID, "test01"].shouldBeNull()

        bot.userData[user, "test02"] = "value02"
        bot.userData[user, "test02"] shouldBe "value02"
    }

    @Test
    suspend fun `class data test`() {
        val empty = bot.classData[TG_ID, "test"]
        empty.shouldBeNull()

        bot.classData[TG_ID, "test"] = "value"
        val set = bot.classData[TG_ID, "test"]
        set.shouldNotBeNull()
        set shouldBe "value"

        val suspendGet = bot.classData.getAsync(TG_ID, "test2").await()
        suspendGet.shouldBeNull()

        bot.classData.setAsync(TG_ID, "test2", "value2").await()
        val setSuspendGet = bot.classData.getAsync(TG_ID, "test2").await()
        setSuspendGet.shouldNotBeNull()
        setSuspendGet shouldBe "value2"

        bot.classData[TG_ID, "test0"] = "value0"
        bot.classData[TG_ID, "test0"].shouldNotBeNull()
        bot.classData.del(TG_ID, "test0")
        bot.classData[TG_ID, "test0"].shouldBeNull()

        bot.classData[TG_ID, "test01"] = "value01"
        bot.classData[TG_ID, "test01"].shouldNotBeNull()
        bot.classData.delAsync(TG_ID, "test01").await()
        bot.classData[TG_ID, "test01"].shouldBeNull()

        bot.classData.clearAll(TG_ID)
        bot.classData[TG_ID, "test"].shouldBeNull()
        bot.classData[TG_ID, "test2"].shouldBeNull()

        bot.classData[TG_ID, "test3"] = "value"
        bot.classData[TG_ID, "test3"].shouldNotBeNull()
        bot.classData.clearAll(TG_ID)
        bot.classData[TG_ID, "test3"].shouldBeNull()

        bot.userData[user, "test02"] = "value02"
        bot.userData[user, "test02"] shouldBe "value02"
    }

    @Test
    suspend fun `class data right clearing`() {
        suspend fun runSingleListener() {
            bot.update.setListener {
                handle(it)
                stopListener()
            }
        }

        // check for clear ctx before
        bot.classData[1, "test"].shouldBeNull()

        // in `test` we set classData param
        doMockHttp(MockUpdate.SINGLE("test"))
        runSingleListener()
        bot.classData[1, "test"].shouldNotBeNull() shouldBe "value"

        // activity still in `TgAnnotationsModel` class, so valid behav is not empty classData
        doMockHttp(MockUpdate.SINGLE("common"))
        runSingleListener()
        bot.classData[1, "test"].shouldNotBeNull() shouldBe "value"

        // but `test2` is not in `TgAnnotationsModel` so there ctx should be clean.
        doMockHttp(MockUpdate.SINGLE("test2"))
        runSingleListener()
        bot.classData[1, "test"].shouldBeNull() // !!!
    }
}
