package eu.vendeli.tgbot.types.internal

@JvmInline
value class ActivityCtx<out T : ProcessedUpdate>(val update: T)
