package com.github.vendelieu.tgbot.types

sealed class PassportElementError(val source: String)

data class DataField(
    val type: EncryptedPassportElementType,
    val fieldName: String,
    val dataHash: String,
    val message: String
) : PassportElementError("data")

data class FrontSide(
    val type: EncryptedPassportElementType,
    val fileHash: String,
    val message: String
) : PassportElementError("front_side")

data class ReverseSide(
    val type: EncryptedPassportElementType,
    val fileHash: String,
    val message: String
) : PassportElementError("reverse_side")

data class Selfie(
    val type: EncryptedPassportElementType,
    val fileHash: String,
    val message: String
) : PassportElementError("selfie")

data class FileElement(
    val type: EncryptedPassportElementType,
    val fileHash: String,
    val message: String
) : PassportElementError("file")

data class Files(
    val type: EncryptedPassportElementType,
    val fileHashes: List<String>,
    val message: String
) : PassportElementError("files")

data class TranslationFile(
    val type: EncryptedPassportElementType,
    val fileHash: String,
    val message: String
) : PassportElementError("translation_file")

data class TranslationFiles(
    val type: EncryptedPassportElementType,
    val fileHashes: List<String>,
    val message: String
) : PassportElementError("translation_files")

data class Unspecified(
    val type: EncryptedPassportElementType,
    val elementHash: String,
    val message: String
) : PassportElementError("unspecified")
