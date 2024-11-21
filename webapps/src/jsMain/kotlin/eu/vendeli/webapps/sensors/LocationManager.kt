package eu.vendeli.webapps.sensors

import eu.vendeli.webapps.utils.LocationRequestedCallback

external interface LocationManager {
    val isInited: Boolean
    val isLocationAvailable: Boolean
    val isAccessRequested: Boolean
    val isAccessGranted: Boolean

    fun init(callback: () -> Unit = definedExternally): LocationManager
    fun getLocation(callback: LocationRequestedCallback): LocationManager
    fun openSettings(): LocationManager
}
