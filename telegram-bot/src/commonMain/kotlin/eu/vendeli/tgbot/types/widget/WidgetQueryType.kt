package eu.vendeli.tgbot.types.widget

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.ton.TonWalletCard
import kotlinx.serialization.*

/**
 * This object describes the type of a query from a widget. Currently, it can be one of
 * - WidgetQueryTypeData
 * - WidgetQueryTypeSignData
 * - WidgetQueryTypeSentTransaction
 *
 * [Api reference](https://core.telegram.org/bots/api#widgetquerytype)
 *
 */
@Serializable
sealed class WidgetQueryType {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    /**
     * The query contains data for the bot.
     * @property walletCard Optional. TON wallet card of the user who sent the query, if it was sent to the bot along with the query
     */
    @Serializable
    @SerialName("data")
    data class Data(
        val walletCard: TonWalletCard? = null,
    ) : WidgetQueryType()

    /**
     * The query contains data signed by a TON wallet of the user who sent the query. The data is signed for the domain bot<bot_id>, where <bot_id> is the identifier of the bot.
     * @property wallet The wallet that signed the data
     * @property signData JSON-serialized signed data
     */
    @Serializable
    @SerialName("sign_data")
    data class SignData(
        val wallet: WidgetQueryWallet,
        val signData: String,
    ) : WidgetQueryType()

    /**
     * The query notifies the bot about a transaction sent from a TON wallet of the user who sent the query.
     * @property wallet The wallet from which the transaction was sent
     * @property msgHash Value of msg_hash of the transaction
     */
    @Serializable
    @SerialName("sent_transaction")
    data class SentTransaction(
        val wallet: WidgetQueryWallet,
        val msgHash: String,
    ) : WidgetQueryType()
}
