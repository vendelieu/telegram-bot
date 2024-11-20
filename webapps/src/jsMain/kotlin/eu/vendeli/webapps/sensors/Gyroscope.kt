package eu.vendeli.webapps.sensors

import eu.vendeli.webapps.utils.TrackingStartedCallback
import eu.vendeli.webapps.utils.TrackingStoppedCallback

external interface Gyroscope {
    val isStarted: Boolean
    val x: Float
    val y: Float
    val z: Float

    fun start(
        params: GyroscopeStartParams,
        callback: TrackingStartedCallback = definedExternally,
    ): Gyroscope

    fun stop(
        callback: TrackingStoppedCallback = definedExternally,
    ): Gyroscope
}
