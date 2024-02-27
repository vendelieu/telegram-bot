package eu.vendeli.webapps.utils

import eu.vendeli.webapps.core.EventType
import eu.vendeli.webapps.core.WebApp

/**
 * @return The callback which should be used in case you want to turn off events handling
 */
fun WebApp.onThemeChanged(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.ThemeChanged, EventHandler.NoParams(eventHandler))

/**
 * @return The callback which should be used in case you want to turn off events handling
 */
fun WebApp.onMainButtonClicked(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.MainButtonClicked, EventHandler.NoParams(eventHandler))

/**
 * @return The callback which should be used in case you want to turn off events handling
 */
fun WebApp.onViewportChanged(eventHandler: ViewportChangedEventHandler) =
    onEvent(EventType.ViewportChanged, EventHandler.ViewportChanged(eventHandler))

/**
 * @return The callback which should be used in case you want to turn off events handling
 */
fun WebApp.onBackButtonClicked(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.BackButtonClicked, EventHandler.NoParams(eventHandler))

/**
 * @return The callback which should be used in case you want to turn off events handling
 */
fun WebApp.onSettingsButtonClicked(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.SettingsButtonClicked, EventHandler.NoParams(eventHandler))

/**
 * @return The callback which should be used in case you want to turn off events handling
 */
fun WebApp.onInvoiceClosed(eventHandler: InvoiceClosedEventHandler) =
    onEvent(EventType.InvoiceClosed, EventHandler.InvoiceClosed(eventHandler))

/**
 * @return The callback which should be used in case you want to turn off events handling
 */
fun WebApp.onPopupClosed(eventHandler: PopupClosedEventHandler) =
    onEvent(EventType.PopupClosed, EventHandler.PopupClosed(eventHandler))

/**
 * @return The callback which should be used in case you want to turn off events handling
 */
fun WebApp.onQRTextReceived(eventHandler: QRTextReceivedEventHandler) =
    onEvent(EventType.QRTextReceived, EventHandler.QRTextReceived(eventHandler))

/**
 * @return The callback which should be used in case you want to turn off events handling
 */
fun WebApp.onClipboardTextReceived(eventHandler: ClipboardTextReceivedEventHandler) =
    onEvent(EventType.ClipboardTextReceived, EventHandler.ClipboardTextReceived(eventHandler))

/**
 * @return The callback which should be used in case you want to turn off events handling
 */
fun WebApp.onWriteAccessRequested(eventHandler: WriteAccessRequestedHandler) =
    onEvent(EventType.WriteAccessRequested, EventHandler.WriteAccessRequested(eventHandler))

/**
 * @return The callback which should be used in case you want to turn off events handling
 */
fun WebApp.onContactRequested(eventHandler: ContactRequestedHandler) =
    onEvent(EventType.ContactRequested, EventHandler.ContactRequested(eventHandler))
