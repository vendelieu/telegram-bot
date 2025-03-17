import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.Injectable
import eu.vendeli.tgbot.interfaces.marker.Autowiring
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

data class TestEntity(
    val testVal: Int,
)

@Injectable
class TestInjectable : Autowiring<TestEntity> {
    override suspend fun get(
        update: ProcessedUpdate,
        bot: TelegramBot,
    ): TestEntity = TestEntity(1)
}

@CommandHandler(["/test"])
suspend fun test(entity: TestEntity, user: User, bot: TelegramBot) {
}
