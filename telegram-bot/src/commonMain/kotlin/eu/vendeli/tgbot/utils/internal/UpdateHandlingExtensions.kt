package eu.vendeli.tgbot.utils.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ParsedText
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.utils.common.cast
import eu.vendeli.tgbot.utils.common.defaultArgParser
import eu.vendeli.tgbot.utils.common.fqName
import kotlin.reflect.KClass

internal suspend inline fun KClass<out Guard>.checkIsGuarded(
    user: User?,
    update: ProcessedUpdate,
    bot: TelegramBot,
): Boolean {
    if (fqName == DefaultGuard::class.fqName) return true
    return bot.config.classManager
        .getInstance(this)
        .cast<Guard>()
        .condition(user, update, bot)
}

internal suspend inline fun KClass<out Filter>.checkIsNotFiltered(
    user: User?,
    update: ProcessedUpdate,
    bot: TelegramBot,
): Boolean {
    if (fqName == DefaultFilter::class.fqName) return true
    return bot.config.classManager
        .getInstance(this)
        .cast<Filter>()
        .checkIsNotFiltered(user, update, bot)
}

internal suspend inline fun Filter.checkIsNotFiltered(
    user: User?,
    update: ProcessedUpdate,
    bot: TelegramBot,
): Boolean = match(user, update, bot)

internal fun TgUpdateHandler.getParameters(
    parser: KClass<out ArgumentParser>?,
    request: ParsedText,
): Map<String, String> = parser
    ?.let {
        if (it.fqName == DefaultArgParser::class.fqName) bot.config.commandParsing.run {
            defaultArgParser(
                request.tail,
                parametersDelimiter,
                parameterValueDelimiter,
            )
        }
        else bot.config.classManager
            .getInstance(it)
            .cast<ArgumentParser>()
            .parse(request.tail)
    } ?: emptyMap()
