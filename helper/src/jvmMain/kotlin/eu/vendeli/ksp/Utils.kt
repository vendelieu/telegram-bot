package eu.vendeli.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asTypeName
import eu.vendeli.tgbot.interfaces.marker.Keyboard
import eu.vendeli.tgbot.types.msg.MessageEntity
import java.io.File
import java.io.FileFilter

internal val replyMarkupType = Keyboard::class.asTypeName()
internal val entitiesType = LIST.parameterizedBy(MessageEntity::class.asTypeName())

internal fun String.beginWithUpperCase(): String = when (this.length) {
    0 -> ""
    1 -> uppercase()
    else -> this[0].uppercase() + this.substring(1)
}

internal fun String.snakeToCamelCase() = split('_')
    .mapIndexed { i, it ->
        if (i == 0) return@mapIndexed it
        it.beginWithUpperCase()
    }.joinToString("")

@Suppress("UNCHECKED_CAST")
@OptIn(KspExperimental::class)
internal fun Resolver.resolveSymbolsFromDir(
    path: String,
    additionalFilter: (KSDeclaration) -> Boolean = { true },
): List<KSDeclaration> {
    val basePkg = path.substringAfter("commonMain/kotlin/").replace('/', '.')
    val packages = mutableListOf(basePkg)
    File(path).listFiles(FileFilter { it.isDirectory && it.name != "internal" })?.map {
        packages.add("$basePkg.${it.name}")
    }

    val output: MutableList<KSDeclaration> = mutableListOf()

    packages.forEach { pkg ->
        (
            getDeclarationsFromPackage(pkg).filter {
                additionalFilter(it)
            } as Sequence<KSClassDeclaration>
        ).toList().let {
            output.addAll(it)
        }
    }

    return output
}
