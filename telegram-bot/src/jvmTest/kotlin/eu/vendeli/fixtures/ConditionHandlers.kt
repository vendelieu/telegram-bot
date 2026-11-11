@file:Suppress("EmptyFunctionBlock")

package eu.vendeli.fixtures

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.ConditionOf
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.interfaces.helper.UpdateMatcher
import eu.vendeli.tgbot.types.component.MessageKind
import eu.vendeli.tgbot.types.component.MessageUpdate
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.utils.common.safeCast
import java.util.concurrent.ConcurrentLinkedQueue

/** Records which condition-gated fixture handlers fired. */
object ConditionCalls {
    val fired = ConcurrentLinkedQueue<String>()
    val evaluations = ConcurrentLinkedQueue<String>()

    fun reset() {
        fired.clear()
        evaluations.clear()
    }
}

/** Photo message whose widest size is at least [minWidth]. */
@ConditionOf(MinPhotoWidthMatcher::class)
@UpdateHandler([UpdateType.MESSAGE], messageKind = [MessageKind.PHOTO])
annotation class WidePhoto(
    val minWidth: Int = 0,
)

object MinPhotoWidthMatcher : UpdateMatcher<WidePhoto> {
    override suspend fun matches(annotation: WidePhoto, update: ProcessedUpdate, bot: TelegramBot): Boolean {
        ConditionCalls.evaluations += "MinPhotoWidth(${annotation.minWidth})"
        return update
            .safeCast<MessageUpdate>()
            ?.message
            ?.photo
            ?.any { it.width >= annotation.minWidth } == true
    }
}

/** Message caption containing [text]; carries no `@UpdateHandler`, meant to be combined with one. */
@ConditionOf(CaptionContainsMatcher::class)
annotation class CaptionContains(
    val text: String,
    val ignoreCase: Boolean = false,
)

class CaptionContainsMatcher : UpdateMatcher<CaptionContains> {
    override suspend fun matches(annotation: CaptionContains, update: ProcessedUpdate, bot: TelegramBot): Boolean {
        ConditionCalls.evaluations += "CaptionContains(${annotation.text})"
        val caption = update.safeCast<MessageUpdate>()?.message?.caption ?: return false
        return caption.contains(annotation.text, annotation.ignoreCase)
    }
}

/** Composed annotation: wide photo that also mentions "invoice". */
@WidePhoto(minWidth = 500)
@CaptionContains("INVOICE", ignoreCase = true)
annotation class WideInvoicePhoto

class ConditionHandlers {
    @WidePhoto(minWidth = 800)
    fun widePhoto() {
        ConditionCalls.fired += "widePhoto"
    }

    @UpdateHandler([UpdateType.MESSAGE], messageKind = [MessageKind.PHOTO])
    @CaptionContains("receipt")
    fun receiptPhoto() {
        ConditionCalls.fired += "receiptPhoto"
    }

    @WideInvoicePhoto
    fun wideInvoicePhoto() {
        ConditionCalls.fired += "wideInvoicePhoto"
    }
}
