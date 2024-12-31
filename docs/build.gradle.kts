plugins {
    kotlin("jvm") apply false
    dokka
}

dependencies {
    rootProject.subprojects.filter { it.name !in listOf("helper", "ksp", "ktgram-gradle-plugin", "docs") }.forEach {
        dokka(project(":" + it.name))
    }
}
