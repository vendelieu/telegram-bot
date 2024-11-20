package eu.vendeli.webapps.sensors

external interface LocationData {
    @JsName("latitude")
    val latitude: Float

    @JsName("longitude")
    val longitude: Float

    @JsName("altitude")
    val altitude: Float?

    @JsName("course")
    val course: Float?

    @JsName("speed")
    val speed: Float?

    @JsName("horizontal_accuracy")
    val horizontalAccuracy: Float?

    @JsName("vertical_accuracy")
    val verticalAccuracy: Float?

    @JsName("course_accuracy")
    val courseAccuracy: Float?

    @JsName("speed_accuracy")
    val speedAccuracy: Float?
}
