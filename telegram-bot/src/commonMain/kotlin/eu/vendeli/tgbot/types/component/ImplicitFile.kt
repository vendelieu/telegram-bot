package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.utils.common.safeCast
import eu.vendeli.tgbot.utils.serde.ToStringSerializer
import kotlinx.serialization.Serializable

@Serializable(ImplicitFile.Serde::class)
sealed class ImplicitFile {
    abstract val file: Any

    @Serializable
    class FileId(
        override val file: String,
    ) : ImplicitFile()

    @Serializable
    class FileData(
        override val file: InputFile,
    ) : ImplicitFile()

    companion object {
        @Suppress("FunctionName")
        @Deprecated("Use FileData instead", replaceWith = ReplaceWith("FileData(file)"))
        fun Inp(
            file: InputFile,
        ) = FileData(file)

        @Suppress("FunctionName")
        @Deprecated("Use FileId instead", replaceWith = ReplaceWith("FileId(file)"))
        fun Str(
            file: String,
        ) = FileId(file)
    }

    internal object Serde : ToStringSerializer<ImplicitFile>({ (this as? FileId)?.file.toString() })
}
