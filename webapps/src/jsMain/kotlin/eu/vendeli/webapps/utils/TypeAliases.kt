package eu.vendeli.webapps.utils

import eu.vendeli.webapps.core.WebApp
import eu.vendeli.webapps.invoice.InvoiceClosedInfo
import eu.vendeli.webapps.ui.ViewportChangedData

// Callbacks
typealias AlertCallback = () -> Unit
typealias ClosePopupCallback = (id: String) -> Unit
typealias ConfirmCallback = (confirmed: Boolean) -> Unit
typealias QRTextReceivedCallback = (String) -> Boolean
typealias ClipboardTextReceivedCallback = (String) -> Unit
typealias BiometricRequestAccessCallback = (accessGranted: Boolean) -> Unit
typealias BiometricAuthenticateCallback = (userAuthenticated: Boolean) -> Unit
typealias BiometricUpdateTokenCallback = (tokenUpdated: Boolean) -> Unit

// Handlers
typealias NoParamsEventHandler = WebApp.() -> Unit
typealias ViewportChangedEventHandler = WebApp.(ViewportChangedData) -> Unit
typealias InvoiceClosedEventHandler = WebApp.(InvoiceClosedInfo) -> Unit
typealias PopupClosedEventHandler = WebApp.(String?) -> Unit
typealias QRTextReceivedEventHandler = WebApp.(String) -> Boolean
typealias ClipboardTextReceivedEventHandler = WebApp.(String) -> Unit
typealias WriteAccessRequestedHandler = WebApp.(WriteAccessRequestState) -> Unit
typealias ContactRequestedHandler = WebApp.(ContactRequestState) -> Unit
