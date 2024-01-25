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
