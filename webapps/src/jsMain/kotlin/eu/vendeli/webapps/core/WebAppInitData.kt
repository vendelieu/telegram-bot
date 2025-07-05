package eu.vendeli.webapps.core

import kotlin.time.Instant

external interface WebAppInitData {
    @JsName("query_id")
    val queryId: String?
    val user: WebAppUser?
    val receiver: WebAppUser?

    @JsName("start_param")
    val startParam: String?

    @JsName("auth_date")
    val authDate: Instant

    @JsName("can_send_after")
    val canSendAfter: Instant
    val chat: WebAppChat
    val hash: String
}
