package eu.vendeli.webapps.invoice

external interface InvoiceClosedInfo {
    val url: String
    val status: String
}

val InvoiceClosedInfo.Status: InvoiceStatus
    get() = InvoiceStatus.valueOf(status)
