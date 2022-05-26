package com.github.vendelieu.tgbot.types

enum class EncryptedPassportElementType(private val literal: String) {
    PersonalDetails("personal_details"), Passport("passport"), DriverLicence("driver_license"),
    IdentityCard("identity_card"), InternalPassport("internal_passport"), Address("address"),
    UtilityBill("utility_bill"), BankStatement("bank_statement"), RentalAgreement("rental_agreement"),
    PassportRegistration("passport_registration"), TemporaryRegistration("temporary_registration"),
    PhoneNumber("phone_number"), Email("email");

    override fun toString(): String = literal
}

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
