package eu.vendeli.tgbot.types.internal

import com.fasterxml.jackson.annotation.JsonValue
import java.io.File

sealed class ImplicitFile<T>(@JsonValue val file: T) {
    class FromString(file: String) : ImplicitFile<String>(file)
    class FromByteArray(file: ByteArray) : ImplicitFile<ByteArray>(file)
    class FromFile(file: File) : ImplicitFile<File>(file)
}
