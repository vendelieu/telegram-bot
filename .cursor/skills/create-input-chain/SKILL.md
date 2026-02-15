---
name: create-input-chain
description: Creates multi-step input chains for conversation flows using the telegram-bot DSL. Use when building simple multi-step dialogs without wizard state persistence, or when inputChain is preferred over @WizardHandler.
---

# Create Input Chain

## Quick Start

Define a chain inside `bot.setFunctionality { }`. Set `bot.inputListener[user] = "id"` to start. The chain advances automatically after each step.

```kotlin
bot.setFunctionality {
    onCommand("/start") {
        message { "What's your name?" }.send(user, bot)
        bot.inputListener[user] = "conversation"
    }
    inputChain("conversation") {
        message { "Nice to meet you, ${message.text}!" }.send(update.getUser(), bot)
        message { "What's your favorite food?" }.send(update.getUser(), bot)
    }.breakIf({ message.text == "peanut butter" }) {
        message { "Oh, too bad, I'm allergic." }.send(update.getUser(), bot)
    }.andThen {
        message { "Thanks! You said: ${message.text}" }.send(update.getUser(), bot)
    }
}
bot.handleUpdates()
```

## Structure

- **inputChain("id") { }** — First step. Block runs when user sends a message while `inputListener` is `"id"`.
- **andThen { }** — Next step. Chain advances to this step after the previous block runs.
- **breakIf(condition, repeat = true, block)** — On current step: if condition is true, run block. If `repeat = true`, stay on current step (user can re-enter). If `repeat = false`, do not re-register.

## Context

Inside each block, `ProcessingContext` / `ActivityCtx` provides:

- `update` — the ProcessedUpdate
- `message` — shortcut for message content (when applicable)
- `update.getUser()` — user reference

## When to Use

- **Input chain**: Simple linear flows, no typed state, quick setup
- **Wizard**: Complex validation, typed state, `Transition.JumpTo`/`Retry`, state managers

## Reference

- [InputChainBuilder.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/core/InputChainBuilder.kt)
- [README.md](README.md) — inputChain example
