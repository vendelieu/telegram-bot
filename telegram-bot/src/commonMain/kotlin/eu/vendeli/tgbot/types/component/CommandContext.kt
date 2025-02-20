package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.annotations.dsl.FunctionalDSL

@FunctionalDSL
data class CommandContext<out T : ProcessedUpdate>(
    val update: T,
    val parameters: Map<String, String>,
) {
    val user = update.getUser()
}
