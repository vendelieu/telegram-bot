@file:Suppress("EmptyFunctionBlock")

// @generated ActivitiesData.kt: Activity, TestWizard
// @generated KtGramCtxLoader.kt: registerCommand, registerActivity, TestWizard
// @generated BotCtx.kt:
//   getState
//   String?
//   Int?

import eu.vendeli.tgbot.annotations.WizardHandler
import eu.vendeli.tgbot.generated.getState
import eu.vendeli.tgbot.implementations.MapIntStateManager
import eu.vendeli.tgbot.implementations.MapStateManager
import eu.vendeli.tgbot.types.chain.Transition
import eu.vendeli.tgbot.types.chain.WizardContext
import eu.vendeli.tgbot.types.chain.WizardStep
import eu.vendeli.tgbot.types.component.MessageUpdate
import eu.vendeli.tgbot.types.component.UpdateType

class CustomStringStateManager : MapStateManager<String>()

@WizardHandler(
    trigger = ["/start"],
    scope = [UpdateType.MESSAGE],
    stateManagers = [CustomStringStateManager::class, MapIntStateManager::class],
)
object TestWizard {
    @WizardHandler.StateManager(CustomStringStateManager::class)
    object NameStep : WizardStep(isInitial = true) {
        override suspend fun onEntry(ctx: WizardContext) {}

        override suspend fun validate(ctx: WizardContext): Transition {
            val text = (ctx.update as? MessageUpdate)?.message?.text ?: return Transition.Retry()
            return if (text.isNotBlank() && text.length >= 2) Transition.Next else Transition.Retry()
        }

        override suspend fun store(ctx: WizardContext): String? =
            (ctx.update as? MessageUpdate)?.message?.text
    }

    object AgeStep : WizardStep() {
        override suspend fun onEntry(ctx: WizardContext) {
            val name = ctx.getState<TestWizard.NameStep>(TestWizard.NameStep) // typed: String?
        }

        override suspend fun validate(ctx: WizardContext): Transition {
            val text = (ctx.update as? MessageUpdate)?.message?.text ?: return Transition.Retry()
            val age = text.toIntOrNull()
            return if (age != null && age in 1..150) Transition.Next else Transition.Retry()
        }

        override suspend fun store(ctx: WizardContext): Int? =
            (ctx.update as? MessageUpdate)?.message?.text?.toIntOrNull()
    }

    object ConfirmStep : WizardStep() {
        override suspend fun onEntry(ctx: WizardContext) {}

        override suspend fun validate(ctx: WizardContext): Transition {
            val text = (ctx.update as? MessageUpdate)?.message?.text?.lowercase() ?: return Transition.Retry()
            return when (text) {
                "yes", "y" -> Transition.Finish
                else -> Transition.Retry()
            }
        }
    }
}
