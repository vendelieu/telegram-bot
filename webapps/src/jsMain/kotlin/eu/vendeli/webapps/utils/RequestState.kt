package eu.vendeli.webapps.utils

external interface WriteAccessRequestState {
    val status: String
}

@Suppress("EnumEntryName")
enum class WriteAccessRequestStatus {
    allowed,
    canceled,
}

val WriteAccessRequestState.Status: WriteAccessRequestStatus
    get() = WriteAccessRequestStatus.valueOf(status)

external interface ContactRequestState {
    val status: String
}

@Suppress("EnumEntryName")
enum class ContactRequestStatus {
    sent,
    canceled,
}

val ContactRequestState.Status: ContactRequestStatus
    get() = ContactRequestStatus.valueOf(status)
