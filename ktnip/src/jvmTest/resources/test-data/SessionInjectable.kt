/* G-EXPECT
file=ActivitiesData.kt
contains="bot.sessions?.of(update)"
contains="bot.sessions?.of(update, "
contains="is not configured on this bot"
matches="of\(update, .wizard.\)"
matches="of\(update, .support.\)"
notContains="Can't acquire"
*/

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.SessionQualifier
import eu.vendeli.tgbot.interfaces.session.Session

@CommandHandler(["/start"])
suspend fun sessionStart(session: Session, bot: TelegramBot) {
}

@CommandHandler(["/maybe"])
suspend fun sessionMaybe(session: Session?, bot: TelegramBot) {
}

@CommandHandler(["/multi"])
suspend fun multiSession(
    @SessionQualifier("wizard") wizard: Session,
    @SessionQualifier("support") support: Session,
    bot: TelegramBot,
) {
}
