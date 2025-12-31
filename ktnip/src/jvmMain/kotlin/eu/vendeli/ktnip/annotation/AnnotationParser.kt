package eu.vendeli.ktnip.annotation

import com.google.devtools.ksp.isDefault
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import eu.vendeli.ktnip.utils.cast
import eu.vendeli.ktnip.utils.safeCast
import eu.vendeli.ktnip.utils.toRateLimits
import eu.vendeli.tgbot.annotations.ArgParser
import eu.vendeli.tgbot.annotations.CommonHandler
import eu.vendeli.tgbot.annotations.Guard
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.configuration.RateLimits

/**
 * Parses annotation arguments into typed values.
 * Centralizes annotation parsing logic.
 */
object AnnotationParser {
    /**
     * Parses CommandHandler annotation arguments.
     */
    fun parseCommandHandler(
        arguments: List<KSValueArgument>,
        isCallbackQ: Boolean,
    ): CommandHandlerData {
        val commands = parseValueList(arguments)
        val scope = parseScopes(arguments) ?: if (isCallbackQ) {
            listOf(UpdateType.CALLBACK_QUERY)
        } else {
            listOf(UpdateType.MESSAGE)
        }
        val autoAnswer = arguments.firstOrNull {
            it.name?.asString() == "autoAnswer"
        }?.takeIf { !it.isDefault() }?.value?.safeCast<Boolean>()

        return CommandHandlerData(commands, scope, autoAnswer)
    }

    /**
     * Parses InputHandler annotation arguments.
     */
    fun parseInputHandler(arguments: List<KSValueArgument>): List<String> =
        parseValueList(arguments)

    /**
     * Parses UpdateHandler annotation arguments.
     */
    fun parseUpdateHandler(arguments: List<KSValueArgument>): List<UpdateType> =
        arguments.first().value.cast<List<*>>().map { i ->
            when (i) {
                is KSType -> i.declaration.toString()
                is KSClassDeclaration -> i.simpleName.getShortName()
                else -> throw IllegalStateException("Unknown type $i")
            }.let { UpdateType.valueOf(it) }
        }

    /**
     * Parses CommonHandler.Text or CommonHandler.Regex arguments.
     */
    fun parseCommonHandler(arguments: List<KSValueArgument>): CommonHandlerData {
        val value = arguments.first { it.name?.asString() == CommonHandler.Text::value.name }.value
        val filters = arguments.firstOrNull {
            it.name?.asString() == CommonHandler.Text::filters.name
        }?.value?.safeCast<List<KSType>>()?.map {
            it.declaration.qualifiedName!!.asString()
        } ?: emptyList()

        val priority = arguments.firstOrNull {
            it.name?.asString() == CommonHandler.Text::priority.name
        }?.value?.safeCast<Int>() ?: 0

        val scope = parseScopes(arguments) ?: listOf(UpdateType.MESSAGE)

        val regexOptions = if (value is String) {
            parseRegexOptions(arguments)
        } else {
            emptyList()
        }

        return CommonHandlerData(value!!, filters, priority, scope, regexOptions)
    }

    /**
     * Parses Guard annotation.
     */
    fun parseGuard(arguments: List<KSValueArgument>): String =
        arguments.firstOrNull { it.name?.asString() == Guard::guard.name }
            ?.value?.safeCast<KSType>()
            ?.let { it.declaration.qualifiedName!!.asString() }
            ?: DefaultGuard::class.qualifiedName!!

    /**
     * Parses ArgParser annotation.
     */
    fun parseArgParser(arguments: List<KSValueArgument>): String =
        arguments.firstOrNull { it.name?.asString() == ArgParser::argParser.name }
            ?.value?.safeCast<KSType>()
            ?.let { it.declaration.qualifiedName!!.asString() }
            ?: DefaultArgParser::class.qualifiedName!!

    /**
     * Parses RateLimits annotation.
     */
    fun parseRateLimits(arguments: List<KSValueArgument>): RateLimits =
        ((arguments.firstOrNull()?.value?.safeCast<Long>() ?: 0) to
            (arguments.lastOrNull()?.value?.safeCast<Long>() ?: 0)).toRateLimits()

    /**
     * Parses scope argument (list of UpdateTypes).
     */
    private fun parseScopes(arguments: List<KSValueArgument>): List<UpdateType>? =
        arguments.firstOrNull {
            it.name?.asString() == "scope"
        }?.value?.safeCast<List<*>>()?.map {
            val value = when (it) {
                is KSType -> it.declaration.toString()
                is KSClassDeclaration -> it.simpleName.getShortName()
                else -> throw IllegalStateException("Unknown type $it")
            }
            UpdateType.valueOf(value)
        }

    /**
     * Parses value list argument.
     */
    private fun parseValueList(arguments: List<KSValueArgument>): List<String> =
        arguments.first { it.name?.asString() == "value" }.value.cast()

    /**
     * Parses regex options for CommonHandler.Regex.
     */
    private fun parseRegexOptions(arguments: List<KSValueArgument>): List<RegexOption> =
        arguments.firstOrNull {
            it.name?.asString() == CommonHandler.Regex::options.name
        }?.value?.safeCast<List<*>>()?.map { i ->
            when (i) {
                is KSType -> i.declaration.toString()
                is KSClassDeclaration -> i.simpleName.getShortName()
                else -> throw IllegalStateException("Unknown type $i")
            }.let { RegexOption.valueOf(it) }
        } ?: emptyList()
}

/**
 * Data class for parsed CommandHandler annotation.
 */
data class CommandHandlerData(
    val commands: List<String>,
    val scope: List<UpdateType>,
    val autoAnswer: Boolean?,
)

/**
 * Data class for parsed CommonHandler annotation.
 */
data class CommonHandlerData(
    val value: Any, // String or List<String>
    val filters: List<String>,
    val priority: Int,
    val scope: List<UpdateType>,
    val regexOptions: List<RegexOption>,
)
