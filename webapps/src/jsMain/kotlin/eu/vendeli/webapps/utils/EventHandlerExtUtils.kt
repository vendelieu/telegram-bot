package eu.vendeli.webapps.utils

import eu.vendeli.webapps.core.EventType
import eu.vendeli.webapps.core.WebApp

fun WebApp.onThemeChanged(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.ThemeChanged, EventHandler.NoParams(eventHandler))

fun WebApp.onMainButtonClicked(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.MainButtonClicked, EventHandler.NoParams(eventHandler))

fun WebApp.onViewportChanged(eventHandler: ViewportChangedEventHandler) =
    onEvent(EventType.ViewportChanged, EventHandler.ViewportChanged(eventHandler))

fun WebApp.onBackButtonClicked(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.BackButtonClicked, EventHandler.NoParams(eventHandler))

fun WebApp.onSettingsButtonClicked(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.SettingsButtonClicked, EventHandler.NoParams(eventHandler))

fun WebApp.onInvoiceClosed(eventHandler: InvoiceClosedEventHandler) =
    onEvent(EventType.InvoiceClosed, EventHandler.InvoiceClosed(eventHandler))

fun WebApp.onPopupClosed(eventHandler: PopupClosedEventHandler) =
    onEvent(EventType.PopupClosed, EventHandler.PopupClosed(eventHandler))

fun WebApp.onQRTextReceived(eventHandler: QRTextReceivedEventHandler) =
    onEvent(EventType.QRTextReceived, EventHandler.QRTextReceived(eventHandler))

fun WebApp.onScanQrPopupClosed(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.ScanQrPopupClosed, EventHandler.NoParams(eventHandler))

fun WebApp.onClipboardTextReceived(eventHandler: ClipboardTextReceivedEventHandler) =
    onEvent(EventType.ClipboardTextReceived, EventHandler.ClipboardTextReceived(eventHandler))

fun WebApp.onWriteAccessRequested(eventHandler: WriteAccessRequestedHandler) =
    onEvent(EventType.WriteAccessRequested, EventHandler.WriteAccessRequested(eventHandler))

fun WebApp.onContactRequested(eventHandler: ContactRequestedHandler) =
    onEvent(EventType.ContactRequested, EventHandler.ContactRequested(eventHandler))
