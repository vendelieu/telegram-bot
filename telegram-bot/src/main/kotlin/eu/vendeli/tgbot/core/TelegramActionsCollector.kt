package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.ParamMapping
import eu.vendeli.tgbot.annotations.RegexCommandHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.types.internal.ActionType
import eu.vendeli.tgbot.types.internal.Actions
import eu.vendeli.tgbot.types.internal.Invocation
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import mu.KotlinLogging
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import java.lang.reflect.Parameter
import kotlin.reflect.jvm.kotlinFunction

/**
 * Collects commands and inputs before the program starts in a particular package by key annotations.
 *
 */
internal object TelegramActionsCollector {
    private val logger = KotlinLogging.logger {}

    private fun Array<Parameter>.getParameters() =
        filter { it.annotations.any { a -> a is ParamMapping } }
            .associate { p -> p.name to (p.annotations.first { it is ParamMapping } as ParamMapping).name }

    /**
     * Function that collects a list of actions that the bot will operate with.
     *
     * @param packageName the name of the package where the search will take place.
     * @return [Actions]
     */
    @Suppress("LongMethod")
    fun collect(packageName: String): Actions = with(
        Reflections(
            ConfigurationBuilder().forPackages(packageName),
            Scanners.MethodsAnnotated,
        ),
    ) {
        val commands = mutableMapOf<String, Invocation>()
        val regexCommands = mutableMapOf<Regex, Invocation>()
        val inputs = mutableMapOf<String, Invocation>()
        val updateHandlers = mutableMapOf<UpdateType, Invocation>()

        getMethodsAnnotatedWith(CommandHandler::class.java).forEach { m ->
            val annotation = (m.annotations.find { it is CommandHandler } as CommandHandler)
            annotation.value.forEach { v ->
                commands[v] = Invocation(
                    clazz = m.declaringClass,
                    method = m,
                    namedParameters = m.parameters.getParameters(),
                    rateLimits = RateLimits(annotation.rateLimits.period, annotation.rateLimits.rate),
                    scope = annotation.scope.toSet(),
                    type = ActionType.COMMAND,
                )
            }
        }

        getMethodsAnnotatedWith(RegexCommandHandler::class.java).forEach { m ->
            val annotation = (m.annotations.find { it is RegexCommandHandler } as RegexCommandHandler)
            regexCommands[annotation.value.toRegex()] = Invocation(
                clazz = m.declaringClass,
                method = m,
                namedParameters = m.parameters.getParameters(),
                rateLimits = RateLimits(annotation.rateLimits.period, annotation.rateLimits.rate),
                type = ActionType.REGEX_COMMAND,
            )
        }

        getMethodsAnnotatedWith(InputHandler::class.java).forEach { m ->
            val annotation = (m.annotations.find { it is InputHandler } as InputHandler)
            annotation.value.forEach { v ->
                inputs[v] = Invocation(
                    clazz = m.declaringClass,
                    method = m,
                    namedParameters = m.parameters.getParameters(),
                    rateLimits = RateLimits(annotation.rateLimits.period, annotation.rateLimits.rate),
                    type = ActionType.INPUT,
                )
            }
        }

        getMethodsAnnotatedWith(UpdateHandler::class.java).forEach { m ->
            val annotation = (m.annotations.find { it is UpdateHandler } as UpdateHandler)
            annotation.type.forEach { type ->
                updateHandlers[type] = Invocation(
                    clazz = m.declaringClass,
                    method = m,
                    rateLimits = RateLimits.NOT_LIMITED,
                    type = ActionType.TYPE_HANDLER,
                )
            }
        }

        val unhandled = getMethodsAnnotatedWith(UnprocessedHandler::class.java).firstOrNull()?.let { m ->
            Invocation(
                clazz = m.declaringClass,
                method = m,
                rateLimits = RateLimits.NOT_LIMITED,
                type = ActionType.UNPROCESSED_HANDLER,
            )
        }

        return@with Actions(
            commands = commands,
            regexCommands = regexCommands,
            inputs = inputs,
            updateHandlers = updateHandlers,
            unhandled = unhandled,
        ).log()
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun Actions.log(): Actions {
        val totalActions = commands.size + regexCommands.size +
            inputs.size + updateHandlers.size + (if (unhandled != null) 1 else 0)

        logger.info { "Found total $totalActions actions.\n" }
        logger.debug {
            buildString {
                appendLine("Commands:")
                append(
                    commands.entries.takeIf { it.isNotEmpty() }?.let { entry ->
                        entry.joinToString("\n", postfix = "\n") {
                            "${it.key} -> ${it.value.method.kotlinFunction}"
                        }
                    } ?: "None\n",
                )
                appendLine("Regex Commands:")
                append(
                    regexCommands.entries.takeIf { it.isNotEmpty() }?.let { entry ->
                        entry.joinToString("\n", postfix = "\n") {
                            "${it.key} -> ${it.value.method.kotlinFunction}"
                        }
                    } ?: "None\n",
                )
                appendLine("Inputs:")
                append(
                    regexCommands.entries.takeIf { it.isNotEmpty() }?.let { entry ->
                        entry.joinToString("\n", postfix = "\n") {
                            "${it.key} -> ${it.value.method.kotlinFunction}"
                        }
                    } ?: "None\n",
                )
                appendLine("Update handlers:")
                append(
                    updateHandlers.entries.takeIf { it.isNotEmpty() }?.let { entry ->
                        entry.joinToString("\n", postfix = "\n") {
                            "${it.key} -> ${it.value.method.kotlinFunction}"
                        }
                    } ?: "None\n",
                )
                append("Unhandled: ${unhandled?.method?.kotlinFunction ?: "None"}")
            }
        }

        return this
    }
}
