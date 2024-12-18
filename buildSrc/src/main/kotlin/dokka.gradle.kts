import CommonResources.REPO_URL
import org.jetbrains.dokka.gradle.engine.parameters.DokkaSourceSetSpec
import java.time.LocalDate

plugins {
    id("org.jetbrains.dokka")
}

dokka {
    moduleName = if (project.name != "docs") project.name else "KtGram"
    dokkaSourceSets {
        configureSourceNamed("main") { project.plugins.hasPlugin("org.jetbrains.kotlin:kotlin-jvm") }
        configureSourceNamed("commonMain")
        configureSourceNamed("jvmMain")
        configureSourceNamed("jsMain")
        configureSourceNamed("nativeMain")
    }

    pluginsConfiguration.html {
        customAssets.from(rootDir.resolve("assets/logo-icon.svg"))
        footerMessage.set("© ${LocalDate.now().year} Vendelieu")
        homepageLink.set(REPO_URL)
    }
}

private fun NamedDomainObjectContainer<DokkaSourceSetSpec>.configureSourceNamed(
    name: String,
    condition: NamedDomainObjectContainer<DokkaSourceSetSpec>.() -> Boolean = { name in names },
) = apply {
    if (condition()) named(name) {
        sourceRoots.setFrom(project.projectDir.resolve("src/$name/kotlin"))
        sourceLink {
            localDirectory.set(file("src/$name/kotlin"))
            remoteUrl("$REPO_URL/blob/master/${project.name}/src/$name/kotlin")
            remoteLineSuffix.set("#L")
        }
    }
}
