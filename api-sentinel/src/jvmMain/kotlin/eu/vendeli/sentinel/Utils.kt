package eu.vendeli.sentinel

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asTypeName
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.MediaAction
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.marker.Keyboard
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.utils.common.fqName
import java.io.File
import java.io.FileFilter

internal val replyMarkupType = Keyboard::class.asTypeName()
internal val entitiesType = LIST.parameterizedBy(MessageEntity::class.asTypeName())
internal val simpleActionFQ = SimpleAction::class.fqName
internal val actionFQ = Action::class.fqName
internal val mediaActionFQ = MediaAction::class.fqName

@Suppress("UNCHECKED_CAST")
internal inline fun <R> Any?.safeCast(): R? = this as? R

internal inline fun KSPLogger.invalid(message: () -> String) = exception(IllegalStateException(message()))

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

private val omittedDirectories = setOf(
    "component",
    "options",
    "configuration",
    "chain",
)

@Suppress("UNCHECKED_CAST")
@OptIn(KspExperimental::class)
internal fun Resolver.resolveSymbolsFromDir(
    path: String,
    additionalFilter: (KSDeclaration) -> Boolean = { true },
): List<KSDeclaration> {
    val basePkg = path.substringAfter("commonMain/kotlin/").replace('/', '.')
    val packages = mutableListOf(basePkg)
    File(path).listFiles(FileFilter { it.isDirectory && it.name !in omittedDirectories })?.map {
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
