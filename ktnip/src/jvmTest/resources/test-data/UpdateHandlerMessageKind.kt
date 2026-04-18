/* G-EXPECT
file=KtGramCtxLoader.kt
contains="registerUpdateTypeHandler(UpdateType.MESSAGE,"
contains="setOf(eu.vendeli.tgbot.types.component.MessageKind.PHOTO"
contains="eu.vendeli.tgbot.types.component.MessageKind.VIDEO"
contains="photoOrVideoHandler"
contains="plainUpdateHandler"
notContains="setOf(eu.vendeli.tgbot.types.component.MessageKind.PHOTO, eu.vendeli.tgbot.types.component.MessageKind.VIDEO), plainUpdateHandler"
*/

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.types.component.MessageKind
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.UpdateType

@UpdateHandler(
    type = [UpdateType.MESSAGE],
    messageKind = [MessageKind.PHOTO, MessageKind.VIDEO],
)
suspend fun photoOrVideoHandler(update: ProcessedUpdate, bot: TelegramBot) {
}

@UpdateHandler(type = [UpdateType.CALLBACK_QUERY])
suspend fun plainUpdateHandler(update: ProcessedUpdate, bot: TelegramBot) {
}
