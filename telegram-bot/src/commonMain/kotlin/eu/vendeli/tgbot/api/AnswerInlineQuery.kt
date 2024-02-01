@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

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

inline fun answerInlineQuery(inlineQueryId: String, results: List<InlineQueryResult>) =
    AnswerInlineQueryAction(inlineQueryId, results)
fun answerInlineQuery(inlineQueryId: String, results: ListingBuilder<InlineQueryResult>.() -> Unit) =
    answerInlineQuery(inlineQueryId, ListingBuilder.build(results))

inline fun answerInlineQuery(inlineQueryId: String, vararg result: InlineQueryResult) =
    answerInlineQuery(inlineQueryId, result.asList())
