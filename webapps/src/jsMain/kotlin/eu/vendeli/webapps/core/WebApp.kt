package eu.vendeli.webapps.core

import eu.vendeli.webapps.button.BackButton
import eu.vendeli.webapps.button.MainButton
import eu.vendeli.webapps.button.SettingsButton
import eu.vendeli.webapps.haptic.HapticFeedback
import eu.vendeli.webapps.invoice.InvoiceClosedInfo
import eu.vendeli.webapps.popup.PopupParams
import eu.vendeli.webapps.popup.ScanQrPopupParams
import eu.vendeli.webapps.ui.ThemeParams
import eu.vendeli.webapps.utils.AlertCallback
import eu.vendeli.webapps.utils.ClipboardTextReceivedCallback
import eu.vendeli.webapps.utils.ClosePopupCallback
import eu.vendeli.webapps.utils.ConfirmCallback
import eu.vendeli.webapps.utils.EventHandler
import eu.vendeli.webapps.utils.OpenLinkParams
import eu.vendeli.webapps.utils.QRTextReceivedCallback

external class WebApp {
    val version: String
    val platform: String
    val initData: String
    val initDataUnsafe: WebAppInitData
    val headerColor: String?
    val backgroundColor: String?
    val colorScheme: String
    val themeParams: ThemeParams
    val isExpanded: Boolean
    val viewportHeight: Float
    val viewportStableHeight: Float
    val isClosingConfirmationEnabled: Boolean
    val isVerticalSwipesEnabled: Boolean

    fun setHeaderColor(color: String)
    fun setBackgroundColor(color: String)
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

    @JsName("MainButton")
    val mainButton: MainButton

    @JsName("BackButton")
    val backButton: BackButton

    @JsName("HapticFeedback")
    val hapticFeedback: HapticFeedback

    @JsName("CloudStorage")
    val cloudStorage: CloudStorage

    @JsName("BiometricManager")
    val biometricManager: BiometricManager

    @JsName("SettingsButton")
    val settingsButton: SettingsButton

    internal fun onEvent(type: String, callback: EventHandler)

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
}
