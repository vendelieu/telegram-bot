package eu.vendeli

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.utils.common.parseCommand
import eu.vendeli.tgbot.utils.common.serde
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

/** Properties that must hold for any input, complementing the example based tests. */
class PropertyBasedTest : AnnotationSpec() {
    private val bot = TelegramBot("000000:TEST")

    @Test
    suspend fun `user survives a serialization round trip`() {
        checkAll(Arb.long(), Arb.boolean(), Arb.string(), Arb.string().orNull(), Arb.string().orNull()) {
            id,
            isBot,
            first,
            last,
            username,
            ->
            val user = User(id, isBot, first, lastName = last, username = username)

            serde.decodeFromString(User.serializer(), serde.encodeToString(User.serializer(), user)) shouldBe user
        }
    }

    @Test
    suspend fun `chat survives a serialization round trip`() {
        checkAll(Arb.long(), Arb.enum<ChatType>(), Arb.string().orNull()) { id, type, title ->
            val chat = Chat(id, type, title = title)

            serde.decodeFromString(Chat.serializer(), serde.encodeToString(Chat.serializer(), chat)) shouldBe chat
        }
    }

    @Test
    suspend fun `command parsing never fails and keeps the command name first`() {
        checkAll(Arb.string()) { text ->
            val parsed = bot.update.parseCommand(text)

            if (text.startsWith("/")) parsed.command shouldStartWith "/"
        }
    }

    @Test
    suspend fun `command parsing splits the command from its tail`() {
        checkAll(Arb.string(minSize = 1, maxSize = 20).map { name -> name.filter(Char::isLetterOrDigit) }) { name ->
            if (name.isEmpty()) return@checkAll
            val parsed = bot.update.parseCommand("/$name?tail")

            parsed.command shouldBe "/$name"
            parsed.tail shouldBe "tail"
        }
    }
}
