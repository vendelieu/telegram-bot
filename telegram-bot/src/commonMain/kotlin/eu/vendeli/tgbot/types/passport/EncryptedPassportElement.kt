package eu.vendeli.tgbot.types.passport

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EncryptedPassportElementType {
    @SerialName("personal_details")
    PersonalDetails,

    @SerialName("passport")
    Passport,

    @SerialName("driver_license")
    DriverLicence,

    @SerialName("identity_card")
    IdentityCard,

    @SerialName("internal_passport")
    InternalPassport,

    @SerialName("address")
    Address,

    @SerialName("utility_bill")
    UtilityBill,

    @SerialName("bank_statement")
    BankStatement,

    @SerialName("rental_agreement")
    RentalAgreement,

    @SerialName("passport_registration")
    PassportRegistration,

    @SerialName("temporary_registration")
    TemporaryRegistration,

    @SerialName("phone_number")
    PhoneNumber,

    @SerialName("email")
    Email,
}

/**
 * Describes documents or other Telegram Passport elements shared with the bot by the user.
 * Api reference: https://core.telegram.org/bots/api#encryptedpassportelement
 * @property type Element type. One of "personal_details", "passport", "driver_license", "identity_card", "internal_passport", "address", "utility_bill", "bank_statement", "rental_agreement", "passport_registration", "temporary_registration", "phone_number", "email".
 * @property data Optional. Base64-encoded encrypted Telegram Passport element data provided by the user, available for "personal_details", "passport", "driver_license", "identity_card", "internal_passport" and "address" types. Can be decrypted and verified using the accompanying EncryptedCredentials.
 * @property phoneNumber Optional. User's verified phone number, available only for "phone_number" type
 * @property email Optional. User's verified email address, available only for "email" type
 * @property files Optional. Array of encrypted files with documents provided by the user, available for "utility_bill", "bank_statement", "rental_agreement", "passport_registration" and "temporary_registration" types. Files can be decrypted and verified using the accompanying EncryptedCredentials.
 * @property frontSide Optional. Encrypted file with the front side of the document, provided by the user. Available for "passport", "driver_license", "identity_card" and "internal_passport". The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @property reverseSide Optional. Encrypted file with the reverse side of the document, provided by the user. Available for "driver_license" and "identity_card". The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @property selfie Optional. Encrypted file with the selfie of the user holding a document, provided by the user; available for "passport", "driver_license", "identity_card" and "internal_passport". The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @property translation Optional. Array of encrypted files with translated versions of documents provided by the user. Available if requested for "passport", "driver_license", "identity_card", "internal_passport", "utility_bill", "bank_statement", "rental_agreement", "passport_registration" and "temporary_registration" types. Files can be decrypted and verified using the accompanying EncryptedCredentials.
 * @property hash Base64-encoded element hash for using in PassportElementErrorUnspecified
*/
@Serializable
data class EncryptedPassportElement(
    val type: EncryptedPassportElementType,
    val data: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val files: List<PassportFile>? = null,
    val frontSide: PassportFile? = null,
    val reverseSide: PassportFile? = null,
    val selfie: PassportFile? = null,
    val translation: List<PassportFile>? = null,
    val hash: String,
)
