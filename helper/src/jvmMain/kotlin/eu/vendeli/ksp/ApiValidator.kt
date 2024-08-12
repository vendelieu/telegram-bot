package eu.vendeli.ksp

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

internal fun ApiProcessor.validateApi(classes: Sequence<KSClassDeclaration>, apiFile: String) {
    val apiJson = Json.parseToJsonElement(File(apiFile).readText())

    classes.forEach { cls ->
        val className = cls.simpleName.getShortName()
        val classFullname = cls.qualifiedName!!.asString()
        val properties = cls.getDeclaredProperties()
        val methodName = properties
            .find {
                it.simpleName.getShortName() == "method"
            }!!
            .annotations
            .first()
            .arguments
            .first()
            .value as String

        // validate name class SHOULD be MethodNameAction
        if (!(methodName + "action").equals(className, true)) logger.warn(
            "Action naming convention is broken or wrong method is using in $classFullname (method: $methodName)",
        )

        val parameters: MutableMap<String, TypeName> = mutableMapOf()

        // collect parameters (in lowercase) from constructors
        cls.getConstructors().forEach { c ->
            c.parameters.forEach { parameters[it.name!!.getShortName().lowercase()] = it.type.toTypeName() }
        }

        // collect from options
        logger.info("Processing class $classFullname")
        properties
            .find {
                it.simpleName.getShortName() == "options"
            }?.type
            ?.resolve()
            ?.declaration
            ?.let { it as KSClassDeclaration }
            ?.getDeclaredProperties()
            ?.associate { it.simpleName.getShortName().lowercase() to it.type.toTypeName() }
            ?.let { parameters.putAll(it) }

        val absentProperties = listOf(
            "replymarkup",
            "chatid",
            "caption",
            "entities",
            "captionentities",
            "inlinemessageid",
            "businessconnectionid",
        )

        // find json info for method
        val method = apiJson.jsonObject["methods"]!!.jsonObject[methodName]
        if (method == null) {
            logger.exception(IllegalStateException("Api validation gone wrong, no data for method: $methodName"))
            return
        }
        method.jsonObject["fields"]?.jsonArray?.forEach {
            val origParameterName = it.jsonObject["name"]!!
                .jsonPrimitive.content
            val paramName = origParameterName.replace("_", "")
            if (paramName !in absentProperties && paramName !in parameters) {
                logger.warn(
                    "Api parameter `$origParameterName` is possibly not present in class $classFullname (method: `$methodName`)\n${method.jsonObject["href"]!!.jsonPrimitive.content}",
                )
            }
        }
    }
}
