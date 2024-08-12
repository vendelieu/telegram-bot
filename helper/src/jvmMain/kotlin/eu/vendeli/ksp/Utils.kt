package eu.vendeli.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import java.io.File
import java.io.FileFilter

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
internal fun Resolver.resolveSymbolsFromDir(path: String): List<KSClassDeclaration> {
    val basePkg = "eu.vendeli.tgbot.types"
    val packages = mutableListOf(basePkg)
    File(path).listFiles(FileFilter { it.isDirectory && it.name != "internal" })?.map {
        packages.add("$basePkg.${it.name}")
    }

    val output: MutableList<KSClassDeclaration> = mutableListOf()

    packages.forEach { pkg ->
        (
            getDeclarationsFromPackage(pkg).filter {
                it is KSClassDeclaration &&
                    // not enum
                    it.classKind != ClassKind.ENUM_CLASS
            } as Sequence<KSClassDeclaration>
        ).toList().let {
            output.addAll(it)
        }
    }

    return output
}
