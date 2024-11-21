package eu.vendeli.webapps.utils

import eu.vendeli.webapps.core.EventType
import eu.vendeli.webapps.core.WebApp

fun WebApp.onThemeChanged(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.ThemeChanged, EventHandler.NoParams(eventHandler))

fun WebApp.onMainButtonClicked(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.MainButtonClicked, EventHandler.NoParams(eventHandler))

fun WebApp.onSecondaryButtonClicked(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.SecondaryButtonClicked, EventHandler.NoParams(eventHandler))

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

fun WebApp.onActivated(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.Activated, EventHandler.Activated(eventHandler))

fun WebApp.onDeactivated(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.Deactivated, EventHandler.Deactivated(eventHandler))

fun WebApp.onSafeAreaChanged(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.SafeAreaChanged, EventHandler.SafeAreaChanged(eventHandler))

fun WebApp.onContentSafeAreaChanged(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.ContentSafeAreaChanged, EventHandler.ContentSafeAreaChanged(eventHandler))

fun WebApp.onFullscreenChanged(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.FullscreenChanged, EventHandler.FullscreenChanged(eventHandler))

fun WebApp.onHomeScreenAdded(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.HomeScreenAdded, EventHandler.HomeScreenAdded(eventHandler))

fun WebApp.onAccelerometerStarted(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.AccelerometerStarted, EventHandler.AccelerometerStarted(eventHandler))

fun WebApp.onAccelerometerStopped(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.AccelerometerStopped, EventHandler.AccelerometerStopped(eventHandler))

fun WebApp.onAccelerometerChanged(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.AccelerometerChanged, EventHandler.AccelerometerChanged(eventHandler))

fun WebApp.onDeviceOrientationStarted(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.DeviceOrientationStarted, EventHandler.DeviceOrientationStarted(eventHandler))

fun WebApp.onDeviceOrientationStopped(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.DeviceOrientationStopped, EventHandler.DeviceOrientationStopped(eventHandler))

fun WebApp.onDeviceOrientationChanged(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.DeviceOrientationChanged, EventHandler.DeviceOrientationChanged(eventHandler))

fun WebApp.onGyroscopeStarted(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.GyroscopeStarted, EventHandler.GyroscopeStarted(eventHandler))

fun WebApp.onGyroscopeStopped(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.GyroscopeStopped, EventHandler.GyroscopeStopped(eventHandler))

fun WebApp.onGyroscopeChanged(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.GyroscopeChanged, EventHandler.GyroscopeChanged(eventHandler))

fun WebApp.onLocationManagerUpdated(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.LocationManagerUpdated, EventHandler.LocationManagerUpdated(eventHandler))

fun WebApp.onShareMessageSent(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.ShareMessageSent, EventHandler.ShareMessageSent(eventHandler))

fun WebApp.onEmojiStatusSet(eventHandler: NoParamsEventHandler) =
    onEvent(EventType.EmojiStatusSet, EventHandler.EmojiStatusSet(eventHandler))

fun WebApp.onFullscreenFailed(eventHandler: FullscreenFailedEventHandler) =
    onEvent(EventType.FullscreenFailed, EventHandler.FullscreenFailed(eventHandler))

fun WebApp.onHomeScreenChecked(eventHandler: HomeScreenCheckedEventHandler) =
    onEvent(EventType.HomeScreenChecked, EventHandler.HomeScreenChecked(eventHandler))

fun WebApp.onAccelerometerFailed(eventHandler: AccelerometerFailedEventHandler) =
    onEvent(EventType.AccelerometerFailed, EventHandler.AccelerometerFailed(eventHandler))

fun WebApp.onDeviceOrientationFailed(eventHandler: DeviceOrientationFailedEventHandler) =
    onEvent(EventType.DeviceOrientationFailed, EventHandler.DeviceOrientationFailed(eventHandler))

fun WebApp.onGyroscopeFailed(eventHandler: GyroscopeFailedEventHandler) =
    onEvent(EventType.GyroscopeFailed, EventHandler.GyroscopeFailed(eventHandler))

fun WebApp.onLocationRequested(eventHandler: LocationRequestedEventHandler) =
    onEvent(EventType.LocationRequested, EventHandler.LocationRequested(eventHandler))

fun WebApp.onEmojiStatusAccessRequested(eventHandler: EmojiStatusAccessRequestedEventHandler) =
    onEvent(EventType.EmojiStatusAccessRequested, EventHandler.EmojiStatusAccessRequested(eventHandler))

fun WebApp.onShareMessageFailed(eventHandler: ShareMessageFailedEventHandler) =
    onEvent(EventType.ShareMessageFailed, EventHandler.ShareMessageFailed(eventHandler))

fun WebApp.onEmojiStatusFailed(eventHandler: EmojiStatusFailedEventHandler) =
    onEvent(EventType.EmojiStatusFailed, EventHandler.EmojiStatusFailed(eventHandler))

fun WebApp.onFileDownloadRequested(eventHandler: FileDownloadRequestedEventHandler) =
    onEvent(EventType.FileDownloadRequested, EventHandler.FileDownloadRequested(eventHandler))
