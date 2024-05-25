package eu.vendeli.webapps.popup

import kotlinx.js.JsPlainObject

@JsPlainObject
external interface PopupParams {
    var message: String
    var title: String?
    var buttons: Array<PopupButton>
}

fun PopupParams(
    message: String,
    firstButton: PopupButton,
    vararg otherButtons: PopupButton,
) = PopupParams(
    message = message,
    title = null,
    buttons = arrayOf(
        firstButton,
        *otherButtons,
    ),
)

fun PopupParams(
    title: String,
    message: String,
    firstButton: PopupButton,
    vararg otherButtons: PopupButton,
) = PopupParams(
    message = message,
    title = title,
    buttons = arrayOf(
        firstButton,
        *otherButtons,
    ),
)
