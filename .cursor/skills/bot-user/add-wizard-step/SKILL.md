---
name: add-wizard-step
description: [Bot developer] Implements WizardStep for wizard flows. Use when adding a step to a wizard, implementing validation, state storage, or transitions in the telegram-bot library.
---

# Add Wizard Step

## Structure

Extend `WizardStep`. Override `onEntry` (required) and optionally `validate`, `store`, `onRetry`.

```kotlin
object NameStep : WizardStep(isInitial = true) {
    override suspend fun onEntry(ctx: WizardContext) {
        message("What's your name?").send(ctx.user, ctx.bot)
    }
    override suspend fun validate(ctx: WizardContext): Transition {
        val text = (ctx.update as? MessageUpdate)?.message?.text ?: return Transition.Retry()
        return if (text.isNotBlank()) Transition.Next else Transition.Retry("Invalid")
    }
    override suspend fun store(ctx: WizardContext): String? =
        (ctx.update as? MessageUpdate)?.message?.text
}
```

## Lifecycle Methods

| Method | Required | Purpose |
|--------|----------|---------|
| `onEntry(ctx)` | Yes | Called when entering step. Send prompts, set keyboards. |
| `validate(ctx)` | No | Return `Transition` based on input. Default: `Transition.Next` |
| `store(ctx)` | No | Return value to persist. Type must match a state manager. Default: `null` |
| `onRetry(ctx, reason)` | No | Called when validation returns `Transition.Retry`. Default: no-op |

## Transitions

| Transition | Effect |
|------------|--------|
| `Transition.Next` | Move to next step in sequence |
| `Transition.JumpTo(Step::class)` | Jump to specific step |
| `Transition.Retry()` or `Transition.Retry("reason")` | Stay on step; `onRetry` is called |
| `Transition.Finish` | End wizard |

## WizardContext

- `ctx.user`, `ctx.update`, `ctx.bot`
- `ctx.getState(Step::class)`, `ctx.setState(Step::class, value)`, `ctx.delState(Step::class)` — KSP generates type-safe accessors per step

## Step ID

Override `id` if needed: `override val id: String = "custom_id"`. Default: fully qualified class name.

## Initial Step

Set `isInitial = true` on exactly one step. That step runs when the wizard starts.

## Related Skills

- **build-wizard-flow** — Full wizard flow overview
- **add-wizard-handler** — Configure @WizardHandler (trigger, scope, state managers)

## Reference

- [WizardStep.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/chain/WizardStep.kt)
- [WizardActivity.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/chain/WizardActivity.kt)
- [TestWizard.kt](telegram-bot/src/jvmTest/kotlin/eu/vendeli/fixtures/TestWizard.kt)
