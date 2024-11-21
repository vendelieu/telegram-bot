package eu.vendeli.webapps.core

import eu.vendeli.webapps.biometric.BiometricAuthenticateParams
import eu.vendeli.webapps.biometric.BiometricRequestAccessParams
import eu.vendeli.webapps.biometric.BiometricType
import eu.vendeli.webapps.utils.BiometricAuthenticateCallback
import eu.vendeli.webapps.utils.BiometricRequestAccessCallback
import eu.vendeli.webapps.utils.BiometricUpdateTokenCallback

external interface BiometricManager {
    val isInited: Boolean
    val isBiometricAvailable: Boolean
    val biometricType: BiometricType
    val isAccessRequested: Boolean
    val isAccessGranted: Boolean
    val isBiometricTokenSaved: Boolean
    val deviceId: String

    fun init(callback: () -> Unit = definedExternally): BiometricManager
    fun requestAccess(
        params: BiometricRequestAccessParams,
        callback: BiometricRequestAccessCallback = definedExternally,
    ): BiometricManager
    fun authenticate(
        params: BiometricAuthenticateParams,
        callback: BiometricAuthenticateCallback = definedExternally,
    ): BiometricManager

    fun updateBiometricToken(
        token: String,
        callback: BiometricUpdateTokenCallback = definedExternally,
    ): BiometricManager

    fun openSettings(): BiometricManager
}
