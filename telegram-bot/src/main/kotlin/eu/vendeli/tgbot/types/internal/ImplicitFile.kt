package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.utils.serde.ToStringSerializer
import kotlinx.serialization.Serializable

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
@Serializable(ImplicitFile.Str.Companion::class)
sealed class ImplicitFile {
    abstract val file: Any

    @Serializable(Str.Companion::class)
    class Str(override val file: String) : ImplicitFile() {
        internal companion object : ToStringSerializer<Str>({ file })
    }

    @Serializable
    class InpFile(override val file: InputFile) : ImplicitFile()
}
