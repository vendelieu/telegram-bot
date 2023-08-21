package eu.vendeli.tgbot.types.internal

import com.fasterxml.jackson.annotation.JsonValue

sealed class ImplicitFile<T : Any>(@JsonValue val file: T) {
    class Str(file: String) : ImplicitFile<String>(file)
    class InpFile(file: InputFile) : ImplicitFile<InputFile>(file)
}
