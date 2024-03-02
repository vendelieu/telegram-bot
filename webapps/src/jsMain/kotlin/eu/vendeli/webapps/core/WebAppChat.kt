package eu.vendeli.webapps.core

external interface WebAppChat {
    val id: Long
    val type: String
    val title: String
    val username: String?

    @JsName("photo_url")
    val photoUrl: String?
}
