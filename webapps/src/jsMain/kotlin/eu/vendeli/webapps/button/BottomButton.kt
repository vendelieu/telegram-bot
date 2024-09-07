package eu.vendeli.webapps.button

import kotlin.js.Json

@Suppress("EnumEntryName")
enum class BottomButtonType {
    main, secondary
}

external class BottomButton {
    val type: BottomButtonType
    val text: String
    fun setText(text: String): BottomButton

    var color: String
    var textColor: String

    var hasShineEffect: Boolean

    val isVisible: Boolean
    fun show(): BottomButton
    fun hide(): BottomButton

    val isActive: Boolean
    fun enable(): BottomButton
    fun disable(): BottomButton

    val isProgressVisible: Boolean
    fun showProgress(leaveActive: Boolean = definedExternally): BottomButton
    fun hideProgress(): BottomButton

    fun onClick(eventHandler: () -> Unit): BottomButton
    fun offClick(eventHandler: () -> Unit): BottomButton

    internal fun setParams(params: Json): BottomButton
}

class MainButtonParams(
    var text: String? = null,
    var color: String? = null,
    @JsName("text_color")
    var textColor: String? = null,
    @JsName("has_shine_effect")
    var hasShineEffect: Boolean? = null,
    @JsName("is_active")
    var isActive: Boolean? = null,
    @JsName("is_visible")
    var isVisible: Boolean? = null,
)

fun BottomButton.setParams(block: MainButtonParams.() -> Unit): BottomButton = MainButtonParams().apply(block).let {
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    this.setParams(it.asDynamic() as Json)
}


