package eu.vendeli.ksp

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.InlineActionExt
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.utils.fqName
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal fun ApiProcessor.validateApi(classes: Sequence<KSClassDeclaration>, apiFile: JsonElement) {
    val allMethods = apiFile.jsonObject["methods"]!!.jsonObject.toMap()
    val visitedMethods = mutableSetOf<String>()

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

        // validate class name SHOULD be MethodNameAction
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

        // add parameters that represented in features/etc
        cls.superTypes.forEach {
            when (
                it
                    .resolve()
                    .declaration.qualifiedName!!
                    .asString()
            ) {
                CaptionFeature::class.fqName -> {
                    parameters["caption"] = STRING
                    parameters["captionEntities"] = entitiesType
                }

                EntitiesFeature::class.fqName -> parameters["entities"] = entitiesType
                MarkupFeature::class.fqName -> parameters["replyMarkup"] = replyMarkupType
                BusinessActionExt::class.fqName -> parameters["businessConnectionId"] = STRING
                InlineActionExt::class.fqName -> parameters["inlineMessageId"] = STRING
            }
        }

        // find json info for method
        val method = allMethods[methodName]
        if (method == null) {
            logger.invalid {
                "Api validation gone wrong, no data for method: $methodName"
            }
            return
        }
        // check return type correctness
        method.jsonObject["returns"]!!.jsonArray.let { returns ->
            val actionVariants = listOf("Action", "SimpleAction", "MediaAction")
            val methodActionRet = cls
                .getAllSuperTypes()
                .find {
                    it.declaration.simpleName.getShortName() in actionVariants
                }!!
                .arguments
                .first()
                .type!!
                .resolve()

            val apiReturnMatchType = when (val simpleName = methodActionRet.declaration.simpleName.getShortName()) {
                "List" ->
                    "Array of " + methodActionRet.arguments
                        .first()
                        .type!!
                        .resolve()
                        .toClassName()
                        .simpleName
                        .returnTypeCorrection()

                else -> simpleName.returnTypeCorrection()
            }

            if (returns.find { (it as? JsonPrimitive)?.content == apiReturnMatchType } == null)
                logger.warn(
                    "Possibly return type of $classFullname ($apiReturnMatchType) is wrong, should be one of $returns",
                )
        }

        // check all fields presence
        method.jsonObject["fields"]?.jsonArray?.forEach params@{
            val origParameterName = it.jsonObject["name"]!!
                .jsonPrimitive.content
            val camelParamName = origParameterName.snakeToCamelCase()
            val targetParam = parameters[camelParamName]
            val isRequired = it.jsonObject["required"]!!.jsonPrimitive.boolean
            val apiRefLink = method.jsonObject["href"]!!.jsonPrimitive.content

            // exclude chatId since it covered in send* methods
            if (camelParamName != "chatId" && targetParam == null) {
                logger.warn(
                    "Api parameter `$origParameterName`($camelParamName) " +
                        "is probably not present in class $classFullname (method: `$methodName`)\n$apiRefLink",
                )
                return@params
            }

            // check nullability
            if (isRequired && targetParam?.isNullable == true) logger.invalid {
                "Wrong nullability for `$camelParamName` in $classFullname\n$apiRefLink"
            }
            parameters.remove(camelParamName) // remove checked ones
        }
        // check is there anything left after checking
        parameters.remove("block") // remove EntitiesCtxBuilder constructor parameter
        parameters.takeIf { it.isNotEmpty() }?.let {
            logger.warn(
                "Probably redundant parameters for method $methodName: ${parameters.keys.joinToString()}\n" +
                    "Implemented in $classFullname.",
            )
        }
        if (!visitedMethods.add(methodName)) logger.warn("Duplicate processing of a method $methodName")
    }
    val leftMethods = allMethods.keys - visitedMethods
    if (leftMethods.isNotEmpty()) logger.warn("Not all methods have been processed; remaining are:: $leftMethods")
}

private fun String.returnTypeCorrection() = when (this) {
    "ProcessedUpdate" -> "Update"
    "Int" -> "Integer"
    else -> this
}
