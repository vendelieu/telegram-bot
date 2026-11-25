plugins {
    kotlin("jvm")
    alias(libs.plugins.quarkus.extension)
    dokka
    publish
}

kotlin {
    jvmToolchain(JVM_TARGET.toInt())
}

quarkusExtension {
    deploymentModule = ":quarkus-ktgram-deployment"
}

// The 3.x plugin doesn't declare the version as an input of this task, so changing it
// would leave a stale deployment-artifact version in the descriptor.
tasks.named("extensionDescriptor") { inputs.property("version", version.toString()) }

dependencies {
    implementation(libs.quarkus.core)
    implementation(libs.quarkus.arc)

    // Unlike the Spring starter, the extension brings KtGram itself: Quarkus needs the library
    // on the extension's (non-reloadable) runtime classpath, otherwise dev/test mode in a
    // multi-module build loads it in a different class loader than the extension.
    api(project(":telegram-bot"))
    compileOnly(libs.ktor.client.core)
}

libraryData {
    name = "Quarkus extension"
    description = "Quarkus extension for KtGram."
}
