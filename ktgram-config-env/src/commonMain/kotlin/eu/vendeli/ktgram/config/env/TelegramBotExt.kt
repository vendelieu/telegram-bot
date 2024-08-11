package eu.vendeli.ktgram.config.env

import eu.vendeli.tgbot.TelegramBot

fun TelegramBot.fromEnvironment() = TelegramBot(EnvConfigLoader)
