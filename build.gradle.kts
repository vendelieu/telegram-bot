allprojects {
    repositories {
        mavenCentral()
    }
}

tasks.create<Delete>("clean") {
    delete.add(rootProject.buildDir)
}