package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.api.botactions.getUpdates
import eu.vendeli.tgbot.types.internal.UpdateType
import kotlinx.coroutines.CoroutineDispatcher

internal val DEFAULT_SCOPE = setOf(UpdateType.MESSAGE)
internal expect val PROCESSING_DISPATCHER: CoroutineDispatcher

internal val GET_UPDATES_ACTION = getUpdates()

const val DEFAULT_CONTENT_TYPE = "text/plain"
const val DEFAULT_FILENAME = "file"

val DEFAULT_HANDLING_BEHAVIOUR: HandlingBehaviourBlock = { handle(it) }
