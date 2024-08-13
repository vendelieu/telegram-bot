package eu.vendeli.ksp.dto

import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

data class ProcessorCtxData(
    val injectableTypes: Map<TypeName, ClassName>,
    val logger: KSPLogger,
    val idxPostfix: String,
)
