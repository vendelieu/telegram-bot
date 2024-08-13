package eu.vendeli.ktgram.config.env

import eu.vendeli.tgbot.TelegramBot

fun TelegramBot.Companion.fromEnvironment() = TelegramBot(EnvConfigLoader)
