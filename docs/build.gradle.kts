plugins {
    kotlin("jvm") apply false
    dokka
}

dependencies {
    rootProject.subprojects.filter { it.name !in listOf("api-sentinel", "ksp", "docs") }.forEach {
        dokka(project(":" + it.name))
    }
}
