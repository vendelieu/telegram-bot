package eu.vendeli.ktgram.config.env

import eu.vendeli.tgbot.TelegramBot

fun TelegramBot.fromToml(toml: String) = TelegramBot(TomlConfigLoader(toml))
