plugins {
    kotlin("jvm") apply false
    dokka
}

dependencies {
    rootProject.subprojects.filter { it.name !in listOf("helper", "ksp", "docs") }.forEach {
        dokka(project(":" + it.name))
    }
}
