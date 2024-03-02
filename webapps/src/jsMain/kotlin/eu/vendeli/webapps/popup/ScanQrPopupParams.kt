package eu.vendeli.webapps.popup

import eu.vendeli.webapps.utils.build


external interface ScanQrPopupParams {
    var text: String?
}

fun ScanQrPopupParams(
    text: String? = null
) = build<ScanQrPopupParams> {
    if (text != null) this.text = text
}
