package eu.vendeli.webapps.utils
import eu.vendeli.webapps.core.WebApp
import eu.vendeli.webapps.invoice.InvoiceClosedInfo
import eu.vendeli.webapps.sensors.LocationData
import eu.vendeli.webapps.ui.ViewportChangedData
import eu.vendeli.webapps.user_interaction.AccelerometerFailedInfo
import eu.vendeli.webapps.user_interaction.DeviceOrientationFailedInfo
import eu.vendeli.webapps.user_interaction.EmojiStatusAccessRequestedInfo
import eu.vendeli.webapps.user_interaction.EmojiStatusFailedInfo
import eu.vendeli.webapps.user_interaction.FileDownloadRequestedInfo
import eu.vendeli.webapps.user_interaction.FullscreenFailedInfo
import eu.vendeli.webapps.user_interaction.GyroscopeFailedInfo
import eu.vendeli.webapps.user_interaction.HomeScreenCheckedInfo
import eu.vendeli.webapps.user_interaction.HomeScreenStatus
import eu.vendeli.webapps.user_interaction.ShareMessageFailedInfo

// Callbacks
typealias AlertCallback = () -> Unit
typealias ClosePopupCallback = (id: String) -> Unit
typealias ConfirmCallback = (confirmed: Boolean) -> Unit
typealias QRTextReceivedCallback = (String) -> Boolean
typealias ClipboardTextReceivedCallback = (String) -> Unit
typealias BiometricRequestAccessCallback = (accessGranted: Boolean) -> Unit
typealias BiometricAuthenticateCallback = (userAuthenticated: Boolean) -> Unit
typealias BiometricUpdateTokenCallback = (tokenUpdated: Boolean) -> Unit
typealias TrackingStartedCallback = (started: Boolean) -> Unit
typealias TrackingStoppedCallback = (started: Boolean) -> Unit
typealias LocationRequestedCallback = (location: LocationData?) -> Unit
typealias HomeScreenStatusCallback = (status: HomeScreenStatus) -> Unit

// Handlers
typealias NoParamsEventHandler = WebApp.() -> Unit
typealias ViewportChangedEventHandler = WebApp.(ViewportChangedData) -> Unit
typealias InvoiceClosedEventHandler = WebApp.(InvoiceClosedInfo) -> Unit
typealias PopupClosedEventHandler = WebApp.(String?) -> Unit
typealias QRTextReceivedEventHandler = WebApp.(String) -> Boolean
typealias ClipboardTextReceivedEventHandler = WebApp.(String) -> Unit
typealias WriteAccessRequestedHandler = WebApp.(WriteAccessRequestState) -> Unit
typealias ContactRequestedHandler = WebApp.(ContactRequestState) -> Unit
typealias FullscreenFailedEventHandler = WebApp.(FullscreenFailedInfo) -> Unit
typealias HomeScreenCheckedEventHandler = WebApp.(HomeScreenCheckedInfo) -> Unit
typealias AccelerometerFailedEventHandler = WebApp.(AccelerometerFailedInfo) -> Unit
typealias DeviceOrientationFailedEventHandler = WebApp.(DeviceOrientationFailedInfo) -> Unit
typealias EmojiStatusFailedEventHandler = WebApp.(EmojiStatusFailedInfo) -> Unit
typealias EmojiStatusAccessRequestedEventHandler = WebApp.(EmojiStatusAccessRequestedInfo) -> Unit
typealias FileDownloadRequestedEventHandler = WebApp.(FileDownloadRequestedInfo) -> Unit
typealias GyroscopeFailedEventHandler = WebApp.(GyroscopeFailedInfo) -> Unit
typealias LocationRequestedEventHandler = WebApp.(LocationData) -> Unit
typealias ShareMessageFailedEventHandler = WebApp.(ShareMessageFailedInfo) -> Unit
