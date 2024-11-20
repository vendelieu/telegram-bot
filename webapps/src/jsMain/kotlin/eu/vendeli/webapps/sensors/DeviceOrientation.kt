package eu.vendeli.webapps.sensors

import eu.vendeli.webapps.utils.TrackingStartedCallback
import eu.vendeli.webapps.utils.TrackingStoppedCallback

external interface DeviceOrientation {
    val isStarted: Boolean
    val absolute: Boolean
    val alpha: Float
    val beta: Float
    val gamma: Float

    fun start(
        params: DeviceOrientationStartParams,
        callback: TrackingStartedCallback = definedExternally,
    ): DeviceOrientation

    fun stop(
        callback: TrackingStoppedCallback = definedExternally,
    ): DeviceOrientation
}
