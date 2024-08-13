package eu.vendeli.ksp

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import eu.vendeli.ksp.utils.botClass
import eu.vendeli.ksp.utils.classDataCtx
import eu.vendeli.ksp.utils.classDataCtxDef
import eu.vendeli.ksp.utils.getAnnotatedClassSymbols
import eu.vendeli.ksp.utils.userClass
import eu.vendeli.ksp.utils.userDataCtx
import eu.vendeli.ksp.utils.userDataCtxDef
import eu.vendeli.tgbot.annotations.CtxProvider
import eu.vendeli.tgbot.annotations.internal.InternalApi

internal fun processCtxProviders(
    codeGenerator: CodeGenerator,
    resolver: Resolver,
    pkg: String? = null,
) {
    val filePkg = pkg ?: "eu.vendeli.tgbot.generated"
    FileSpec
        .builder(
            filePkg,
            "BotCtx",
        ).apply {
            val providers = resolver.getAnnotatedClassSymbols(CtxProvider::class, pkg)
            var userDataType = STRING

            val userDataCtxClass = providers
                .firstOrNull { c ->
                    c.getAllSuperTypes().firstOrNull { it.toClassName() == userDataCtx }?.also {
                        userDataType = it.arguments
                            .first()
                            .type
                            ?.resolve()
                            ?.toClassName() ?: STRING
                    } != null
                }?.takeIf {
                    it.classKind == ClassKind.CLASS
                }?.toClassName() ?: userDataCtxDef

            val classDataCtxClass = providers
                .firstOrNull { c ->
                    c.getAllSuperTypes().any { it.toClassName() == classDataCtx }
                }?.takeIf {
                    it.classKind == ClassKind.CLASS
                }?.toClassName() ?: classDataCtxDef

            addProperty(
                PropertySpec
                    .builder(
                        "_userData",
                        userDataCtxClass,
                        KModifier.PRIVATE,
                    ).apply {
                        initializer(userDataCtxClass.reflectionName() + "()")
                    }.build(),
            )

            addProperty(
                PropertySpec
                    .builder(
                        "_classData",
                        classDataCtxClass,
                        KModifier.PRIVATE,
                    ).apply {
                        initializer(classDataCtxClass.reflectionName() + "()")
                    }.build(),
            )

            addFunction(
                FunSpec
                    .builder("____clearClassData")
                    .addModifiers(KModifier.SUSPEND)
                    .addParameter("tgId", LONG)
                    .addAnnotation(InternalApi::class)
                    .addCode("return _classData.clearAll(tgId)")
                    .build(),
            )

            addProperty(
                PropertySpec
                    .builder(
                        "userData",
                        userDataCtxClass,
                    ).receiver(botClass)
                    .getter(FunSpec.getterBuilder().addCode("return _userData").build())
                    .build(),
            )

            addProperty(
                PropertySpec
                    .builder(
                        "classData",
                        classDataCtxClass,
                    ).receiver(botClass)
                    .getter(FunSpec.getterBuilder().addCode("return _classData").build())
                    .build(),
            )

            addFunction(
                FunSpec
                    .builder("get")
                    .addModifiers(KModifier.OPERATOR)
                    .receiver(userClass)
                    .addParameter("key", STRING)
                    .returns(userDataType.copy(true))
                    .addCode("return _userData[id, key]")
                    .build(),
            )

            addFunction(
                FunSpec
                    .builder("set")
                    .addModifiers(KModifier.OPERATOR)
                    .receiver(userClass)
                    .addParameter("key", STRING)
                    .addParameter("value", userDataType)
                    .addCode("return _userData.set(id, key, value)")
                    .build(),
            )
        }.build()
        .runCatching {
            writeTo(
                codeGenerator = codeGenerator,
                dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray()),
            )
        }
}
