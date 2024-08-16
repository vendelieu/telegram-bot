package eu.vendeli.ksp

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.ksp.utils.FileBuilder
import eu.vendeli.ksp.utils.userClass

fun buildChainStateBindings(
    botCtxBuilder: FileBuilder,
    chainDeclaration: KSClassDeclaration,
    statefulLinks: HashSet<KSClassDeclaration>,
    logger: KSPLogger,
) = botCtxBuilder.run {
    val providerName = "${chainDeclaration.simpleName.getShortName()}AllState"
    val chainType = chainDeclaration.toClassName()
    addImport("kotlinx.coroutines", "runBlocking")

    val user = FunSpec.constructorBuilder()
        .addParameter("user", userClass)
        .build()

    val stateBindingClass = TypeSpec.classBuilder(providerName)
        .primaryConstructor(user)
        .addProperty(
            PropertySpec.builder("user", userClass)
                .initializer("user")
                .addModifiers(KModifier.PRIVATE)
                .build(),
        )

    statefulLinks.forEach { l ->
        val linkName = l.simpleName.getShortName()
        val linkQName = l.qualifiedName!!.asString()

        if (l.classKind != ClassKind.OBJECT) {
            logger.warn("$linkQName is not an object, so `getAllState` binding is skipped.")
            return@forEach
        }

        val linkType = l.getDeclaredFunctions().first {
            it.simpleName.getShortName() == "action"
        }.returnType!!.toTypeName().copy(true)

        stateBindingClass.addProperty(
            PropertySpec.builder(linkName, linkType)
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return runBlocking {\n\t $linkQName.state.get(user) \n\t}")
                        .build(),
                )
                .build(),
        )
    }

    addFunction(
        FunSpec
            .builder("getAllState")
            .receiver(userClass)
            .returns(ClassName("", providerName))
            .addParameter("chain", chainType)
            .addCode("return $providerName(this)")
            .build(),
    )

    addType(stateBindingClass.build())
}
