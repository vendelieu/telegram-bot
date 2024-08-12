package eu.vendeli.ksp

internal fun String.beginWithUpperCase(): String = when (this.length) {
    0 -> ""
    1 -> uppercase()
    else -> this[0].uppercase() + this.substring(1)
}

internal fun String.snakeToCamelCase() = split('_').mapIndexed { i, it ->
    if (i == 0) return@mapIndexed it
    it.beginWithUpperCase()
}.joinToString("")
