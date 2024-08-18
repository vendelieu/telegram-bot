package eu.vendeli.ktgram.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.listProperty
import javax.inject.Inject

abstract class KtGramExt @Inject constructor(factory: ObjectFactory) {
    val packages = factory.listProperty<String>()
}
