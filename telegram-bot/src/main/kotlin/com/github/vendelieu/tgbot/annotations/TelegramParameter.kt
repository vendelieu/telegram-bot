package com.github.vendelieu.tgbot.annotations

/**
 * Because of the limitation of the telegram api in the length of callbacks,
 * this annotation allows you to map parameters by a specific name.
 *
 * @property name Parameter name representation in the callback.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class TelegramParameter(val name: String)
