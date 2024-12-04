package eu.vendeli.ksp.dto

import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import eu.vendeli.ksp.utils.FileBuilder

data class CollectorsContext(
    val activitiesFile: FileBuilder,
    val botCtxFile: FileBuilder,
    val injectableTypes: Map<TypeName, ClassName>,
    val logger: KSPLogger,
    val idxPostfix: String,
    val pkg: String? = null,
    val autoCleanClassData: Boolean = true,
)
