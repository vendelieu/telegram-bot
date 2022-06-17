package eu.vendeli.tgbot.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "source",
    defaultImpl = PassportElementError.Unspecified::class
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PassportElementError.DataField::class, name = "data"),
    JsonSubTypes.Type(value = PassportElementError.FrontSide::class, name = "front_side"),
    JsonSubTypes.Type(value = PassportElementError.ReverseSide::class, name = "reverse_side"),
    JsonSubTypes.Type(value = PassportElementError.Selfie::class, name = "selfie"),
    JsonSubTypes.Type(value = PassportElementError.FileElement::class, name = "file"),
    JsonSubTypes.Type(value = PassportElementError.Files::class, name = "files"),
    JsonSubTypes.Type(value = PassportElementError.TranslationFile::class, name = "translation_file"),
    JsonSubTypes.Type(value = PassportElementError.TranslationFiles::class, name = "translation_files"),
    JsonSubTypes.Type(value = PassportElementError.Unspecified::class, name = "unspecified"),
)
sealed class PassportElementError(val source: String) {
    data class DataField(
        val type: EncryptedPassportElementType,
        val fieldName: String,
        val dataHash: String,
        val message: String,
    ) : PassportElementError("data")

    data class FrontSide(
        val type: EncryptedPassportElementType,
        val fileHash: String,
        val message: String,
    ) : PassportElementError("front_side")

    data class ReverseSide(
        val type: EncryptedPassportElementType,
        val fileHash: String,
        val message: String,
    ) : PassportElementError("reverse_side")

    data class Selfie(
        val type: EncryptedPassportElementType,
        val fileHash: String,
        val message: String,
    ) : PassportElementError("selfie")

    data class FileElement(
        val type: EncryptedPassportElementType,
        val fileHash: String,
        val message: String,
    ) : PassportElementError("file")

    data class Files(
        val type: EncryptedPassportElementType,
        val fileHashes: List<String>,
        val message: String,
    ) : PassportElementError("files")

    data class TranslationFile(
        val type: EncryptedPassportElementType,
        val fileHash: String,
        val message: String,
    ) : PassportElementError("translation_file")

    data class TranslationFiles(
        val type: EncryptedPassportElementType,
        val fileHashes: List<String>,
        val message: String,
    ) : PassportElementError("translation_files")

    data class Unspecified(
        val type: EncryptedPassportElementType,
        val elementHash: String,
        val message: String,
    ) : PassportElementError("unspecified")
}
