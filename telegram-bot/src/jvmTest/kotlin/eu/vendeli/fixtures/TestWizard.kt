@file:Suppress("EmptyFunctionBlock")

package eu.vendeli.fixtures

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.implementations.MapIntStateManager
import eu.vendeli.tgbot.implementations.MapStringStateManager
import eu.vendeli.tgbot.types.chain.Transition
import eu.vendeli.tgbot.types.chain.WizardActivity
import eu.vendeli.tgbot.types.chain.WizardContext
import eu.vendeli.tgbot.types.chain.WizardStateManager
import eu.vendeli.tgbot.types.chain.WizardStep
import eu.vendeli.tgbot.types.component.MessageUpdate
import kotlin.reflect.KClass

/**
 * Test wizard for collecting user name and age.
 * Used for testing WizardActivity, WizardStep, and state management.
 */
object TestWizard {
    // Track calls for testing
    val onEntryCalls = mutableListOf<String>()
    val onRetryCalls = mutableListOf<String>()
    val validateCalls = mutableListOf<String>()
    val storeCalls = mutableListOf<String>()

    fun reset() {
        onEntryCalls.clear()
        onRetryCalls.clear()
        validateCalls.clear()
        storeCalls.clear()
    }

    /**
     * Initial step - asks for user's name
     */
    object NameStep : WizardStep(isInitial = true) {
        override suspend fun onEntry(ctx: WizardContext) {
            onEntryCalls.add("NameStep")
        }

        override suspend fun onRetry(ctx: WizardContext, reason: String?) {
            onRetryCalls.add("NameStep")
        }

        override suspend fun validate(ctx: WizardContext): Transition {
            validateCalls.add("NameStep")
            val text = (ctx.update as? MessageUpdate)?.message?.text ?: return Transition.Retry()
            return if (text.isNotBlank() && text.length >= 2) {
                Transition.Next
            } else {
                Transition.Retry()
            }
        }

        override suspend fun store(ctx: WizardContext): String? {
            storeCalls.add("NameStep")
            return (ctx.update as? MessageUpdate)?.message?.text
        }
    }

    /**
     * Second step - asks for user's age
     */
    object AgeStep : WizardStep() {
        override suspend fun onEntry(ctx: WizardContext) {
            onEntryCalls.add("AgeStep")
        }

        override suspend fun onRetry(ctx: WizardContext, reason: String?) {
            onRetryCalls.add("AgeStep")
        }

        override suspend fun validate(ctx: WizardContext): Transition {
            validateCalls.add("AgeStep")
            val text = (ctx.update as? MessageUpdate)?.message?.text ?: return Transition.Retry()
            val age = text.toIntOrNull()
            return if (age != null && age in 1..150) {
                Transition.Next
            } else {
                Transition.Retry()
            }
        }

        override suspend fun store(ctx: WizardContext): Int? {
            storeCalls.add("AgeStep")
            return (ctx.update as? MessageUpdate)?.message?.text?.toIntOrNull()
        }
    }

    /**
     * Final step - confirmation
     */
    object ConfirmStep : WizardStep() {
        override suspend fun onEntry(ctx: WizardContext) {
            onEntryCalls.add("ConfirmStep")
        }

        override suspend fun validate(ctx: WizardContext): Transition {
            validateCalls.add("ConfirmStep")
            val text = (ctx.update as? MessageUpdate)?.message?.text?.lowercase() ?: return Transition.Retry()
            return when (text) {
                "yes", "y" -> Transition.Finish
                "no", "n" -> Transition.JumpTo(NameStep::class)
                else -> Transition.Retry()
            }
        }

        override suspend fun store(ctx: WizardContext): String? {
            storeCalls.add("ConfirmStep")
            return (ctx.update as? MessageUpdate)?.message?.text
        }
    }
}

/**
 * Test WizardActivity implementation that manages the TestWizard flow.
 */
class TestWizardActivity : WizardActivity() {
    override val id: Int = 100
    override val qualifier: String = "eu.vendeli.fixtures.TestWizard"
    override val function: String = "wizard"

    override val steps: List<WizardStep> = listOf(
        TestWizard.NameStep,
        TestWizard.AgeStep,
        TestWizard.ConfirmStep,
    )

    private val stringStateManager = MapStringStateManager()
    private val intStateManager = MapIntStateManager()

    override fun getStateManagerForStep(
        step: KClass<out WizardStep>,
        bot: TelegramBot,
    ): WizardStateManager<out Any>? = when (step) {
        TestWizard.NameStep::class -> stringStateManager
        TestWizard.AgeStep::class -> intStateManager
        TestWizard.ConfirmStep::class -> stringStateManager
        else -> null
    }

    // Expose state managers for testing
    fun getStringStateManager(): WizardStateManager<String> = stringStateManager
    fun getIntStateManager(): WizardStateManager<Int> = intStateManager
}

/**
 * Alternative wizard for testing JumpTo transitions
 */
object JumpTestWizard {
    object Step1 : WizardStep(isInitial = true) {
        override suspend fun onEntry(ctx: WizardContext) {}
        override suspend fun validate(ctx: WizardContext): Transition = Transition.JumpTo(Step3::class)
    }

    object Step2 : WizardStep() {
        override suspend fun onEntry(ctx: WizardContext) {}
        override suspend fun validate(ctx: WizardContext): Transition = Transition.Next
    }

    object Step3 : WizardStep() {
        override suspend fun onEntry(ctx: WizardContext) {}
        override suspend fun validate(ctx: WizardContext): Transition = Transition.Finish
    }
}

class JumpTestWizardActivity : WizardActivity() {
    override val id: Int = 101
    override val qualifier: String = "eu.vendeli.fixtures.JumpTestWizard"
    override val function: String = "jumpWizard"

    override val steps: List<WizardStep> = listOf(
        JumpTestWizard.Step1,
        JumpTestWizard.Step2,
        JumpTestWizard.Step3,
    )

    override fun getStateManagerForStep(
        step: KClass<out WizardStep>,
        bot: TelegramBot,
    ): WizardStateManager<out Any>? = null
}
