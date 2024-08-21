package eu.vendeli.ksp

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.ksp.utils.FileBuilder
import eu.vendeli.ksp.utils.idLongClass

fun buildChainStateBindings(
    botCtxBuilder: FileBuilder,
    chainDeclaration: KSClassDeclaration,
    statefulLinks: HashSet<KSClassDeclaration>,
    logger: KSPLogger,
) = botCtxBuilder.run {
    val providerName = "${chainDeclaration.simpleName.getShortName()}AllState"
    val chainType = chainDeclaration.toClassName()
    val keyClasses = mutableSetOf<TypeName>()
    addImport("kotlinx.coroutines", "runBlocking")

    val stateBindingClass = TypeSpec
        .classBuilder(providerName)

    statefulLinks.forEach { l ->
        val linkName = l.simpleName.getShortName()
        val linkQName = l.qualifiedName!!.asString()

        if (l.classKind != ClassKind.OBJECT) {
            logger.warn("$linkQName is not an object, so `getAllState` binding is skipped.")
            return@forEach
        }

        val linkType = l
            .getDeclaredFunctions()
            .first {
                it.simpleName.getShortName() == "action"
            }.returnType!!
            .toTypeName()
            .copy(true)

        val stateKeyType = l.getAllProperties()
            .first {
                it.simpleName.getShortName() == "state"
            }.type.resolve().arguments.first().toTypeName()

        keyClasses.add(stateKeyType)

        stateBindingClass.addProperty(
            PropertySpec
                .builder(linkName, linkType)
                .getter(
                    FunSpec
                        .getterBuilder()
                        .addStatement("return runBlocking {\n\t $linkQName.state.get(key) \n\t}")
                        .build(),
                ).build(),
        )
    }

    keyClasses.singleOrNull()?.let { keyClass ->
        val constructorKeyFun = FunSpec
            .constructorBuilder()
            .addParameter("key", keyClass)
            .build()

        stateBindingClass
            .primaryConstructor(constructorKeyFun)
            .addProperty(
                PropertySpec
                    .builder("key", keyClass)
                    .initializer("key")
                    .addModifiers(KModifier.PRIVATE)
                    .build(),
            )

        addFunction(
            FunSpec
                .builder("getAllState").also {
                    if (keyClass == idLongClass) it.receiver(idLongClass)
                    else it.addParameter("key", keyClass)
                }
                .returns(ClassName(packageName, providerName))
                .addParameter("chain", chainType)
                .addCode("return $providerName(${if (keyClass == idLongClass) "this" else "key"})")
                .build(),
        )

        addType(stateBindingClass.build())
    }
}
