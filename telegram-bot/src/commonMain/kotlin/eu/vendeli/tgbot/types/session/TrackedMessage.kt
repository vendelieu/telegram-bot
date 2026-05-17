package eu.vendeli.tgbot.types.session

import eu.vendeli.tgbot.types.component.MessageKind
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * Metadata captured for every message a session observes.
 *
 * @property messageId Telegram message id.
 * @property chatId Chat the message lives in (may differ from the session's chat when the user tracks manually).
 * @property userId Sender id where known; `null` for channel posts and some service messages.
 * @property kind Content classification via [eu.vendeli.tgbot.types.component.detectKind].
 * @property direction [Direction.Incoming] for updates, [Direction.Outgoing] for bot sends.
 * @property businessConnectionId Preserved for future business-connection-scoped deletion work.
 * @property at Timestamp the entry was recorded.
 */
@Serializable
data class TrackedMessage(
    val messageId: Long,
    val chatId: Long,
    val userId: Long?,
    val kind: MessageKind?,
    val direction: Direction,
    val businessConnectionId: String?,
    @Serializable(InstantSerializer::class)
    val at: Instant,
)
