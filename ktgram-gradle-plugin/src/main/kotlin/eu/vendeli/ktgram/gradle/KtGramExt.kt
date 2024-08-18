package eu.vendeli.ktgram.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

abstract class KtGramExt
    @Inject
    constructor(
        factory: ObjectFactory,
    ) {
        val packages = factory.listProperty<String>()
        val addSnapshotRepo = factory.property<Boolean>()
        val forceVersion = factory.property<String>()
    }
