package eu.vendeli.webapps.sensors

external interface DeviceOrientationStartParams {
    @JsName("refresh_rate")
    val refreshRate: Int?

    @JsName("need_absolute")
    val needAbsolute: Boolean?
}
