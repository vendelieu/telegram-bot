package eu.vendeli.tgbot.types.internal

import io.ktor.http.ContentType

class MediaData {
    lateinit var method: TgMethod
    lateinit var dataField: String
    lateinit var name: String
    lateinit var data: ByteArray
    var parameters: Map<String, Any?>? = null
    lateinit var contentType: ContentType
}
