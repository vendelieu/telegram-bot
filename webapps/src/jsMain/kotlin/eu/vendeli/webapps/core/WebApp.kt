package eu.vendeli.webapps.core

import eu.vendeli.webapps.button.BackButton
import eu.vendeli.webapps.button.BottomButton
import eu.vendeli.webapps.button.SettingsButton
import eu.vendeli.webapps.haptic.HapticFeedback
import eu.vendeli.webapps.invoice.InvoiceClosedInfo
import eu.vendeli.webapps.popup.PopupParams
import eu.vendeli.webapps.popup.ScanQrPopupParams
import eu.vendeli.webapps.sensors.Accelerometer
import eu.vendeli.webapps.sensors.DeviceOrientation
import eu.vendeli.webapps.sensors.Gyroscope
import eu.vendeli.webapps.sensors.LocationManager
import eu.vendeli.webapps.ui.SafeAreaInset
import eu.vendeli.webapps.ui.ThemeParams
import eu.vendeli.webapps.utils.AlertCallback
import eu.vendeli.webapps.utils.ClipboardTextReceivedCallback
import eu.vendeli.webapps.utils.ClosePopupCallback
import eu.vendeli.webapps.utils.ConfirmCallback
import eu.vendeli.webapps.user_interaction.DownloadFileParams
import eu.vendeli.webapps.user_interaction.EmojiStatusParams
import eu.vendeli.webapps.utils.HomeScreenStatusCallback
import eu.vendeli.webapps.utils.OpenLinkParams
import eu.vendeli.webapps.utils.QRTextReceivedCallback
import eu.vendeli.webapps.utils.StoryShareParams

external class WebApp {
    val version: String
    val platform: String
    val initData: String
    val initDataUnsafe: WebAppInitData
    val headerColor: String
    val backgroundColor: String
    val bottomBarColor: String
    val colorScheme: String
    val themeParams: ThemeParams
    val isExpanded: Boolean
    val viewportHeight: Float
    val viewportStableHeight: Float
    val isClosingConfirmationEnabled: Boolean
    val isVerticalSwipesEnabled: Boolean
    val isActive: Boolean
    val isFullscreen: Boolean
    val isOrientationLocked: Boolean
    val safeAreaInset: SafeAreaInset
    val contentSafeAreaInset: SafeAreaInset

    fun setHeaderColor(color: String)
    fun setBackgroundColor(color: String)
    fun setBottomBarColor(color: String)
    fun enableClosingConfirmation()
    fun disableClosingConfirmation()
    fun enableVerticalSwipes()
    fun disableVerticalSwipes()

    fun showPopup(params: PopupParams, callback: ClosePopupCallback? = definedExternally)
    fun showAlert(message: String, callback: AlertCallback? = definedExternally)
    fun showConfirm(message: String, callback: ConfirmCallback? = definedExternally)
    fun showScanQrPopup(params: ScanQrPopupParams, callback: QRTextReceivedCallback? = definedExternally)
    fun closeScanQrPopup()
    fun readTextFromClipboard(callback: ClipboardTextReceivedCallback? = definedExternally)
    fun shareToStory(mediaUrl: String, params: StoryShareParams? = definedExternally)

    @JsName("MainButton")
    val mainButton: BottomButton

    @JsName("SecondaryButton")
    val secondaryButton: BottomButton

    @JsName("BackButton")
    val backButton: BackButton

    @JsName("HapticFeedback")
    val hapticFeedback: HapticFeedback

    @JsName("CloudStorage")
    val cloudStorage: CloudStorage

    @JsName("BiometricManager")
    val biometricManager: BiometricManager

    @JsName("Accelerometer")
    val accelerometer: Accelerometer

    @JsName("DeviceOrientation")
    val deviceOrientation: DeviceOrientation

    @JsName("Gyroscope")
    val gyroscope: Gyroscope

    @JsName("LocationManager")
    val locationManager: LocationManager

    @JsName("SettingsButton")
    val settingsButton: SettingsButton

    internal fun onEvent(type: String, callback: Function<*>)

    fun sendData(data: String)

    fun ready()
    fun expand()
    fun close()

    fun isVersionAtLeast(version: String): Boolean
    fun openLink(url: String, params: OpenLinkParams = definedExternally)
    fun openTelegramLink(url: String)
    fun openInvoice(url: String, callback: (InvoiceClosedInfo) -> Unit = definedExternally)

    fun requestWriteAccess(callback: ((Boolean) -> Unit)? = definedExternally)
    fun requestContact(callback: ((Boolean) -> Unit)? = definedExternally)

    fun requestFullscreen()
    fun exitFullscreen()
    fun lockOrientation()
    fun unlockOrientation()
    fun addToHomeScreen()
    fun checkHomeScreenStatus(callback: HomeScreenStatusCallback = definedExternally)

    @Suppress("LocalVariableName")
    fun shareMessage(msg_id: Long, callback: (Boolean) -> Unit = definedExternally)

    @Suppress("LocalVariableName")
    fun setEmojiStatus(
        custom_emoji_id: Long,
        params: EmojiStatusParams = definedExternally,
        callback: (Boolean) -> Unit = definedExternally,
    )

    fun requestEmojiStatusAccess(callback: (Boolean) -> Unit = definedExternally)

    fun downloadFile(
        params: DownloadFileParams,
        callback: (Boolean) -> Unit = definedExternally,
    )
}
