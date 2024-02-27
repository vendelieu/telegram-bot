package eu.vendeli.webapps.utils

@Suppress("NOTHING_TO_INLINE")
inline fun <T : Any> build(): T =
    js("({})") as T

inline fun <T : Any> build(
    block: T.() -> Unit,
): T = build<T>().apply(block)
