package eu.vendeli.webapps.user_interaction

external interface DownloadFileParams {
    val url: String

    @JsName("file_name")
    val fileName: String
}
