package eu.vendeli.ktgram.gradle

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
