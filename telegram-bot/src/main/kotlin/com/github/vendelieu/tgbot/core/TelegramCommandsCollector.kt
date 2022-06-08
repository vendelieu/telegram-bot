package com.github.vendelieu.tgbot.core

import com.github.vendelieu.tgbot.annotations.TelegramCommand
import com.github.vendelieu.tgbot.annotations.TelegramInput
import com.github.vendelieu.tgbot.annotations.TelegramParameter
import com.github.vendelieu.tgbot.annotations.TelegramUnhandled
import com.github.vendelieu.tgbot.types.internal.Actions
import com.github.vendelieu.tgbot.types.internal.Invocation
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.lang.reflect.Parameter

/**
 * Collects commands and inputs before the program starts in a particular package by key annotations.
 *
 * @constructor Create empty Telegram commands collector
 */
internal object TelegramCommandsCollector {
    private fun Array<Parameter>.getParameters() = filter { it.annotations.any { a -> a is TelegramParameter } }
        .associate { p -> p.name to (p.annotations.first { it is TelegramParameter } as TelegramParameter).name }

    /**
     * Function that collects a list of actions that the bot will operate with.
     *
     * @param packageName the name of the package where the search will take place.
     * @return [Actions]
     */
    fun collect(packageName: String): Actions = with(
        Reflections(
            packageName,
            Scanners.MethodsAnnotated
        )
    ) {
        val commands = mutableMapOf<String, Invocation>()
        val inputs = mutableMapOf<String, Invocation>()

        getMethodsAnnotatedWith(TelegramCommand::class.java).forEach { m ->
            (m.annotations.find { it is TelegramCommand } as TelegramCommand).value.forEach { v ->
                commands[v] = Invocation(
                    clazz = m.declaringClass, method = m, namedParameters = m.parameters.getParameters()
                )
            }
        }

        getMethodsAnnotatedWith(TelegramInput::class.java).forEach { m ->
            (m.annotations.find { it is TelegramInput } as TelegramInput).value.forEach { v ->
                inputs[v] = Invocation(
                    clazz = m.declaringClass, method = m, namedParameters = m.parameters.getParameters()
                )
            }
        }

        return@with Actions(
            commands = commands, inputs = inputs,
            unhandled =
            getMethodsAnnotatedWith(TelegramUnhandled::class.java).firstOrNull()?.let { m ->
                Invocation(clazz = m.declaringClass, method = m)
            }
        )
    }
}
