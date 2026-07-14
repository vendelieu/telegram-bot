package eu.vendeli.ktgram.gradle

private val dynamicVersionMarkers = charArrayOf('+', '[', ']', '(', ')', ',')

/**
 * Returns true for dynamic/range version notations (`+`, `latest.release`, `[1.0,2.0)` etc.)
 * that cannot be compared and shouldn't be overridden.
 */
internal fun String.isDynamicVersion(): Boolean =
    indexOfAny(dynamicVersionMarkers) >= 0 || startsWith("latest.", ignoreCase = true)

/**
 * Compares two version strings by their numeric dot-separated parts,
 * treating a pre-release suffix (after `-`) as lower than the plain release.
 */
internal fun compareVersions(left: String, right: String): Int {
    val leftParts = left.split('-', limit = 2)
    val rightParts = right.split('-', limit = 2)
    val leftNumbers = leftParts[0].split('.').map { it.toIntOrNull() ?: 0 }
    val rightNumbers = rightParts[0].split('.').map { it.toIntOrNull() ?: 0 }

    repeat(maxOf(leftNumbers.size, rightNumbers.size)) { i ->
        val cmp = leftNumbers.getOrElse(i) { 0 }.compareTo(rightNumbers.getOrElse(i) { 0 })
        if (cmp != 0) return cmp
    }

    val leftSuffix = leftParts.getOrNull(1)
    val rightSuffix = rightParts.getOrNull(1)
    return when {
        leftSuffix == rightSuffix -> 0
        leftSuffix == null -> 1
        rightSuffix == null -> -1
        else -> leftSuffix.compareTo(rightSuffix, ignoreCase = true)
    }
}

internal val loggerProviders = setOf(
    "ch.qos.logback" to "logback-classic",
    "org.slf4j" to "slf4j-simple",
    "org.slf4j" to "log4j-over-slf4j",
    "org.slf4j" to "slf4j-log4j12",
    "org.slf4j" to "slf4j-jdk14",
    "org.slf4j" to "jul-to-slf4j",
    "org.slf4j" to "slf4j-reload4j",
    "org.slf4j" to "jcl-over-slf4j",
    "org.slf4j" to "slf4j-jcl",
    "org.slf4j" to "slf4j-jdk-platform-logging",
    "org.apache.logging.log4j" to "log4j-slf4j-impl",
    "org.apache.logging.log4j" to "log4j-slf4j2-impl",
    "org.apache.logging.log4j" to "log4j-slf4j18-impl",
    "org.tinylog" to "slf4j-tinylog",
    "org.logevents" to "logevents",
    "com.hkupty.penna" to "penna-core",
    "io.jstach.rainbowgum" to "rainbowgum-core",
)
