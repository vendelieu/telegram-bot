package eu.vendeli.ktgram.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

/**
 * Extension for [KtGramPlugin] that can be used to configure the KtGram DSL.
 *
 * @property packages list of packages to scan for KtGram annotations.
 * @property addSnapshotRepo set to true to add the snapshot repository to the repositories list.
 * @property forceVersion set to a version string to force library specific version.
 * @property autoCleanClassData set to false to prevent the KSP processor from cleaning the class data automatically.
 * @property ktorJvmEngine the Ktor JVM engine to use, defaults to [KtorJvmEngine.JAVA].
 */
abstract class KtGramExt
    @Inject
    constructor(
        factory: ObjectFactory,
    ) {
        val packages = factory.listProperty<String>()
        val addSnapshotRepo = factory.property<Boolean>()
        val forceVersion = factory.property<String>()
        val autoCleanClassData = factory.property<Boolean>()
        val ktorJvmEngine = factory.property<KtorJvmEngine>()
    }
