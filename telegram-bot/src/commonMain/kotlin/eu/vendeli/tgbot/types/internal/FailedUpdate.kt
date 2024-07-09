package eu.vendeli.tgbot.types.internal

data class FailedUpdate(
    val exception: Throwable,
    val update: ProcessedUpdate,
)
