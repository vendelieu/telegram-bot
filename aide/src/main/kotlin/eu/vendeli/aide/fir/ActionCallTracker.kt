package eu.vendeli.aide.fir

import eu.vendeli.aide.dto.ActionMeta
import eu.vendeli.aide.dto.ActionStatementType
import eu.vendeli.aide.dto.ActionType
import eu.vendeli.aide.dto.SourceKey
import eu.vendeli.aide.utils.ACTION_FQ_NAME
import eu.vendeli.aide.utils.SIMPLE_ACTION_FQ_NAME
import eu.vendeli.aide.utils.checkParents
import eu.vendeli.aide.utils.isAction
import eu.vendeli.aide.utils.isIntersecting
import eu.vendeli.aide.utils.isScopeFunctionCall
import eu.vendeli.aide.utils.isSendCall
import eu.vendeli.aide.utils.removeIntersecting
import eu.vendeli.aide.utils.resolveActionType
import eu.vendeli.aide.utils.toSourceKey
import eu.vendeli.aide.utils.unwrapReceiver
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirProperty
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.FirQualifiedAccessExpression
import org.jetbrains.kotlin.fir.references.symbol
import org.jetbrains.kotlin.fir.resolve.isSubclassOf
import org.jetbrains.kotlin.fir.resolve.toClassSymbol
import org.jetbrains.kotlin.fir.types.resolvedType
import org.jetbrains.kotlin.fir.visitors.FirVisitorVoid
import org.jetbrains.kotlin.name.FqName

internal class ActionCallTracker(
    internal val session: FirSession,
) : FirVisitorVoid() {
    val unhandledActions = mutableMapOf<SourceKey, ActionMeta>()
    val actionVariables = mutableSetOf<SourceKey>()

    private val receiverStack = ArrayDeque<SourceKey>()

    override fun visitElement(element: FirElement) {
        element.acceptChildren(this)
    }

    override fun visitFunctionCall(call: FirFunctionCall) {
        call.acceptChildren(this)

        when {
            call.isSendCall() -> handleSendCall(call)
            call.isScopeFunctionCall() -> handleScopeFunctionCall(call)
            isAction(call) -> handleActionCall(call)
        }
    }

    override fun visitProperty(property: FirProperty) {
        property.initializer?.takeIf { it is FirFunctionCall && isAction(it) }?.let {
            actionVariables.add(it.source.toSourceKey() ?: return)
        }
    }

    private fun handleSendCall(call: FirFunctionCall) {
        val receiver = call.dispatchReceiver?.unwrapReceiver()

        when {
            // Case 1: Scope function receiver `message { ... }.run { send() }`
            receiverStack.isNotEmpty() && receiver != null && isAction(receiver) -> {
                val stackKey = receiverStack.pop()

                unhandledActions.keys.removeIntersecting(stackKey)
                actionVariables.removeIntersecting(stackKey)
            }

            // Case 2: Direct chaining `message { ... }.send()`
            receiver is FirFunctionCall && isAction(receiver) -> receiver.source.toSourceKey()?.let { key ->
                unhandledActions.keys.removeIntersecting(key)
            }

            // Case 3: Variable usage `a.send()`
            receiver is FirQualifiedAccessExpression -> receiver.calleeReference.symbol?.let { symbol ->
                val key = symbol.source.toSourceKey()

                actionVariables.removeIntersecting(key)
                unhandledActions.keys.removeIntersecting(key)
            }
        }
    }

    private fun handleScopeFunctionCall(call: FirFunctionCall) {
        val receiver = call.explicitReceiver ?: call.dispatchReceiver

        if (receiver != null && isAction(receiver)) receiver.source.toSourceKey()?.let { key ->
            receiverStack.add(key)
            unhandledActions.entries
                .find { it.key.isIntersecting(key) }
                ?.value
                ?.statementType = ActionStatementType.LAMBDA
        }

        // Explicitly check lambda arguments for send()
        call.acceptChildren(this)

        if (receiver != null && isAction(receiver)) {
            receiverStack.removeLastOrNull()
        }
    }

    private fun handleActionCall(call: FirFunctionCall) {
        if (call.source.checkParents {
                lighterASTNode.tokenType == KtNodeTypes.OBJECT_LITERAL ||
                    lighterASTNode.tokenType == KtNodeTypes.LAMBDA_EXPRESSION
            }
        ) return

        call.source.toSourceKey()?.let { key ->
            val actionType = call.resolvedType
                .toClassSymbol(session)
                ?.isSubclassOf(simpleActionTypeLT, session, false, true)
                ?.let { ActionType.ACTION } ?: ActionType.SIMPLE_ACTION

            val statementType = when {
                actionVariables.any { it.isIntersecting(key) } -> ActionStatementType.VARIABLE
                receiverStack.isEmpty() -> ActionStatementType.PLAIN_CALL
                else -> ActionStatementType.LAMBDA
            }

            unhandledActions[key] = ActionMeta(actionType, statementType)
        }
    }

    internal val actionTypeLT = requireNotNull(FqName(ACTION_FQ_NAME).resolveActionType(session)?.lookupTag)
    internal val simpleActionTypeLT =
        requireNotNull(FqName(SIMPLE_ACTION_FQ_NAME).resolveActionType(session)?.lookupTag)
}
