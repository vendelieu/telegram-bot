plugins {
    kotlin("jvm")
    publish
}

kotlin {
    jvmToolchain(JVM_TARGET.toInt())
}

dependencies {
    implementation(project(":quarkus-ktgram"))
    implementation(libs.quarkus.core.deployment)
    implementation(libs.quarkus.arc.deployment)

    testImplementation(libs.quarkus.junit.internal)
}

libraryData {
    name = "Quarkus extension deployment"
    description = "Build-time part of the KtGram Quarkus extension."
}
