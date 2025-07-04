package eu.vendeli.tgbot.types.checklist

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * Describes a task in a checklist.
 *
 * [Api reference](https://core.telegram.org/bots/api#checklisttask)
 * @property id Unique identifier of the task
 * @property text Text of the task
 * @property textEntities Optional. Special entities that appear in the task text
 * @property completedByUser Optional. User that completed the task; omitted if the task wasn't completed
 * @property completionDate Optional. Point in time (Unix timestamp) when the task was completed; 0 if the task wasn't completed
 */
@Serializable
data class ChecklistTask(
    val id: Int,
    val text: String,
    val textEntities: List<MessageEntity>? = null,
    val completedByUser: User? = null,
    @Serializable(InstantSerializer::class)
    val completionDate: Instant? = null,
)
