/* G-EXPECT
file=KtGramCtxLoader.kt
contains="registerUpdateTypeHandler(UpdateType.MESSAGE,"
contains="setOf(eu.vendeli.tgbot.types.component.MessageKind.PHOTO)"
contains="conditiontest.WidePhotoMatcher.bind(conditiontest.WidePhoto(minWidth = 800))"
contains="conditiontest.NamedMatcher().bind(conditiontest.Named(name = "
contains="mode = conditiontest.Mode.STRICT, sizes = intArrayOf(1, 2))"
contains="arrayOf("
contains="emptySet(), listOf("
contains="registerUpdateTypeHandler(UpdateType.CALLBACK_QUERY, conditiontest_unconditionalHandler"
noError
*/

package conditiontest

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.ConditionOf
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.interfaces.helper.UpdateMatcher
import eu.vendeli.tgbot.types.component.MessageKind
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.UpdateType

@ConditionOf(WidePhotoMatcher::class)
@UpdateHandler([UpdateType.MESSAGE], messageKind = [MessageKind.PHOTO])
annotation class WidePhoto(val minWidth: Int = 0)

object WidePhotoMatcher : UpdateMatcher<WidePhoto> {
    override suspend fun matches(annotation: WidePhoto, update: ProcessedUpdate, bot: TelegramBot) = true
}

enum class Mode { STRICT, LAX }

@ConditionOf(NamedMatcher::class)
annotation class Named(
    val name: String,
    val tags: Array<String> = [],
    val mode: Mode = Mode.LAX,
    val sizes: IntArray = [],
)

class NamedMatcher : UpdateMatcher<Named> {
    override suspend fun matches(annotation: Named, update: ProcessedUpdate, bot: TelegramBot) = true
}

@WidePhoto(minWidth = 800)
suspend fun widePhotoHandler(update: ProcessedUpdate, bot: TelegramBot) {
}

@UpdateHandler([UpdateType.MESSAGE])
@Named("a b", tags = ["x", "y"], mode = Mode.STRICT, sizes = [1, 2])
suspend fun namedHandler(update: ProcessedUpdate, bot: TelegramBot) {
}

@UpdateHandler([UpdateType.CALLBACK_QUERY])
suspend fun unconditionalHandler(update: ProcessedUpdate, bot: TelegramBot) {
}
