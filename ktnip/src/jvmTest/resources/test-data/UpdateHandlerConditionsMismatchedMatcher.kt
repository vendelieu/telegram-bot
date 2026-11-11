/* G-EXPECT
error contains="must implement UpdateMatcher<conditiontest.Wrong>"
*/

package conditiontest

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.ConditionOf
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.interfaces.helper.UpdateMatcher
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.UpdateType

annotation class Other

@ConditionOf(OtherMatcher::class)
@UpdateHandler([UpdateType.MESSAGE])
annotation class Wrong

object OtherMatcher : UpdateMatcher<Other> {
    override suspend fun matches(annotation: Other, update: ProcessedUpdate, bot: TelegramBot) = true
}

@Wrong
suspend fun h(update: ProcessedUpdate, bot: TelegramBot) {
}
