package eu.vendeli.webapps.utils

import kotlinx.js.JsPlainObject

@JsPlainObject
external interface OpenLinkParams {
    @JsName("try_instant_view")
    val tryInstantView: Boolean
}
