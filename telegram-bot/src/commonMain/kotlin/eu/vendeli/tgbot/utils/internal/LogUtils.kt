package eu.vendeli.tgbot.utils.internal

import eu.vendeli.tgbot.core.Activity
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.utils.common.fqName

internal fun Activity.prettyPrint(): String = buildString {
    append("${qualifier}.${function} (ID: $id)")
    if (rateLimits.rate > 0 || rateLimits.period > 0) append(" ${rateLimits.prettyPrint()}")
    if (guardClass.fqName != DefaultGuard::class.fqName) append(" | \uD83D\uDEE1\uFE0F ${guardClass.simpleName}")
    if (argParser.fqName != DefaultArgParser::class.fqName) append(" | \uD83D\uDEE0 ${argParser.simpleName}")
}


