package eu.vendeli.webapps.core

sealed class EventType(
    val name: String,
) {
    data object ThemeChanged : EventType("themeChanged")
    data object ViewportChanged : EventType("viewportChanged")
    data object MainButtonClicked : EventType("mainButtonClicked")
    data object SecondaryButtonClicked : EventType("secondaryButtonClicked")
    data object BackButtonClicked : EventType("backButtonClicked")
    data object SettingsButtonClicked : EventType("settingsButtonClicked")
    data object InvoiceClosed : EventType("invoiceClosed")
    data object PopupClosed : EventType("popupClosed")
    data object QRTextReceived : EventType("qrTextReceived")
    data object ClipboardTextReceived : EventType("clipboardTextReceived")
    data object WriteAccessRequested : EventType("writeAccessRequested")
    data object ContactRequested : EventType("contactRequested")
    data object ScanQrPopupClosed : EventType("scanQrPopupClosed")
    data object Activated : EventType("activated")
    data object Deactivated : EventType("deactivated")
    data object SafeAreaChanged : EventType("safeAreaChanged")
    data object ContentSafeAreaChanged : EventType("contentSafeAreaChanged")
    data object FullscreenChanged : EventType("fullscreenChanged")
    data object FullscreenFailed : EventType("fullscreenFailed")
    data object HomeScreenAdded : EventType("homeScreenAdded")
    data object HomeScreenChecked : EventType("homeScreenChecked")
    data object AccelerometerStarted : EventType("accelerometerStarted")
    data object AccelerometerStopped : EventType("accelerometerStopped")
    data object AccelerometerChanged : EventType("accelerometerChanged")
    data object AccelerometerFailed : EventType("accelerometerFailed")
    data object DeviceOrientationStarted : EventType("deviceOrientationStarted")
    data object DeviceOrientationStopped : EventType("deviceOrientationStopped")
    data object DeviceOrientationChanged : EventType("deviceOrientationChanged")
    data object DeviceOrientationFailed : EventType("deviceOrientationFailed")
    data object GyroscopeStarted : EventType("gyroscopeStarted")
    data object GyroscopeStopped : EventType("gyroscopeStopped")
    data object GyroscopeChanged : EventType("gyroscopeChanged")
    data object GyroscopeFailed : EventType("gyroscopeFailed")
    data object LocationManagerUpdated : EventType("locationManagerUpdated")
    data object LocationRequested : EventType("locationRequested")
    data object ShareMessageSent : EventType("shareMessageSent")
    data object ShareMessageFailed : EventType("shareMessageFailed")
    data object EmojiStatusSet : EventType("emojiStatusSet")
    data object EmojiStatusFailed : EventType("emojiStatusFailed")
    data object EmojiStatusAccessRequested : EventType("emojiStatusAccessRequested")
    data object FileDownloadRequested : EventType("fileDownloadRequested")
}
