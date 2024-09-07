package eu.vendeli.webapps.utils

import eu.vendeli.webapps.core.EventType
import eu.vendeli.webapps.core.WebApp
import eu.vendeli.webapps.popup.PopupButton
import eu.vendeli.webapps.popup.PopupParams
import eu.vendeli.webapps.ui.ColorScheme

val WebApp.ColorScheme: ColorScheme
    get() = eu.vendeli.webapps.ui.ColorScheme.valueOf(colorScheme)

sealed interface EventHandler {
    val block: Function<*>

    value class ViewportChanged(override val block: ViewportChangedEventHandler) : EventHandler
    value class InvoiceClosed(override val block: InvoiceClosedEventHandler) : EventHandler
    value class PopupClosed(override val block: PopupClosedEventHandler) : EventHandler
    value class QRTextReceived(override val block: QRTextReceivedEventHandler) : EventHandler
    value class ClipboardTextReceived(override val block: ClipboardTextReceivedEventHandler) : EventHandler
    value class WriteAccessRequested(override val block: WriteAccessRequestedHandler) : EventHandler
    value class ContactRequested(override val block: ContactRequestedHandler) : EventHandler
    value class NoParams(override val block: NoParamsEventHandler) : EventHandler
}

internal inline fun WebApp.onEvent(type: EventType.ViewportChanged, eventHandler: EventHandler.ViewportChanged) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.InvoiceClosed, eventHandler: EventHandler.InvoiceClosed) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.PopupClosed, eventHandler: EventHandler.PopupClosed) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.QRTextReceived, eventHandler: EventHandler.QRTextReceived) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.ClipboardTextReceived, eventHandler: EventHandler.ClipboardTextReceived) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.WriteAccessRequested, eventHandler: EventHandler.WriteAccessRequested) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.ContactRequested, eventHandler: EventHandler.ContactRequested) {
    onEvent(type.name, eventHandler.block)
}

// no param events
internal inline fun WebApp.onEvent(type: EventType.ThemeChanged, eventHandler: EventHandler.NoParams) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.MainButtonClicked, eventHandler: EventHandler.NoParams) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.SecondaryButtonClicked, eventHandler: EventHandler.NoParams) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.BackButtonClicked, eventHandler: EventHandler.NoParams) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.SettingsButtonClicked, eventHandler: EventHandler.NoParams) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.ScanQrPopupClosed, eventHandler: EventHandler.NoParams) {
    onEvent(type.name, eventHandler.block)
}

fun WebApp.showPopup(
    message: String,
    title: String?,
    buttons: Array<PopupButton>,
    callback: ClosePopupCallback? = null,
) = showPopup(
    PopupParams(
        message = message,
        title = title,
        buttons = buttons,
    ),
    callback,
)

fun WebApp.showPopup(
    message: String,
    title: String?,
    firstButton: PopupButton,
    vararg otherButtons: PopupButton,
    callback: ClosePopupCallback? = null,
) = showPopup(
    PopupParams(
        message = message,
        title = title,
        buttons = arrayOf(firstButton, *otherButtons),
    ),
    callback,
)

var WebApp.requireClosingConfirmation
    get() = isClosingConfirmationEnabled
    set(value) {
        if (value) {
            enableClosingConfirmation()
        } else {
            disableClosingConfirmation()
        }
    }

internal inline fun WebApp.toggleClosingConfirmation() {
    requireClosingConfirmation = !requireClosingConfirmation
}
