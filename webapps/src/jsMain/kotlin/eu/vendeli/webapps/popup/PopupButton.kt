@file:Suppress("FunctionName")

package eu.vendeli.webapps.popup

import kotlinx.js.JsPlainObject
import kotlinx.serialization.Serializable

@JsPlainObject
external interface PopupButton {
    var id: String
    var type: PopupButtonType
    var text: String?
}

@Serializable
@Suppress("EnumEntryName")
enum class PopupButtonType {
    default, ok, close, cancel, destructive
}

fun DefaultPopupButton(
    id: String,
    text: String,
) = PopupButton(id = id, type = PopupButtonType.default, text = text)

fun OkPopupButton(
    id: String,
) = PopupButton(id = id, type = PopupButtonType.ok)

fun ClosePopupButton(
    id: String,
) = PopupButton(id = id, type = PopupButtonType.close)

fun CancelPopupButton(
    id: String,
) = PopupButton(id = id, type = PopupButtonType.cancel)

fun DestructivePopupButton(
    id: String,
    text: String,
) = PopupButton(id = id, type = PopupButtonType.destructive, text = text)
