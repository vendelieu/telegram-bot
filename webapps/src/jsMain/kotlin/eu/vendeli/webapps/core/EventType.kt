package eu.vendeli.webapps.core

sealed class EventType(val name: String) {
    data object ThemeChanged : EventType("themeChanged")
    data object ViewportChanged : EventType("viewportChanged")
    data object MainButtonClicked : EventType("mainButtonClicked")
    data object BackButtonClicked : EventType("backButtonClicked")
    data object SettingsButtonClicked : EventType("settingsButtonClicked")
    data object InvoiceClosed : EventType("invoiceClosed")
    data object PopupClosed : EventType("popupClosed")
    data object QRTextReceived : EventType("qrTextReceived")
    data object ClipboardTextReceived : EventType("clipboardTextReceived")
    data object WriteAccessRequested : EventType("writeAccessRequested")
    data object ContactRequested : EventType("contactRequested")
}
