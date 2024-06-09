package eu.vendeli.webapps.button

import kotlin.js.Json

external class MainButton {
    val text: String
    fun setText(text: String): MainButton

    var color: String
    var textColor: String

    val isVisible: Boolean
    fun show(): MainButton
    fun hide(): MainButton

    val isActive: Boolean
    fun enable(): MainButton
    fun disable(): MainButton

    val isProgressVisible: Boolean
    fun showProgress(leaveActive: Boolean = definedExternally): MainButton
    fun hideProgress(): MainButton

    fun onClick(eventHandler: () -> Unit): MainButton
    fun offClick(eventHandler: () -> Unit): MainButton

    internal fun setParams(params: Json): MainButton
}

class MainButtonParams(
    var text: String? = null,
    var color: String? = null,
    @JsName("text_color")
    var textColor: String? = null,
    @JsName("is_active")
    var isActive: Boolean? = null,
    @JsName("is_visible")
    var isVisible: Boolean? = null,
)

fun MainButton.setParams(block: MainButtonParams.() -> Unit): MainButton = MainButtonParams().apply(block).let {
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    this.setParams(it.asDynamic() as Json)
}


