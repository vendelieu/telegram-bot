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
 * @property autoCleanClassData set to true to make the processor handle cleaning the class data automatically.
 * @property autoAnswerCallback set to true to automatically respond to callbacks unless otherwise specified in the annotation.
 * @property ktorJvmEngine the Ktor JVM engine to use, defaults to [KtorJvmEngine.JAVA].
 * @property handleLoggingProvider set to false to disable the logging provider handling.
 * @property aideEnabled set to true to enable compilation Aide for automatically send actions.
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
        val autoAnswerCallback = factory.property<Boolean>()
        val ktorJvmEngine = factory.property<KtorJvmEngine>()
        val handleLoggingProvider = factory.property<Boolean>()
        val aideEnabled = factory.property<Boolean>()
    }
