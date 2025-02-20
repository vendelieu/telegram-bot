package eu.vendeli.tgbot.types.component

data class FailedUpdate(
    val exception: Throwable,
    val update: ProcessedUpdate,
)
