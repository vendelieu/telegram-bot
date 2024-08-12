package eu.vendeli.ksp

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal fun ApiProcessor.validateTypes(classes: Sequence<KSClassDeclaration>, apiFile: JsonElement) {
    val types = apiFile.jsonObject["types"]!!.jsonObject

    classes.forEach { cls ->
        val className = cls.simpleName.getShortName()
        val classFullName = cls.qualifiedName!!.asString()
        val classParams = cls.getDeclaredProperties().map { it.simpleName.asString() }.toSet()

        val typeInfo = types[className]?.jsonObject
        if (typeInfo == null) {
            logger.warn("Class $classFullName not found specs and validation is omitted.")
            return@forEach
        }
        typeInfo["fields"]!!.jsonArray.forEach {
            val paramName = it.jsonObject["name"]?.jsonPrimitive?.content?.snakeToCamelCase()
            if (paramName !in classParams)
                logger.exception(IllegalStateException("Parameter $paramName is not present in $classFullName\n${typeInfo["href"]!!.jsonPrimitive.content}"))
        }
    }
}
