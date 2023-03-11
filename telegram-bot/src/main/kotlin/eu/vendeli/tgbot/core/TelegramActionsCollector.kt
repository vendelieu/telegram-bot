package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.annotations.CallbackParam
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.types.internal.Actions
import eu.vendeli.tgbot.types.internal.Invocation
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import mu.KotlinLogging
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.lang.reflect.Parameter
import kotlin.reflect.jvm.kotlinFunction

/**
 * Collects commands and inputs before the program starts in a particular package by key annotations.
 *
 */
internal object TelegramActionsCollector {
    private val logger = KotlinLogging.logger {}

    private fun Array<Parameter>.getParameters() =
        filter { it.annotations.any { a -> a is CallbackParam } }
            .associate { p -> p.name to (p.annotations.first { it is CallbackParam } as CallbackParam).name }

    /**
     * Function that collects a list of actions that the bot will operate with.
     *
     * @param packageName the name of the package where the search will take place.
     * @return [Actions]
     */
    fun collect(packageName: String): Actions = with(
        Reflections(
            packageName,
            Scanners.MethodsAnnotated,
        ),
    ) {
        val commands = mutableMapOf<String, Invocation>()
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
                )
            }
        }

        getMethodsAnnotatedWith(InputHandler::class.java).forEach { m ->
            val annotation = (m.annotations.find { it is InputHandler } as InputHandler)
            annotation.value.forEach { v ->
                inputs[v] = Invocation(
                    clazz = m.declaringClass,
                    method = m,
                    namedParameters = m.parameters.getParameters(),
                    rateLimits = RateLimits(annotation.rateLimits.period, annotation.rateLimits.rate),
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
                )
            }
        }

        val unhandled = getMethodsAnnotatedWith(UnprocessedHandler::class.java).firstOrNull()?.let { m ->
            Invocation(clazz = m.declaringClass, method = m, rateLimits = RateLimits.NOT_LIMITED)
        }

        val totalActions = commands.size + inputs.size + updateHandlers.size + (if (unhandled != null) 1 else 0)
        logger.info {
            buildString {
                append("Found total $totalActions actions.\n")
                append(
                    commands.entries.joinToString("\n", "Commands:\n", "\n") {
                        "${it.key} -> ${it.value.method.kotlinFunction}"
                    },
                )
                append(
                    inputs.entries.joinToString("\n", "Inputs:\n", "\n") {
                        "${it.key} -> ${it.value.method.kotlinFunction}"
                    },
                )
                append(
                    updateHandlers.entries.joinToString("\n", "Update handlers:\n", "\n") {
                        "${it.key} -> ${it.value.method.kotlinFunction}"
                    },
                )
                append("Unhandled: ${unhandled?.method?.kotlinFunction}")
            }
        }

        return@with Actions(
            commands = commands,
            inputs = inputs,
            updateHandlers = updateHandlers,
            unhandled = unhandled,
        )
    }
}
