---
name: add-wizard-handler
description: Configures @WizardHandler annotation for wizard flows. Use when creating a new wizard, setting wizard triggers, scope, or state managers for the telegram-bot library.
---

# Add Wizard Handler

## Structure

Annotate an object or class with `@WizardHandler`. Nest `WizardStep` objects inside. KSP generates `WizardActivity`; no manual Activity class.

```kotlin
@WizardHandler(trigger = ["/start"])
object MyWizard {
    object NameStep : WizardStep(isInitial = true) { ... }
    object AgeStep : WizardStep { ... }
}
```

## Parameters

| Parameter | Purpose | Default |
|-----------|---------|---------|
| `trigger` | Commands that start the wizard | Required |
| `scope` | Update types to check for trigger | `[UpdateType.MESSAGE]` |
| `stateManagers` | State manager classes for step storage | `MapStringStateManager`, `MapIntStateManager`, `MapLongStateManager` |

## Trigger

- `trigger = ["/start"]` — single command
- `trigger = ["/start", "/register"]` — multiple commands

## Scope

- `scope = [UpdateType.MESSAGE]` — commands in messages (default)
- `scope = [UpdateType.CALLBACK_QUERY]` — button-triggered wizard

## State Managers

KSP matches each step's `store()` return type to a state manager:

- `String?` → `MapStringStateManager`
- `Int?` → `MapIntStateManager`
- `Long?` → `MapLongStateManager`

Override per step with `@WizardHandler.StateManager`:

```kotlin
@WizardHandler(trigger = ["/start"], stateManagers = [CustomStateManager::class])
object MyWizard {
    object NameStep : WizardStep { ... }

    @WizardHandler.StateManager(OtherStateManager::class)
    object AgeStep : WizardStep { ... }
}
```

## Related Skills

- **build-wizard-flow** — Full wizard flow overview
- **add-wizard-step** — Implement WizardStep for each step

## Reference

- [WizardHandler.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/annotations/WizardHandler.kt)
- [WizardStateManager.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/chain/WizardStateManager.kt)
