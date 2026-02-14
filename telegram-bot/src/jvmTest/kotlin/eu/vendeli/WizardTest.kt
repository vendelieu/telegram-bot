package eu.vendeli

import BotTestContext
import eu.vendeli.fixtures.JumpTestWizard
import eu.vendeli.fixtures.JumpTestWizardActivity
import eu.vendeli.fixtures.TestWizard
import eu.vendeli.fixtures.TestWizardActivity
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chain.Transition
import eu.vendeli.tgbot.types.chain.UserChatReference
import eu.vendeli.tgbot.types.chain.WizardContext
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.common.Update
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.common.processUpdate
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.random.Random
import kotlin.time.Instant

class WizardTest : BotTestContext(true, true) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest

    private val testUser = User(id = 1, isBot = false, firstName = "TestUser")
    private val testChat = Chat(id = 1, type = ChatType.Private)

    private fun createUpdate(text: String) = Update(
        Random.nextInt(),
        message = Message(
            Random.nextLong(),
            from = testUser,
            chat = testChat,
            date = Instant.DISTANT_PAST,
            text = text,
        ),
    ).processUpdate()

    private fun createWizardContext(text: String): WizardContext {
        val update = createUpdate(text)
        return WizardContext(testUser, update, bot)
    }

    @BeforeEach
    fun setUp() {
        TestWizard.reset()
        bot.inputListener.del(testUser.id)
    }

    // ==================== WizardStep Tests ====================

    @Test
    fun `WizardStep id defaults to fully qualified class name`() {
        TestWizard.NameStep.id shouldBe TestWizard.NameStep::class.qualifiedName
        TestWizard.AgeStep.id shouldBe TestWizard.AgeStep::class.qualifiedName
        TestWizard.ConfirmStep.id shouldBe TestWizard.ConfirmStep::class.qualifiedName
    }

    @Test
    fun `WizardStep isInitial property is set correctly`() {
        TestWizard.NameStep.isInitial shouldBe true
        TestWizard.AgeStep.isInitial shouldBe false
        TestWizard.ConfirmStep.isInitial shouldBe false
    }

    @Test
    suspend fun `WizardStep onEntry is called`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.NameStep.id}")

        val ctx = createWizardContext("test")
        TestWizard.NameStep.onEntry(ctx)

        TestWizard.onEntryCalls shouldContainExactly listOf("NameStep")
    }

    @Test
    suspend fun `WizardStep validate returns Next for valid input`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.NameStep.id}")

        val ctx = createWizardContext("John")
        val transition = TestWizard.NameStep.validate(ctx)

        transition shouldBe Transition.Next
        TestWizard.validateCalls shouldContainExactly listOf("NameStep")
    }

    @Test
    suspend fun `WizardStep validate returns Retry for invalid input`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.NameStep.id}")

        val ctx = createWizardContext("J") // Too short
        val transition = TestWizard.NameStep.validate(ctx)

        transition shouldBe Transition.Retry()
    }

    @Test
    suspend fun `WizardStep store returns correct value`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.NameStep.id}")

        val ctx = createWizardContext("John")
        val stored = TestWizard.NameStep.store(ctx)

        stored shouldBe "John"
        TestWizard.storeCalls shouldContainExactly listOf("NameStep")
    }

    @Test
    suspend fun `AgeStep validate returns Retry for non-numeric input`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.AgeStep.id}")

        val ctx = createWizardContext("not a number")
        val transition = TestWizard.AgeStep.validate(ctx)

        transition shouldBe Transition.Retry()
    }

    @Test
    suspend fun `AgeStep validate returns Retry for out of range age`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.AgeStep.id}")

        val ctx = createWizardContext("200")
        val transition = TestWizard.AgeStep.validate(ctx)

        transition shouldBe Transition.Retry()
    }

    @Test
    suspend fun `AgeStep validate returns Next for valid age`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.AgeStep.id}")

        val ctx = createWizardContext("25")
        val transition = TestWizard.AgeStep.validate(ctx)

        transition shouldBe Transition.Next
    }

    @Test
    suspend fun `AgeStep store returns integer value`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.AgeStep.id}")

        val ctx = createWizardContext("25")
        val stored = TestWizard.AgeStep.store(ctx)

        stored shouldBe 25
    }

    // ==================== Transition Tests ====================

    @Test
    suspend fun `ConfirmStep returns Finish transition for yes`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.ConfirmStep.id}")

        val ctx = createWizardContext("yes")
        val transition = TestWizard.ConfirmStep.validate(ctx)

        transition shouldBe Transition.Finish
    }

    @Test
    suspend fun `ConfirmStep returns JumpTo transition for no`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.ConfirmStep.id}")

        val ctx = createWizardContext("no")
        val transition = TestWizard.ConfirmStep.validate(ctx)

        transition.shouldBeInstanceOf<Transition.JumpTo>()
        (transition as Transition.JumpTo).step shouldBe TestWizard.NameStep::class
    }

    @Test
    suspend fun `ConfirmStep returns Retry transition for invalid input`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.ConfirmStep.id}")

        val ctx = createWizardContext("maybe")
        val transition = TestWizard.ConfirmStep.validate(ctx)

        transition shouldBe Transition.Retry()
    }

    // ==================== WizardActivity Tests ====================

    @Test
    suspend fun `WizardActivity start enters initial step`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)

        val ctx = createWizardContext("/wizard")
        wizardActivity.start(ctx)

        TestWizard.onEntryCalls shouldContainExactly listOf("NameStep")
        bot.inputListener.get(testUser.id).shouldNotBeNull()
    }

    @Test
    suspend fun `WizardActivity handleInput transitions to next step`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.NameStep.id}")

        val ctx = createWizardContext("John")
        val nextStep = wizardActivity.handleInput(ctx)

        nextStep shouldBe TestWizard.AgeStep
        TestWizard.validateCalls shouldContainExactly listOf("NameStep")
        TestWizard.storeCalls shouldContainExactly listOf("NameStep")
        TestWizard.onEntryCalls shouldContainExactly listOf("AgeStep")
    }

    @Test
    suspend fun `WizardActivity handleInput retries on invalid input`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.NameStep.id}")

        val ctx = createWizardContext("J") // Too short
        val currentStep = wizardActivity.handleInput(ctx)

        currentStep shouldBe TestWizard.NameStep
        TestWizard.onRetryCalls shouldContainExactly listOf("NameStep")
    }

    @Test
    suspend fun `WizardActivity handleInput finishes wizard`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.ConfirmStep.id}")

        val ctx = createWizardContext("yes")
        val result = wizardActivity.handleInput(ctx)

        result.shouldBeNull() // null indicates wizard finished
        bot.inputListener.get(testUser.id).shouldBeNull() // input listener cleared
    }

    @Test
    suspend fun `WizardActivity handleInput jumps to specified step`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.ConfirmStep.id}")

        val ctx = createWizardContext("no")
        val result = wizardActivity.handleInput(ctx)

        result shouldBe TestWizard.NameStep
        TestWizard.onEntryCalls shouldContainExactly listOf("NameStep")
    }

    // ==================== State Management Tests ====================

    @Test
    suspend fun `State is persisted correctly for string type`() {
        val wizardActivity = TestWizardActivity()
        val stateManager = wizardActivity.getStringStateManager()
        val userRef = UserChatReference(testUser.id, testChat.id)

        stateManager.set(TestWizard.NameStep::class, userRef, "John")
        val retrieved = stateManager.get(TestWizard.NameStep::class, userRef)

        retrieved shouldBe "John"
    }

    @Test
    suspend fun `State is persisted correctly for int type`() {
        val wizardActivity = TestWizardActivity()
        val stateManager = wizardActivity.getIntStateManager()
        val userRef = UserChatReference(testUser.id, testChat.id)

        stateManager.set(TestWizard.AgeStep::class, userRef, 25)
        val retrieved = stateManager.get(TestWizard.AgeStep::class, userRef)

        retrieved shouldBe 25
    }

    @Test
    suspend fun `State can be deleted`() {
        val wizardActivity = TestWizardActivity()
        val stateManager = wizardActivity.getStringStateManager()
        val userRef = UserChatReference(testUser.id, testChat.id)

        stateManager.set(TestWizard.NameStep::class, userRef, "John")
        stateManager.del(TestWizard.NameStep::class, userRef)
        val retrieved = stateManager.get(TestWizard.NameStep::class, userRef)

        retrieved.shouldBeNull()
    }

    @Test
    suspend fun `State is isolated per user`() {
        val wizardActivity = TestWizardActivity()
        val stateManager = wizardActivity.getStringStateManager()
        val userRef1 = UserChatReference(1L, 1L)
        val userRef2 = UserChatReference(2L, 2L)

        stateManager.set(TestWizard.NameStep::class, userRef1, "John")
        stateManager.set(TestWizard.NameStep::class, userRef2, "Jane")

        stateManager.get(TestWizard.NameStep::class, userRef1) shouldBe "John"
        stateManager.get(TestWizard.NameStep::class, userRef2) shouldBe "Jane"
    }

    // ==================== Full Wizard Flow Tests ====================

    @Test
    suspend fun `Complete wizard flow from start to finish`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)

        // Start wizard
        val startCtx = createWizardContext("/wizard")
        wizardActivity.start(startCtx)
        TestWizard.onEntryCalls shouldContainExactly listOf("NameStep")

        // Enter name
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.NameStep.id}")
        val nameCtx = createWizardContext("John")
        val afterName = wizardActivity.handleInput(nameCtx)
        afterName shouldBe TestWizard.AgeStep

        // Enter age
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.AgeStep.id}")
        val ageCtx = createWizardContext("25")
        val afterAge = wizardActivity.handleInput(ageCtx)
        afterAge shouldBe TestWizard.ConfirmStep

        // Confirm
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.ConfirmStep.id}")
        val confirmCtx = createWizardContext("yes")
        val result = wizardActivity.handleInput(confirmCtx)
        result.shouldBeNull()

        // Verify all calls
        TestWizard.onEntryCalls shouldContainExactly listOf("NameStep", "AgeStep", "ConfirmStep")
        TestWizard.validateCalls shouldContainExactly listOf("NameStep", "AgeStep", "ConfirmStep")
        TestWizard.storeCalls shouldContainExactly listOf("NameStep", "AgeStep", "ConfirmStep")
    }

    @Test
    suspend fun `Wizard flow with retry and restart`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)

        // Start wizard
        val startCtx = createWizardContext("/wizard")
        wizardActivity.start(startCtx)

        // Invalid name (too short)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.NameStep.id}")
        val invalidNameCtx = createWizardContext("J")
        val afterInvalidName = wizardActivity.handleInput(invalidNameCtx)
        afterInvalidName shouldBe TestWizard.NameStep
        TestWizard.onRetryCalls shouldContainExactly listOf("NameStep")

        // Valid name
        val validNameCtx = createWizardContext("John")
        val afterValidName = wizardActivity.handleInput(validNameCtx)
        afterValidName shouldBe TestWizard.AgeStep

        // Enter age
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.AgeStep.id}")
        val ageCtx = createWizardContext("25")
        wizardActivity.handleInput(ageCtx)

        // Decline confirmation (restart)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${TestWizard.ConfirmStep.id}")
        val declineCtx = createWizardContext("no")
        val afterDecline = wizardActivity.handleInput(declineCtx)
        afterDecline shouldBe TestWizard.NameStep
    }

    // ==================== JumpTo Wizard Tests ====================

    @Test
    suspend fun `JumpTo wizard skips intermediate step`() {
        val wizardActivity = JumpTestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)
        bot.inputListener.set(testUser.id, "wizard:${wizardActivity.id}:${JumpTestWizard.Step1.id}")

        val ctx = createWizardContext("anything")
        val result = wizardActivity.handleInput(ctx)

        result shouldBe JumpTestWizard.Step3
    }

    // ==================== UserChatReference Tests ====================

    @Test
    fun `UserChatReference equality works correctly`() {
        val ref1 = UserChatReference(1L, 2L)
        val ref2 = UserChatReference(1L, 2L)
        val ref3 = UserChatReference(1L, 3L)

        (ref1 == ref2) shouldBe true
        (ref1 == ref3) shouldBe false
    }

    @Test
    fun `UserChatReference hashCode is consistent`() {
        val ref1 = UserChatReference(1L, 2L)
        val ref2 = UserChatReference(1L, 2L)

        ref1.hashCode() shouldBe ref2.hashCode()
    }

    // ==================== Edge Cases ====================

    @Test
    suspend fun `Wizard handles empty steps list gracefully`() {
        val emptyWizard = object : eu.vendeli.tgbot.types.chain.WizardActivity() {
            override val id: Int = 999
            override val qualifier: String = "test"
            override val function: String = "empty"
            override val steps: List<eu.vendeli.tgbot.types.chain.WizardStep> = emptyList()
            override fun getStateManagerForStep(
                step: kotlin.reflect.KClass<out eu.vendeli.tgbot.types.chain.WizardStep>,
                bot: eu.vendeli.tgbot.TelegramBot,
            ): eu.vendeli.tgbot.types.chain.WizardStateManager<out Any>? = null
        }

        bot.update.registry.registerActivity(emptyWizard)
        val ctx = createWizardContext("/empty")

        try {
            emptyWizard.start(ctx)
            throw AssertionError("Expected error for empty steps")
        } catch (e: IllegalStateException) {
            e.message shouldBe "Wizard must have at least one step"
        }
    }

    @Test
    suspend fun `Wizard returns null when no next step exists`() {
        val wizardActivity = TestWizardActivity()
        bot.update.registry.registerActivity(wizardActivity)

        // Set to ConfirmStep and make it return Next instead of Finish
        // We'll use AgeStep as the last step for this test
        val singleStepWizard = object : eu.vendeli.tgbot.types.chain.WizardActivity() {
            override val id: Int = 998
            override val qualifier: String = "test"
            override val function: String = "single"
            override val steps = listOf(TestWizard.NameStep) // Only one step
            override fun getStateManagerForStep(
                step: kotlin.reflect.KClass<out eu.vendeli.tgbot.types.chain.WizardStep>,
                bot: eu.vendeli.tgbot.TelegramBot,
            ): eu.vendeli.tgbot.types.chain.WizardStateManager<out Any>? = null
        }

        bot.update.registry.registerActivity(singleStepWizard)
        bot.inputListener.set(testUser.id, "wizard:${singleStepWizard.id}:${TestWizard.NameStep.id}")

        val ctx = createWizardContext("John") // Valid name, will trigger Transition.Next
        val result = singleStepWizard.handleInput(ctx)

        result.shouldBeNull() // No next step
    }
}
