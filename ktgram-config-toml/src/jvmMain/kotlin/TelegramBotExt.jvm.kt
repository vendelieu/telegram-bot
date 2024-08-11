import eu.vendeli.ktgram.config.env.TomlConfigLoader
import eu.vendeli.tgbot.TelegramBot
import java.nio.charset.Charset

fun TelegramBot.fromToml(fileName: String = "bot.toml") =
    TelegramBot(
        TomlConfigLoader(
            javaClass.classLoader.getResourceAsStream(fileName)!!.readAllBytes().toString(Charset.defaultCharset()),
        ),
    )
