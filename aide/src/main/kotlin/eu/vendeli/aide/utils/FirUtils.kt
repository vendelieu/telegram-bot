package eu.vendeli.aide.utils

import eu.vendeli.aide.dto.SourceKey
import eu.vendeli.aide.fir.ActionCallTracker
import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.FirQualifiedAccessExpression
import org.jetbrains.kotlin.fir.packageFqName
import org.jetbrains.kotlin.fir.references.resolved
import org.jetbrains.kotlin.fir.references.symbol
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.resolve.isSubclassOf
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.resolve.toClassSymbol
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.fir.types.ConeClassLikeType
import org.jetbrains.kotlin.fir.types.lowerBoundIfFlexible
import org.jetbrains.kotlin.fir.types.resolvedType
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.toKtLightSourceElement
import org.jetbrains.kotlin.utils.addToStdlib.ifTrue

internal fun FirExpression.unwrapReceiver(): FirExpression {
    var current: FirExpression = this
    while (current is FirQualifiedAccessExpression && current.explicitReceiver != null) {
        current = current.explicitReceiver!!
    }
    return current
}

internal fun KtSourceElement?.toSourceKey(): SourceKey? = this?.let {
    SourceKey(startOffset, endOffset)
}

internal fun FirFunctionCall.isSendCall(): Boolean =
    (calleeReference.resolved?.symbol as? FirNamedFunctionSymbol)
        ?.callableId
        ?.asSingleFqName()
        ?.asString()
        ?.let { it.startsWith(API_PACKAGE) && it.endsWith(SEND_FUN_NAME) } == true

internal fun ActionCallTracker.isAction(
    call: FirExpression?,
): Boolean = call?.resolvedType?.toClassSymbol(session)?.let {
    it.isSubclassOf(actionTypeLT, session, false, true) || it.isSubclassOf(simpleActionTypeLT, session, false, true)
} == true

internal fun FirFunctionCall.isScopeFunctionCall(): Boolean = calleeReference
    .takeIf {
        it.resolved
            ?.resolvedSymbol
            ?.packageFqName()
            ?.asString() == "kotlin"
    }?.name
    ?.asString() in scopeFunctionNames

internal fun MutableSet<SourceKey>.removeIntersecting(key: SourceKey?) {
    if (key == null) return
    removeAll { it.isIntersecting(key) }
}

internal fun KtSourceElement?.checkParents(checkBlock: KtSourceElement.() -> Boolean): Boolean {
    var currentElement = this

    while (currentElement != null) {
        // Check current node's token type first
        currentElement.checkBlock().ifTrue { return true }

        // Move to parent node
        val parentNode = currentElement.treeStructure.getParent(
            currentElement.lighterASTNode,
        )

        // Convert parent node to source element
        currentElement = parentNode?.toKtLightSourceElement(currentElement.treeStructure)
    }

    // Reached root without finding target type
    return false
}

internal fun SourceKey.isIntersecting(other: SourceKey): Boolean =
    startOffset <= other.endOffset && endOffset >= other.startOffset

@OptIn(SymbolInternals::class)
internal fun FqName.resolveActionType(session: FirSession): ConeClassLikeType? =
    (session.symbolProvider.getClassLikeSymbolByClassId(ClassId.topLevel(this)) as? FirRegularClassSymbol)
        ?.fir
        ?.defaultType()
        ?.lowerBoundIfFlexible() as? ConeClassLikeType

private val scopeFunctionNames = setOf("run", "let", "apply", "also", "with")
