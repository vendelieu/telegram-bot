package eu.vendeli.ktnip.collectors

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.ksp.toClassName
import eu.vendeli.ktnip.dto.CollectorsContext
import eu.vendeli.ktnip.utils.TypeConstants.botClass
import eu.vendeli.ktnip.utils.TypeConstants.classDataCtx
import eu.vendeli.ktnip.utils.TypeConstants.classDataCtxDef
import eu.vendeli.ktnip.utils.TypeConstants.userClass
import eu.vendeli.ktnip.utils.TypeConstants.userDataCtx
import eu.vendeli.ktnip.utils.TypeConstants.userDataCtxDef
import eu.vendeli.ktnip.utils.getAnnotatedClassSymbols
import eu.vendeli.ktnip.utils.toKSPClassName
import eu.vendeli.tgbot.annotations.CtxProvider
import eu.vendeli.tgbot.annotations.internal.KtGramInternal

internal class BotCtxCollector : Collector {
    override fun collect(resolver: Resolver, ctx: CollectorsContext) {
        ctx.logger.info("Collecting context providers.")
        val providers = resolver.getAnnotatedClassSymbols(CtxProvider::class, ctx.pkg)
        var userDataType = STRING

        val userDataCtxClass = providers
            .firstOrNull { c ->
                c
                    .getAllSuperTypes()
                    .firstOrNull {
                        it.toKSPClassName() == userDataCtx
                    }?.also {
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
                c.getAllSuperTypes().any { it.toKSPClassName() == classDataCtx }
            }?.takeIf {
                it.classKind == ClassKind.CLASS
            }?.toClassName() ?: classDataCtxDef

        ctx.botCtxFile.apply {
            addProperty(
                PropertySpec
                    .builder(
                        "_userData",
                        userDataCtxClass,
                        KModifier.PRIVATE,
                    ).apply {
                        initializer("%T()", userDataCtxClass)
                    }.build(),
            )

            addType(
                TypeSpec.objectBuilder("__CtxUtils")
                    .addAnnotation(KtGramInternal::class)
                    .addSuperinterface(ClassName("eu.vendeli.tgbot.utils.common", "CtxUtils"))
                    .addProperty(
                        PropertySpec.builder(
                            "isClassDataInitialized",
                            ClassName("kotlin", "Lazy").parameterizedBy(UNIT),
                            KModifier.OVERRIDE,
                        ).initializer("lazy { %T }", UNIT)
                            .build(),
                    )
                    .addFunction(
                        FunSpec.builder("clearClassData")
                            .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
                            .addParameter("tgId", LONG)
                            .addCode("_classData.clearAll(tgId)")
                            .build(),
                    ).build(),
            )

            addProperty(
                PropertySpec
                    .builder(
                        "_classData",
                        classDataCtxClass,
                        KModifier.PRIVATE,
                    ).apply {
                        addAnnotation(
                            AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
                                .addMember("%T::class", KtGramInternal::class)
                                .build(),
                        )
                        initializer(
                            "%T().also { __CtxUtils.isClassDataInitialized.value }",
                            classDataCtxClass,
                        )
                    }.build(),
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
        }
    }
}
