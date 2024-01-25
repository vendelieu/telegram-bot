package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.utils.serde.GenericValueSerializer
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.serialization.Serializable

@Serializable
sealed class ImplicitFile {
    abstract val file: Any

    @Serializable
    class Str(override val file: String) : ImplicitFile()

    @Serializable
    class InpFile(override val file: InputFile) : ImplicitFile()

    internal companion object : GenericValueSerializer<ImplicitFile>({ file.toJsonElement() })
}
