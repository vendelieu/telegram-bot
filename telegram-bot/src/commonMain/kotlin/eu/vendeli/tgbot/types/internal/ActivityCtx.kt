package eu.vendeli.tgbot.types.internal

import kotlin.jvm.JvmInline

@JvmInline
value class ActivityCtx<out T : ProcessedUpdate>(val update: T)
