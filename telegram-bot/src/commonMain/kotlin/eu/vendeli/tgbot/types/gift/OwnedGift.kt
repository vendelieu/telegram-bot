package eu.vendeli.tgbot.types.gift

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.types.story.StoryAreaType
import eu.vendeli.tgbot.types.user.Gift
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.time.Instant

/**
 * This object describes a gift received and owned by a user or a chat. Currently, it can be one of
 * - OwnedGiftRegular
 * - OwnedGiftUnique
 *
 * [Api reference](https://core.telegram.org/bots/api#ownedgift)
 *
 */
@Serializable
sealed class OwnedGift {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("regular")
    data class Regular(
        val gift: Gift,
        @Serializable(InstantSerializer::class)
        val sendDate: Instant,
        val ownedGiftId: String? = null,
        val senderUser: User? = null,
        val text: String? = null,
        val entities: List<MessageEntity>? = null,
        val isPrivate: Boolean? = null,
        val isSaved: Boolean? = null,
        val canBeUpgraded: Boolean? = null,
        val wasRefunded: Boolean? = null,
        val convertStarCount: Int? = null,
        val prepaidUpgradeStarCount: Int? = null,
    ) : OwnedGift()

    @Serializable
    @SerialName("unique")
    data class Unique(
        val gift: StoryAreaType.UniqueGift,
        @Serializable(InstantSerializer::class)
        val sendDate: Instant,
        val ownedGiftId: String? = null,
        val senderUser: User? = null,
        val isSaved: Boolean? = null,
        val canBeTransferred: Boolean? = null,
        val transferStarCount: Int? = null,
        @Serializable(InstantSerializer::class)
        val nextTransferDate: Instant? = null,
    ) : OwnedGift()
}
