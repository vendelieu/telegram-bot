package eu.vendeli.webapps.core

external interface WebAppUser {
    val id: Long

    @JsName("is_bot")
    val isBot: Boolean?

    @JsName("first_name")
    val firstName: String

    @JsName("last_name")
    val lastName: String?
    val username: String?

    @JsName("language_code")
    val languageCode: String?

    @JsName("is_premium")
    val isPremium: Boolean?

    @JsName("added_to_attachment_menu")
    val addedToAttachmentMenu: Boolean?

    @JsName("allows_write_to_pm")
    val allowsWriteToPM: Boolean?

    @JsName("photo_url")
    val photoUrl: String?
}
