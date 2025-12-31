package eu.vendeli.sentinel

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import eu.vendeli.tgbot.annotations.internal.TgAPI
import kotlinx.serialization.json.*

internal fun ApiProcessor.validateTypes(classes: Sequence<KSClassDeclaration>, apiFile: JsonElement) {
    val types = apiFile.jsonObject["types"]!!.jsonObject.toMap()
    val visitedTypes = mutableSetOf<String>()
    visitedTypes.add("InputFile") // in internal types

    classes.forEach { cls ->
        val className = cls.annotations
            .firstOrNull {
                it.shortName.getShortName() == TgAPI.Name::class.simpleName!!
            }?.arguments
            ?.first()
            ?.value
            ?.toString() ?: cls.simpleName.getShortName()
        val sealedSubclasses = cls.getSealedSubclasses()

        if (sealedSubclasses.any()) {
            visitedTypes.add(className)
            sealedSubclasses.forEach sealedLoop@{ s ->
                val sealedName = s.simpleName.getShortName()
                val sealedFullName = s.qualifiedName!!.asString()
                val sealedParams = s.getAllProperties().associateBy { it.simpleName.asString() }
                val apiName = s.annotations
                    .firstOrNull {
                        it.shortName.getShortName() == TgAPI.Name::class.simpleName!!
                    }?.arguments
                    ?.first()
                    ?.value
                    ?.toString()
                    ?: (className + sealedName)

                processClass(types, sealedParams, apiName, sealedFullName, visitedTypes)
            }
            return@forEach
        }

        val classFullName = cls.qualifiedName!!.asString()
        val classParams = cls.getDeclaredProperties().associateBy { it.simpleName.asString() }

        processClass(types, classParams, className, classFullName, visitedTypes)
    }
    val leftTypes = types.keys - visitedTypes
    if (leftTypes.isNotEmpty()) logger.invalid {
        "Not all types have been processed; remaining are:: $leftTypes"
    }
}

private fun ApiProcessor.processClass(
    types: Map<String, JsonElement>,
    params: Map<String, KSPropertyDeclaration>,
    name: String,
    fullName: String,
    visitedTypesRef: MutableSet<String>,
) {
    val typeInfo = types[name]?.jsonObject
    if (typeInfo == null) {
        logger.invalid {
            "Class $fullName not found specs and validation is omitted."
        }
        return
    }

    val leftParams = params
        .mapNotNull { p ->
            p.takeIf { np ->
                np.value.annotations.none {
                    it.shortName.getShortName() == TgAPI.Ignore::class.simpleName!!
                } && np.value.findOverridee()?.annotations?.none {
                    it.shortName.getShortName() == TgAPI.Ignore::class.simpleName!!
                } != false
            }?.key
        }.toMutableList()

    typeInfo["fields"]?.jsonArray?.forEach {
        val paramName = it.jsonObject["name"]
            ?.jsonPrimitive
            ?.content
            ?.snakeToCamelCase()
        val isRequired = it.jsonObject["required"]!!.jsonPrimitive.boolean
        val targetParam = params[paramName]

        if (targetParam == null) {
            logger.invalid {
                "Parameter $paramName is not present in $fullName\n${typeInfo["href"]!!.jsonPrimitive.content}"
            }
            return@forEach
        }

        if (isRequired && targetParam.type.resolve().isMarkedNullable) logger.invalid {
            "Wrong nullability for `$paramName` in $fullName\n${typeInfo["href"]!!.jsonPrimitive.content}"
        }

        leftParams.remove(paramName)
    }

    if (leftParams.isNotEmpty()) logger.invalid {
        "Probably some parameters are redundant in $fullName\n$leftParams\n${typeInfo["href"]!!.jsonPrimitive.content}\n"
    }

    visitedTypesRef.add(name)
}
