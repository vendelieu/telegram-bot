package eu.vendeli.tgbot.types

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("source")
@OptIn(ExperimentalSerializationApi::class)
sealed class ChatBoostSource(val source: String) {
    abstract val user: User?

    @Serializable
    @SerialName("premium")
    data class Premium(
        override val user: User,
    ) : ChatBoostSource("premium")

    @Serializable
    @SerialName("gift_code")
    data class GiftCode(
        override val user: User,
    ) : ChatBoostSource("gift_code")

    @Serializable
    @SerialName("giveaway")
    data class Giveaway(
        val giveawayMessageId: Long,
        override val user: User? = null,
        val isUnclaimed: Boolean? = null,
    ) : ChatBoostSource("giveaway")
}
