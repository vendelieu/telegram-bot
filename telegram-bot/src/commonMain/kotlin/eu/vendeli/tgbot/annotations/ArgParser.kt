package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import kotlin.reflect.KClass

/**
 * The annotation used to set an argument parsing mechanism that handlers will pick.
 *
 * @property argParser Custom argument parser.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class ArgParser(
    val argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
)
