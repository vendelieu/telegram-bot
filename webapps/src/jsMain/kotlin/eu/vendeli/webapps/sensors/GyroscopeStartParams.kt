package eu.vendeli.webapps.sensors

external interface GyroscopeStartParams {
    @JsName("refresh_rate")
    val refreshRate: Int
}
