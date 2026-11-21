/* G-EXPECT
file=reachability-metadata.json
contains="com.example.bot.Outer$NestedHandler"
contains="java.util.Map"
contains="long[]"
contains="com.example.bot.BaseFilter"
notContains="com.example.bot.ObjectHandler"
*/

package com.example.bot

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.ArgParser
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.CommonHandler
import eu.vendeli.tgbot.annotations.Guard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.media.PhotoSize
import eu.vendeli.tgbot.interfaces.helper.Guard as GuardInterface

typealias Settings = Map<String, List<Int>>

// Guard, parser and filter as classes (not objects): created through their constructor.
class ClassGuard : GuardInterface {
    override suspend fun condition(user: User?, update: ProcessedUpdate, bot: TelegramBot): Boolean = true
}

class ClassArgParser : ArgumentParser {
    override fun parse(text: String): Map<String, String> = emptyMap()
}

// Indirect supertype: both BaseFilter and Filter must be registered.
open class BaseFilter : Filter {
    override suspend fun match(user: User?, update: ProcessedUpdate, bot: TelegramBot): Boolean = true
}

class ClassFilter : BaseFilter()

class Outer {
    // Nested class: JVM binary name Outer$NestedHandler. Constructor parameters cover a generic type,
    // a type alias, a primitive and a primitive array.
    class NestedHandler(
        private val photos: List<PhotoSize>,
        private val settings: Settings,
        private val level: Int,
        private val ids: LongArray,
    ) {
        @CommandHandler(["/nested"], scope = [UpdateType.MESSAGE])
        @Guard(ClassGuard::class)
        @ArgParser(ClassArgParser::class)
        suspend fun nested(user: User, bot: TelegramBot) {}
    }
}

// Handlers in an object are called directly, without ClassManager: nothing to register.
object ObjectHandler {
    @CommonHandler.Text(["hi"], filters = [ClassFilter::class], scope = [UpdateType.MESSAGE])
    suspend fun hi(user: User, bot: TelegramBot) {}
}
