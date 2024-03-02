package eu.vendeli.webapps.popup

import eu.vendeli.webapps.utils.build

external interface PopupParams {
    var message: String
    var title: String?
    var buttons: Array<PopupButton>
}

fun PopupParams(
    message: String,
    title: String?,
    buttons: Array<PopupButton>,
) = build<PopupParams> {
    this.message = message
    this.buttons = buttons
    if (title != null) this.title = title
}

fun PopupParams(
    message: String,
    firstButton: PopupButton,
    vararg otherButtons: PopupButton,
) = PopupParams(
    message,
    null,
    arrayOf(
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
    message,
    title,
    arrayOf(
        firstButton,
        *otherButtons,
    ),
)
