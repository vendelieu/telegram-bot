package com.github.vendelieu.tgbot.core

import com.github.vendelieu.tgbot.core.ManualHandlingDsl.ArgsMode.Query
import com.github.vendelieu.tgbot.core.ManualHandlingDsl.ArgsMode.SpaceKeyValue
import com.github.vendelieu.tgbot.interfaces.BotWaitingInput
import com.github.vendelieu.tgbot.types.*
import com.github.vendelieu.tgbot.utils.parseKeyValueBySpace
import com.github.vendelieu.tgbot.utils.parseQuery
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ManualHandlingDsl internal constructor(
    private val waitingInput: BotWaitingInput,
    private val update: Update,
) {
    /**
     * Argument parsing mode
     * @property Query command?key=value&another=value
     * @property SpaceKeyValue command key value another value
     * (note that if the key-value pair is not fulfilled, the value will be empty string)
     */
    enum class ArgsMode {
        Query, SpaceKeyValue
    }

    var argsParsingMode: ArgsMode = Query

    suspend fun onMessage(block: suspend CoroutineContext.() -> Unit) {
        if (update.message != null) coroutineContext.block()
    }

    suspend fun onEditedMessage(block: suspend CoroutineContext.(Message) -> Unit) {
        if (update.editedMessage != null) coroutineContext.block(update.editedMessage)
    }

    suspend fun onPollAnswer(block: suspend CoroutineContext.(PollAnswer) -> Unit) {
        if (update.pollAnswer != null) coroutineContext.block(update.pollAnswer)
    }

    suspend fun onCallbackQuery(block: suspend CoroutineContext.(CallbackQuery) -> Unit) {
        if (update.callbackQuery != null) coroutineContext.block(update.callbackQuery)
    }

    suspend fun onPoll(block: suspend CoroutineContext.(Poll) -> Unit) {
        if (update.poll != null) coroutineContext.block(update.poll)
    }

    suspend fun onChatJoinRequest(block: suspend CoroutineContext.(ChatJoinRequest) -> Unit) {
        if (update.chatJoinRequest != null) coroutineContext.block(update.chatJoinRequest)
    }

    suspend fun onChatMember(block: suspend CoroutineContext.(ChatMemberUpdated) -> Unit) {
        if (update.chatMember != null) coroutineContext.block(update.chatMember)
    }

    suspend fun onMyChatMember(block: suspend CoroutineContext.(ChatMemberUpdated) -> Unit) {
        if (update.myChatMember != null) coroutineContext.block(update.myChatMember)
    }

    suspend fun onChannelPost(block: suspend CoroutineContext.(Message) -> Unit) {
        if (update.channelPost != null) coroutineContext.block(update.channelPost)
    }

    suspend fun onEditedChannelPost(block: suspend CoroutineContext.(Message) -> Unit) {
        if (update.editedChannelPost != null) coroutineContext.block(update.editedChannelPost)
    }

    suspend fun onChosenInlineResult(block: suspend CoroutineContext.(ChosenInlineResult) -> Unit) {
        if (update.chosenInlineResult != null) coroutineContext.block(update.chosenInlineResult)
    }

    suspend fun onInlineQuery(block: suspend CoroutineContext.(InlineQuery) -> Unit) {
        if (update.inlineQuery != null) coroutineContext.block(update.inlineQuery)
    }

    suspend fun onPreCheckoutQuery(block: suspend CoroutineContext.(PreCheckoutQuery) -> Unit) {
        if (update.preCheckoutQuery != null) coroutineContext.block(update.preCheckoutQuery)
    }

    suspend fun onShippingQuery(block: suspend CoroutineContext.(ShippingQuery) -> Unit) {
        if (update.shippingQuery != null) coroutineContext.block(update.shippingQuery)
    }

    suspend fun onCommand(command: String, block: suspend CoroutineContext.(Map<String, String>) -> Unit) {
        update.message?.text?.also {
            val structuredRequest = if (argsParsingMode == Query) it.parseQuery() else it.parseKeyValueBySpace()
            if (structuredRequest.command == command) coroutineContext.block(structuredRequest.params)
        }
    }

    suspend fun onCommand(command: Regex, block: suspend CoroutineContext.(Map<String, String>) -> Unit) {
        update.message?.text?.also {
            val structuredRequest = if (argsParsingMode == Query) it.parseQuery() else it.parseKeyValueBySpace()
            if (command.matches(structuredRequest.command)) coroutineContext.block(structuredRequest.params)
        }
    }

    suspend fun onInput(identifier: String, block: suspend CoroutineContext.() -> Unit) {
        update.message?.from?.id?.also {
            if (waitingInput.get(it) == identifier) coroutineContext.block()
        }
    }
}
