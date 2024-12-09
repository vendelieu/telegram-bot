package eu.vendeli.tgbot.types.boost

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("source")
@OptIn(ExperimentalSerializationApi::class)
sealed class ChatBoostSource {
    abstract val user: User?
    val source: String by lazy {
        serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("premium")
    data class Premium(
        override val user: User,
    ) : ChatBoostSource()

    @Serializable
    @SerialName("gift_code")
    data class GiftCode(
        override val user: User,
    ) : ChatBoostSource()

    @Serializable
    @SerialName("giveaway")
    data class Giveaway(
        val giveawayMessageId: Long,
        override val user: User? = null,
        val isUnclaimed: Boolean? = null,
        val prizeStarCount: Int? = null,
    ) : ChatBoostSource()
}
