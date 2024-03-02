package eu.vendeli.webapps.button

external interface BackButton {
    val isVisible: Boolean

    fun onClick(callback: () -> Unit)
    fun offClick(callback: () -> Unit)

    fun show()
    fun hide()
}
