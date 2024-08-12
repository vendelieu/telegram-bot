package eu.vendeli.ksp

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal fun ApiProcessor.validateApi(classes: Sequence<KSClassDeclaration>, apiFile: JsonElement) {
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

        // collect parameters from constructors
        cls.getConstructors().forEach { c ->
            c.parameters.forEach { parameters[it.name!!.getShortName()] = it.type.toTypeName() }
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
            ?.associate { it.simpleName.getShortName() to it.type.toTypeName() }
            ?.let { parameters.putAll(it) }

        val absentProperties = listOf(
            "replyMarkup",
            "chatId",
            "caption",
            "entities",
            "captionEntities",
            "inlineMessageId",
            "businessConnectionId",
        )

        // find json info for method
        val method = apiFile.jsonObject["methods"]!!.jsonObject[methodName]
        if (method == null) {
            logger.exception(IllegalStateException("Api validation gone wrong, no data for method: $methodName"))
            return
        }
        method.jsonObject["fields"]?.jsonArray?.forEach {
            val origParameterName = it.jsonObject["name"]!!
                .jsonPrimitive.content
            val camelParamName = origParameterName.snakeToCamelCase()
            if (camelParamName !in absentProperties && camelParamName !in parameters) {
                logger.warn(
                    "Api parameter `$origParameterName`($camelParamName) is possibly not present in class $classFullname (method: `$methodName`)\n${method.jsonObject["href"]!!.jsonPrimitive.content}",
                )
            }
        }
    }
}
