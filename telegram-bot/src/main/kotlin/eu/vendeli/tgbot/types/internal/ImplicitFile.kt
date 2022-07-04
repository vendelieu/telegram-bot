package eu.vendeli.tgbot.types.internal

sealed class ImplicitFile<T>(val file: T) {
    class FileId(fileId: String) : ImplicitFile<String>(fileId)
    class InputFile(file: ByteArray) : ImplicitFile<ByteArray>(file)
}
