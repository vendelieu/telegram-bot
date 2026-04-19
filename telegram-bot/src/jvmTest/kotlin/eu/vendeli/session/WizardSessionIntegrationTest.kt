package eu.vendeli.session

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chain.Transition
import eu.vendeli.tgbot.types.chain.WizardActivity
import eu.vendeli.tgbot.types.chain.WizardContext
import eu.vendeli.tgbot.types.chain.WizardStateManager
import eu.vendeli.tgbot.types.chain.WizardStep
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.common.Update
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.session.Direction
import eu.vendeli.tgbot.types.session.SessionKey
import eu.vendeli.tgbot.utils.common.processUpdate
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.time.Instant

/**
 * Verifies [WizardContext.session] wiring: when sessions are enabled on the bot,
 * the context handed to a wizard step must carry a session scoped to that wizard
 * (qualifier = `wizard:<id>`), isolated from the bot's default chat session.
 * When sessions are not configured, the context exposes a `null` session so
 * wizards remain usable without the subsystem.
 */
class WizardSessionIntegrationTest : AnnotationSpec() {
    private val user = User(id = 42, isBot = false, firstName = "U")
    private val chat = Chat(id = -7, type = ChatType.Private)

    private fun mkUpdate(text: String) = Update(
        Random.nextInt(),
        message = Message(
            messageId = Random.nextLong(),
            from = user,
            chat = chat,
            date = Instant.DISTANT_PAST,
            text = text,
        ),
    ).processUpdate()

    /** Captures the session visible from a step's onEntry for inspection. */
    private object CapturingWizard : WizardActivity() {
        override val id: Int = 7777
        override val qualifier: String = "test.CapturingWizard"
        override val function: String = "capture"

        var captured: eu.vendeli.tgbot.interfaces.session.Session? = null
        var captureAttempted: Boolean = false

        override val steps: List<WizardStep> = listOf(CaptureStep)

        override fun getStateManagerForStep(
            step: KClass<out WizardStep>,
            bot: TelegramBot,
        ): WizardStateManager<out Any>? = null

        object CaptureStep : WizardStep(isInitial = true) {
            override suspend fun onEntry(ctx: WizardContext) {
                captureAttempted = true
                captured = ctx.session
            }

            override suspend fun validate(ctx: WizardContext): Transition = Transition.Finish
        }
    }

    @BeforeEach
    fun resetCapture() {
        CapturingWizard.captured = null
        CapturingWizard.captureAttempted = false
    }

    @Test
    fun sessionIsNullWhenSubsystemDisabled() = runBlocking {
        val bot = TelegramBot("000000:TEST")
        bot.update.registry.registerActivity(CapturingWizard)

        CapturingWizard.invoke(ProcessingContext(mkUpdate("/go"), bot, bot.update.registry))

        CapturingWizard.captureAttempted shouldBe true
        CapturingWizard.captured.shouldBeNull()
    }

    @Test
    fun sessionIsWizardQualifiedWhenSubsystemEnabled() = runBlocking {
        val bot = TelegramBot("000000:TEST") { sessions { trackIncoming = false } }
        bot.update.registry.registerActivity(CapturingWizard)

        CapturingWizard.invoke(ProcessingContext(mkUpdate("/go"), bot, bot.update.registry))

        val session = CapturingWizard.captured.shouldNotBeNull()
        session.key.shouldBeInstanceOf<SessionKey.ChatUser>()
        (session.key as SessionKey.ChatUser).chatId shouldBe chat.id
        (session.key as SessionKey.ChatUser).userId shouldBe user.id
        session.key.qualifier shouldBe "wizard:${CapturingWizard.id}"
    }

    @Test
    fun wizardSessionIsIsolatedFromDefaultChatSession() = runBlocking {
        val bot = TelegramBot("000000:TEST") { sessions { trackIncoming = false } }
        bot.update.registry.registerActivity(CapturingWizard)

        CapturingWizard.invoke(ProcessingContext(mkUpdate("/go"), bot, bot.update.registry))
        val wizardSession = CapturingWizard.captured.shouldNotBeNull()

        wizardSession.track(
            Message(
                messageId = 1,
                from = user,
                chat = chat,
                date = Instant.DISTANT_PAST,
                text = "wizard-own",
            ),
            Direction.Outgoing,
        )

        val defaultSession = bot.sessions!!.get(chatId = chat.id, userId = user.id)
        defaultSession.track(
            Message(
                messageId = 2,
                from = user,
                chat = chat,
                date = Instant.DISTANT_PAST,
                text = "default",
            ),
            Direction.Outgoing,
        )

        wizardSession.messages().map { it.messageId } shouldBe listOf(1L)
        defaultSession.messages().map { it.messageId } shouldBe listOf(2L)
    }
}
