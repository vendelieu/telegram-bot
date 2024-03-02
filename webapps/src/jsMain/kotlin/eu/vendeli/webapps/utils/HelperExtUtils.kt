package eu.vendeli.webapps.utils

import eu.vendeli.webapps.core.webApp

inline fun conditionalAction(
    sendDataAction: () -> String,
    answerWebAppQuery: (String) -> Unit,
    condition: () -> Boolean = { webApp.initDataUnsafe.queryId == null },
) {
    if (condition()) webApp.sendData(sendDataAction())
    else webApp.initDataUnsafe.queryId?.let(answerWebAppQuery)
}

@Suppress("UnusedReceiverParameter")
@JsName("decodeURLQueryComponent")
fun String.decodeURLQueryComponent(): String {
    return js("decodeURIComponent(this)").unsafeCast<String>()
}

const val BG_COLOR = "bg_color"
const val SECONDARY_BG_COLOR = "secondary_bg_color"
