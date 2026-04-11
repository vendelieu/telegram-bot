/* G-EXPECT
file=ActivitiesData.kt
contains="handleTextNumbers"
contains="Activity"
notMatches="object\s+\S*_handleTextNumbers_[0-9a-f]+[\s\S]+object\s+\S*_handleTextNumbers_[0-9a-f]+"

file=KtGramCtxLoader.kt
contains="registerCommonHandler"
contains="First"
contains="Second"
contains="Third"
notMatches="registerActivity\([^)]*_handleTextNumbers_[0-9a-f]+[^)]*\)[\s\S]+registerActivity\([^)]*_handleTextNumbers_[0-9a-f]+[^)]*\)"
*/

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.annotations.CommonHandler
import eu.vendeli.tgbot.types.component.UpdateType

@CommonHandler.Text(
    value = ["First", "Second", "Third"],
    scope = [UpdateType.MESSAGE],
)
suspend fun handleTextNumbers(update: ProcessedUpdate, user: User, bot: TelegramBot) {
}
