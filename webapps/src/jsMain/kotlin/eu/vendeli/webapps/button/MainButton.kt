package eu.vendeli.webapps.button

import kotlinx.js.JsPlainObject
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

@JsPlainObject
external interface MainButtonParams {
    val text: String?
    val color: String?

    @JsName("text_color")
    val textColor: String?

    @JsName("is_active")
    val isActive: Boolean?

    @JsName("is_visible")
    val isVisible: Boolean?
}

fun MainButton.setParams(block: MainButtonParams.() -> Unit): MainButton = MainButtonParams().apply(block).let {
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    this.setParams(it.asDynamic() as Json)
}


