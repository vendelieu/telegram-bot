@file:Suppress("NOTHING_TO_INLINE")

package eu.vendeli.webapps.utils

import eu.vendeli.webapps.core.EventType
import eu.vendeli.webapps.core.WebApp
import eu.vendeli.webapps.popup.PopupButton
import eu.vendeli.webapps.popup.PopupParams
import eu.vendeli.webapps.ui.ColorScheme

val WebApp.ColorScheme: ColorScheme
    get() = eu.vendeli.webapps.ui.ColorScheme
        .valueOf(colorScheme)

sealed interface EventHandler {
    val block: Function<*>

    value class ViewportChanged(
        override val block: ViewportChangedEventHandler,
    ) : EventHandler
    value class InvoiceClosed(
        override val block: InvoiceClosedEventHandler,
    ) : EventHandler
    value class PopupClosed(
        override val block: PopupClosedEventHandler,
    ) : EventHandler
    value class QRTextReceived(
        override val block: QRTextReceivedEventHandler,
    ) : EventHandler
    value class ClipboardTextReceived(
        override val block: ClipboardTextReceivedEventHandler,
    ) : EventHandler
    value class WriteAccessRequested(
        override val block: WriteAccessRequestedHandler,
    ) : EventHandler
    value class ContactRequested(
        override val block: ContactRequestedHandler,
    ) : EventHandler
    value class NoParams(
        override val block: NoParamsEventHandler,
    ) : EventHandler
    value class Activated(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class Deactivated(
        override val block: NoParamsEventHandler,
    ) : EventHandler
    value class SafeAreaChanged(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class ContentSafeAreaChanged(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class FullscreenChanged(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class FullscreenFailed(
        override val block: FullscreenFailedEventHandler,
    ) : EventHandler

    value class HomeScreenAdded(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class HomeScreenChecked(
        override val block: HomeScreenCheckedEventHandler,
    ) : EventHandler

    value class AccelerometerStarted(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class AccelerometerStopped(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class AccelerometerChanged(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class AccelerometerFailed(
        override val block: AccelerometerFailedEventHandler,
    ) : EventHandler

    value class DeviceOrientationStarted(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class DeviceOrientationStopped(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class DeviceOrientationChanged(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class DeviceOrientationFailed(
        override val block: DeviceOrientationFailedEventHandler,
    ) : EventHandler

    value class GyroscopeStarted(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class GyroscopeStopped(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class GyroscopeChanged(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class GyroscopeFailed(
        override val block: GyroscopeFailedEventHandler,
    ) : EventHandler

    value class LocationManagerUpdated(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class LocationRequested(
        override val block: LocationRequestedEventHandler,
    ) : EventHandler

    value class ShareMessageSent(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class ShareMessageFailed(
        override val block: ShareMessageFailedEventHandler,
    ) : EventHandler

    value class EmojiStatusSet(
        override val block: NoParamsEventHandler,
    ) : EventHandler

    value class EmojiStatusFailed(
        override val block: EmojiStatusFailedEventHandler,
    ) : EventHandler

    value class EmojiStatusAccessRequested(
        override val block: EmojiStatusAccessRequestedEventHandler,
    ) : EventHandler

    value class FileDownloadRequested(
        override val block: FileDownloadRequestedEventHandler,
    ) : EventHandler
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

internal inline fun WebApp.onEvent(
    type: EventType.ClipboardTextReceived,
    eventHandler: EventHandler.ClipboardTextReceived,
) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(
    type: EventType.WriteAccessRequested,
    eventHandler: EventHandler.WriteAccessRequested,
) {
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

internal inline fun WebApp.onEvent(type: EventType.Activated, eventHandler: EventHandler.Activated) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.Deactivated, eventHandler: EventHandler.Deactivated) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.SafeAreaChanged, eventHandler: EventHandler.SafeAreaChanged) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.ContentSafeAreaChanged, eventHandler: EventHandler.ContentSafeAreaChanged) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.FullscreenChanged, eventHandler: EventHandler.FullscreenChanged) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.FullscreenFailed, eventHandler: EventHandler.FullscreenFailed) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.HomeScreenAdded, eventHandler: EventHandler.HomeScreenAdded) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.HomeScreenChecked, eventHandler: EventHandler.HomeScreenChecked) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.AccelerometerStarted, eventHandler: EventHandler.AccelerometerStarted) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.AccelerometerStopped, eventHandler: EventHandler.AccelerometerStopped) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.AccelerometerChanged, eventHandler: EventHandler.AccelerometerChanged) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.AccelerometerFailed, eventHandler: EventHandler.AccelerometerFailed) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.DeviceOrientationStarted, eventHandler: EventHandler.DeviceOrientationStarted) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.DeviceOrientationStopped, eventHandler: EventHandler.DeviceOrientationStopped) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.DeviceOrientationChanged, eventHandler: EventHandler.DeviceOrientationChanged) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.DeviceOrientationFailed, eventHandler: EventHandler.DeviceOrientationFailed) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.GyroscopeStarted, eventHandler: EventHandler.GyroscopeStarted) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.GyroscopeStopped, eventHandler: EventHandler.GyroscopeStopped) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.GyroscopeChanged, eventHandler: EventHandler.GyroscopeChanged) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.GyroscopeFailed, eventHandler: EventHandler.GyroscopeFailed) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.LocationManagerUpdated, eventHandler: EventHandler.LocationManagerUpdated) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.LocationRequested, eventHandler: EventHandler.LocationRequested) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.ShareMessageSent, eventHandler: EventHandler.ShareMessageSent) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.ShareMessageFailed, eventHandler: EventHandler.ShareMessageFailed) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.EmojiStatusSet, eventHandler: EventHandler.EmojiStatusSet) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.EmojiStatusFailed, eventHandler: EventHandler.EmojiStatusFailed) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.EmojiStatusAccessRequested, eventHandler: EventHandler.EmojiStatusAccessRequested) {
    onEvent(type.name, eventHandler.block)
}

internal inline fun WebApp.onEvent(type: EventType.FileDownloadRequested, eventHandler: EventHandler.FileDownloadRequested) {
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
