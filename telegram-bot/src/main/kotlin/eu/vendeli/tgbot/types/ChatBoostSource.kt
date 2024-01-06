package eu.vendeli.tgbot.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "source",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ChatBoostSource.Premium::class, name = "premium"),
    JsonSubTypes.Type(value = ChatBoostSource.GiftCode::class, name = "gift_code"),
    JsonSubTypes.Type(value = ChatBoostSource.Giveaway::class, name = "giveaway"),
)
sealed class ChatBoostSource(val source: String) {
    abstract val user: User?
    data class Premium(
        override val user: User,
    ) : ChatBoostSource("premium")

    data class GiftCode(
        override val user: User,
    ) : ChatBoostSource("gift_code")

    data class Giveaway(
        val giveawayMessageId: Long,
        override val user: User? = null,
        val isUnclaimed: Boolean? = null,
    ) : ChatBoostSource("giveaway")
}
