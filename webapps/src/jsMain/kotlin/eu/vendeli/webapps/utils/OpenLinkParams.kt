package eu.vendeli.webapps.utils

external interface OpenLinkParams {
    @JsName("try_instant_view")
    var tryInstantView: Boolean
}

fun OpenLinkParams(
    tryInstantView: Boolean,
) = build<OpenLinkParams> {
    this.tryInstantView = tryInstantView
}
