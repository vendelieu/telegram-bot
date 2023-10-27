package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.User
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class ContextTest : BotTestContext() {
    private val user = User(TG_ID, false, "")

    @Test
    suspend fun `user data test`() {
        val empty = bot.userData.get<String>(TG_ID, "test")
        empty.shouldBeNull()

        bot.userData.set(TG_ID, "test", "value")
        val set = bot.userData.get<String>(TG_ID, "test")
        set.shouldNotBeNull()
        set shouldBe "value"

        val suspendGet = bot.userData.getAsync<String>(TG_ID, "test2").await()
        suspendGet.shouldBeNull()

        bot.userData.setAsync(TG_ID, "test2", "value2").await()
        val setSuspendGet = bot.userData.getAsync<String>(TG_ID, "test2").await()
        setSuspendGet.shouldNotBeNull()
        setSuspendGet shouldBe "value2"

        bot.userData.set(TG_ID, "test0", "value0")
        bot.userData.get<String>(TG_ID, "test0").shouldNotBeNull()
        bot.userData.del(TG_ID, "test0")
        bot.userData.get<String>(TG_ID, "test0").shouldBeNull()

        bot.userData.set(TG_ID, "test01", "value01")
        bot.userData.get<String>(TG_ID, "test01").shouldNotBeNull()
        bot.userData.delAsync(TG_ID, "test01").await()
        bot.userData.get<String>(TG_ID, "test01").shouldBeNull()

        bot.userData[user, "test02"] = "value02"
        bot.userData[user, "test02"] shouldBe "value02"

        bot.userData[user] = "value03" to "test03"
        bot.userData[user, "test03"] shouldBe "value03"
    }

    @Test
    suspend fun `chat data test`() {
        val empty = bot.chatData.get<String>(TG_ID, "test")
        empty.shouldBeNull()

        bot.chatData.set(TG_ID, "test", "value")
        val set = bot.chatData.get<String>(TG_ID, "test")
        set.shouldNotBeNull()
        set shouldBe "value"

        val suspendGet = bot.chatData.getAsync<String>(TG_ID, "test2").await()
        suspendGet.shouldBeNull()

        bot.chatData.setAsync(TG_ID, "test2", "value2").await()
        val setSuspendGet = bot.chatData.getAsync<String>(TG_ID, "test2").await()
        setSuspendGet.shouldNotBeNull()
        setSuspendGet shouldBe "value2"

        bot.chatData.set(TG_ID, "test0", "value0")
        bot.chatData.get<String>(TG_ID, "test0").shouldNotBeNull()
        bot.chatData.del(TG_ID, "test0")
        bot.chatData.get<String>(TG_ID, "test0").shouldBeNull()

        bot.chatData.set(TG_ID, "test01", "value01")
        bot.chatData.get<String>(TG_ID, "test01").shouldNotBeNull()
        bot.chatData.delAsync(TG_ID, "test01").await()
        bot.chatData.get<String>(TG_ID, "test01").shouldBeNull()

        bot.chatData.clearAll(TG_ID)
        bot.chatData.get<String>(TG_ID, "test").shouldBeNull()
        bot.chatData.get<String>(TG_ID, "test2").shouldBeNull()

        bot.chatData.set(TG_ID, "test3", "value")
        bot.chatData.get<String>(TG_ID, "test3").shouldNotBeNull()
        bot.chatData.clearAllAsync(TG_ID).await()
        bot.chatData.get<String>(TG_ID, "test3").shouldBeNull()

        bot.userData[user, "test02"] = "value02"
        bot.userData[user, "test02"] shouldBe "value02"

        bot.userData[user] = "value03" to "test03"
        bot.userData[user, "test03"] shouldBe "value03"
    }
}
