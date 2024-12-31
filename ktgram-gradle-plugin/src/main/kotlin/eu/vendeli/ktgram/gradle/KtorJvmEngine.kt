package eu.vendeli.ktgram.gradle

enum class KtorJvmEngine(
    internal val artifact: String,
) {
    NONE("none"),

    JAVA("java"),
    OKHTTP("okhttp"),
    CIO("cio"),
    APACHE("apache5"),
    JETTY("jetty"),
}
