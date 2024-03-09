package eu.vendeli.tgbot.types.internal

data class ActivityCtx<out T : ProcessedUpdate>(val update: T) {
    val user = update.userOrNull
}
