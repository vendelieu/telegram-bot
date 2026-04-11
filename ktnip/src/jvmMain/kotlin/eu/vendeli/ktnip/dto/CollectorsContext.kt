package eu.vendeli.ktnip.dto

import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import eu.vendeli.ktnip.utils.FileBuilder

data class CollectorsContext(
    val activitiesFile: FileBuilder,
    val botCtxFile: FileBuilder,
    val injectableTypes: Map<TypeName, ClassName>,
    val logger: KSPLogger,
    val loadFun: FunSpec.Builder,
    val pkg: String? = null,
    val autoAnswerCallback: Boolean = false,
    // Tracks Activity object names already emitted into activitiesFile/loadFun
    // during the current processing pass, mapped to the activityId reference
    // returned from generateAndRegisterActivity. Ensures idempotent emission
    // so no caller can append a duplicate object declaration for the same
    // function — covers both CommonHandler.Text multi-value flattening and
    // the hypothetical case of a single function being picked up by multiple
    // collectors.
    val emittedActivities: MutableMap<String, String>,
)
