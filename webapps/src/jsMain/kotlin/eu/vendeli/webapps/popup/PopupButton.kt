@file:Suppress("FunctionName")

package eu.vendeli.webapps.popup

import eu.vendeli.webapps.utils.build
import kotlinx.serialization.Serializable

external interface PopupButton {
    var id: String
    var type: PopupButtonType
    var text: String?
}

fun PopupButton(
    id: String,
    type: PopupButtonType,
    text: String? = null,
) = build<PopupButton> {
    this.id = id
    this.type = type
    if (text != null) this.text = text
}

@Serializable
@Suppress("EnumEntryName")
enum class PopupButtonType {
    default, ok, close, cancel, destructive
}

fun DefaultPopupButton(
    id: String,
    text: String,
) = PopupButton(id, PopupButtonType.default, text)

fun OkPopupButton(
    id: String,
) = PopupButton(id, PopupButtonType.ok)

fun ClosePopupButton(
    id: String,
) = PopupButton(id, PopupButtonType.close)

fun CancelPopupButton(
    id: String,
) = PopupButton(id, PopupButtonType.cancel)

fun DestructivePopupButton(
    id: String,
    text: String,
) = PopupButton(id, PopupButtonType.destructive, text)
