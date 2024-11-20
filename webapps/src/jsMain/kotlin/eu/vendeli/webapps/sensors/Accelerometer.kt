package eu.vendeli.webapps.sensors

import eu.vendeli.webapps.utils.TrackingStartedCallback
import eu.vendeli.webapps.utils.TrackingStoppedCallback

external interface Accelerometer {
    val isStarted: Boolean
    val x: Float
    val y: Float
    val z: Float

    fun start(
        params: AccelerometerStartParams,
        callback: TrackingStartedCallback = definedExternally,
    ): Accelerometer

    fun stop(
        callback: TrackingStoppedCallback = definedExternally,
    ): Accelerometer
}
