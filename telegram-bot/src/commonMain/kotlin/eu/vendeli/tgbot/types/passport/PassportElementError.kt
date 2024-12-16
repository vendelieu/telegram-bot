package eu.vendeli.tgbot.types.passport

import eu.vendeli.tgbot.annotations.internal.TgAPI
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.serializer

@Serializable
@JsonClassDiscriminator("source")
@OptIn(ExperimentalSerializationApi::class)
sealed class PassportElementError {
    @OptIn(InternalSerializationApi::class)
    val source: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("data")
    data class DataField(
        val type: EncryptedPassportElementType,
        val fieldName: String,
        val dataHash: String,
        val message: String,
    ) : PassportElementError()

    @Serializable
    @SerialName("front_side")
    data class FrontSide(
        val type: EncryptedPassportElementType,
        val fileHash: String,
        val message: String,
    ) : PassportElementError()

    @Serializable
    @SerialName("reverse_side")
    data class ReverseSide(
        val type: EncryptedPassportElementType,
        val fileHash: String,
        val message: String,
    ) : PassportElementError()

    @Serializable
    @SerialName("selfie")
    data class Selfie(
        val type: EncryptedPassportElementType,
        val fileHash: String,
        val message: String,
    ) : PassportElementError()

    @Serializable
    @SerialName("file")
    @TgAPI.Name("PassportElementErrorFile")
    data class FileElement(
        val type: EncryptedPassportElementType,
        val fileHash: String,
        val message: String,
    ) : PassportElementError()

    @Serializable
    @SerialName("files")
    data class Files(
        val type: EncryptedPassportElementType,
        val fileHashes: List<String>,
        val message: String,
    ) : PassportElementError()

    @Serializable
    @SerialName("translation_file")
    data class TranslationFile(
        val type: EncryptedPassportElementType,
        val fileHash: String,
        val message: String,
    ) : PassportElementError()

    @Serializable
    @SerialName("translation_files")
    data class TranslationFiles(
        val type: EncryptedPassportElementType,
        val fileHashes: List<String>,
        val message: String,
    ) : PassportElementError()

    @Serializable
    @SerialName("unspecified")
    data class Unspecified(
        val type: EncryptedPassportElementType,
        val elementHash: String,
        val message: String,
    ) : PassportElementError()
}
