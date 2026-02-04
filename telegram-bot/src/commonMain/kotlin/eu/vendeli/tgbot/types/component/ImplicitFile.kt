package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.utils.common.safeCast
import eu.vendeli.tgbot.utils.serde.ToStringSerializer
import kotlinx.serialization.Serializable

@Serializable(ImplicitFile.Companion::class)
sealed class ImplicitFile {
    abstract val file: Any

    @Serializable
    class Str(
        override val file: String,
    ) : ImplicitFile()

    @Serializable
    class InpFile(
        override val file: InputFile,
    ) : ImplicitFile()

    internal companion object : ToStringSerializer<ImplicitFile>({ this.safeCast<Str>()?.file.toString() })
}
