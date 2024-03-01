@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.answer

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.AnswerInlineQueryOptions
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.serialization.builtins.ListSerializer

class AnswerInlineQueryAction(inlineQueryId: String, results: List<InlineQueryResult>) :
    SimpleAction<Boolean>(),
    OptionsFeature<AnswerInlineQueryAction, AnswerInlineQueryOptions> {
    override val method = TgMethod("answerInlineQuery")
    override val returnType = getReturnType()
    override val options = AnswerInlineQueryOptions()

    init {
        parameters["inline_query_id"] = inlineQueryId.toJsonElement()
        parameters["results"] = results.encodeWith(ListSerializer(DynamicLookupSerializer))
    }
}

/**
 * Use this method to send answers to an inline query. On success, True is returned.
 * No more than 50 results per query are allowed.
 * @param inlineQueryId Required 
 * @param results Required 
 * @param cacheTime The maximum amount of time in seconds that the result of the inline query may be cached on the server. Defaults to 300.
 * @param isPersonal Pass True if results may be cached on the server side only for the user that sent the query. By default, results may be returned to any user who sends the same query.
 * @param nextOffset Pass the offset that a client should send in the next query with the same text to receive more results. Pass an empty string if there are no more results or if you don't support pagination. Offset length can't exceed 64 bytes.
 * @param button A JSON-serialized object describing a button to be shown above inline query results
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#answerinlinequery
*/
@Suppress("NOTHING_TO_INLINE")
inline fun answerInlineQuery(inlineQueryId: String, results: List<InlineQueryResult>) =
    AnswerInlineQueryAction(inlineQueryId, results)
fun answerInlineQuery(inlineQueryId: String, results: ListingBuilder<InlineQueryResult>.() -> Unit) =
    answerInlineQuery(inlineQueryId, ListingBuilder.build(results))

@Suppress("NOTHING_TO_INLINE")
inline fun answerInlineQuery(inlineQueryId: String, vararg result: InlineQueryResult) =
    answerInlineQuery(inlineQueryId, result.asList())
