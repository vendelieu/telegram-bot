package eu.vendeli.ksp

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal fun ApiProcessor.validateTypes(classes: Sequence<KSClassDeclaration>, apiFile: JsonElement) {
    val types = apiFile.jsonObject["types"]!!.jsonObject

    classes.forEach { cls ->
        val className = cls.annotations.firstOrNull {
            it.shortName.getShortName() == "Name"
        }?.arguments?.first()?.value?.toString() ?: cls.simpleName.getShortName()
        val sealedSubclasses = cls.getSealedSubclasses()

        if (sealedSubclasses.any()) {
            sealedSubclasses.forEach sealedLoop@{ s ->
                val sealedName = s.simpleName.getShortName()
                val sealedFullName = s.qualifiedName!!.asString()
                val sealedParams = s.getAllProperties().map { it.simpleName.asString() }.toSet()
                val apiName = s.annotations.firstOrNull {
                    it.shortName.getShortName() == "Name"
                }?.arguments?.first()?.value?.toString()
                    ?: (className + sealedName)

                processClass(types, sealedParams, apiName, sealedFullName)
            }
            return@forEach
        }

        val classFullName = cls.qualifiedName!!.asString()
        val classParams = cls.getDeclaredProperties().map { it.simpleName.asString() }.toSet()

        processClass(types, classParams, className, classFullName)
    }
}


private fun ApiProcessor.processClass(types: JsonObject, params: Set<String>, name: String, fullName: String) {
    val typeInfo = types[name]?.jsonObject
    if (typeInfo == null) {
        logger.warn("Class $fullName not found specs and validation is omitted.")
        return
    }

    typeInfo["fields"]?.jsonArray?.forEach {
        val paramName = it.jsonObject["name"]?.jsonPrimitive?.content?.snakeToCamelCase()
        if (paramName !in params)
            logger.exception(IllegalStateException("Parameter $paramName is not present in $fullName\n${typeInfo["href"]!!.jsonPrimitive.content}"))
    }
}
