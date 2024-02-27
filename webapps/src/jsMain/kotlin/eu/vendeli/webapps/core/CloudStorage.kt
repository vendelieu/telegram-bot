package eu.vendeli.webapps.core

external interface CloudStorage {
    fun setItem(
        key: String,
        value: String,
        callback: (e: Any?, success: Boolean?) -> Unit = definedExternally,
    ): CloudStorage

    fun getItem(
        key: String,
        callback: (e: Any?, value: String?) -> Unit,
    ): CloudStorage

    fun getItems(
        key: Array<String>,
        callback: (e: Any?, values: Array<String>?) -> Unit,
    ): CloudStorage

    fun removeItem(
        key: String,
        callback: (e: Any?, success: Boolean?) -> Unit,
    ): CloudStorage

    fun removeItems(
        key: Array<String>,
        callback: (e: Any?, success: Boolean?) -> Unit,
    ): CloudStorage

    fun getKeys(
        callback: (e: Any?, success: Array<String>?) -> Unit,
    ): CloudStorage
}
