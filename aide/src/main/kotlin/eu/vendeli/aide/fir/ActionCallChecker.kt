package eu.vendeli.aide.fir

import eu.vendeli.aide.dto.ActionMeta
import eu.vendeli.aide.dto.ActionStatementType
import eu.vendeli.aide.dto.ActionType
import eu.vendeli.aide.dto.SourceKey
import eu.vendeli.aide.utils.ACTION_FQ_NAME
import eu.vendeli.aide.utils.API_PACKAGE
import eu.vendeli.aide.utils.AideFirErrors
import eu.vendeli.aide.utils.SEND_FUN_NAME
import eu.vendeli.aide.utils.SIMPLE_ACTION_FQ_NAME
import eu.vendeli.aide.utils.botClassId
import eu.vendeli.aide.utils.userClassId
import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirFunctionChecker
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.fir.declarations.FirProperty
import org.jetbrains.kotlin.fir.expressions.FirAnonymousFunctionExpression
import org.jetbrains.kotlin.fir.expressions.FirBlock
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.FirQualifiedAccessExpression
import org.jetbrains.kotlin.fir.expressions.arguments
import org.jetbrains.kotlin.fir.references.resolved
import org.jetbrains.kotlin.fir.references.symbol
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.resolve.isSubclassOf
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.resolve.toClassSymbol
import org.jetbrains.kotlin.fir.scopes.impl.hasTypeOf
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirVariableSymbol
import org.jetbrains.kotlin.fir.types.ConeClassLikeType
import org.jetbrains.kotlin.fir.types.lowerBoundIfFlexible
import org.jetbrains.kotlin.fir.types.resolvedType
import org.jetbrains.kotlin.fir.visitors.FirVisitorVoid
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

/**
 * Checker to detect unhandled Action-returning calls.
 *
 */
class ActionCallChecker(
    private val doAutoSend: Boolean,
) : FirFunctionChecker(MppCheckerKind.Platform) {
    override fun check(declaration: FirFunction, context: CheckerContext, reporter: DiagnosticReporter) {
        val tracker = ActionCallTracker(context.session)
        declaration.accept(tracker, null)

        val hasUserParam = declaration.valueParameters.any { param ->
            param.hasTypeOf(userClassId, false)
        }
        val hasBotParam = declaration.valueParameters.any { param ->
            param.hasTypeOf(botClassId, false)
        }

        if (tracker.unhandledActions.isEmpty() && tracker.variableMapping.isEmpty()) return

        // Report unhandled action calls using their source positions
        tracker.unhandledActions.forEach {
            declaration.accept(
                object : FirVisitorVoid() {
                    override fun visitElement(element: FirElement) {
                        element.source?.let { source ->
                            val isPlainCall = it.value.statementType == ActionStatementType.PLAIN_CALL
                            val isSimpleAction = it.value.type == ActionType.SIMPLE_ACTION
                            val irPassCondition = doAutoSend &&
                                isPlainCall &&
                                (
                                    isSimpleAction &&
                                        hasBotParam ||
                                        !isSimpleAction &&
                                        hasBotParam &&
                                        hasUserParam
                                )

                            if (
                                source.startOffset == it.key.startOffset &&
                                source.endOffset == it.key.endOffset &&
                                !irPassCondition
                            ) {
                                reporter.reportOn(source, AideFirErrors.SEND_CALL_MISSING, context)
                            }
                        }
                        element.acceptChildren(this, null)
                    }
                },
                null,
            )
        }

        tracker.variableMapping.keys.forEach {
            declaration.accept(
                object : FirVisitorVoid() {
                    override fun visitElement(element: FirElement) {
                        element.source?.let { source ->
                            if (source.startOffset == it.startOffset && source.endOffset == it.endOffset) {
                                reporter.reportOn(source, AideFirErrors.SEND_CALL_MISSING, context)
                            }
                        }
                        element.acceptChildren(this, null)
                    }
                },
                null,
            )
        }
    }

    private inner class ActionCallTracker(
        private val session: FirSession,
    ) : FirVisitorVoid() {
        /**
         * A set of offsets of unhandled action calls.
         */
        val unhandledActions = mutableMapOf<SourceKey, ActionMeta>()

        /**
         * A mapping of source keys to the variables that use them.
         */
        val variableMapping = mutableMapOf<SourceKey, FirVariableSymbol<*>>()

        /**
         * A stack of receiver keys.
         */
        private val receiverStack = ArrayDeque<SourceKey>()

        /**
         * A set of names of scope functions.
         */
        private val scopeFunctionNames = setOf("run", "let", "apply", "also", "with")

        /**
         * The type of Action.
         */
        private val actionTypeLT = FqName(ACTION_FQ_NAME).resolveActionType(session)?.lookupTag

        /**
         * The type of SimpleAction.
         */
        private val simpleActionTypeLT = FqName(SIMPLE_ACTION_FQ_NAME).resolveActionType(session)?.lookupTag

        override fun visitElement(element: FirElement) {
            element.acceptChildren(this, null)
        }

        override fun visitFunctionCall(call: FirFunctionCall) {
            // Visit children FIRST to ensure actions are tracked before send() is processed
            call.acceptChildren(this, null)

            when {
                isSendCall(call) -> handleSendCall(call)
                isScopeFunctionCall(call) -> handleScopeFunctionCall(call)
                isActionExpr(call) -> handleActionCall(call)
                else -> {}
            }
        }

        /**
         * Handles a send() call.
         *
         * @param call The call to handle.
         */
        private fun handleSendCall(call: FirFunctionCall) {
            val receiver = call.dispatchReceiver?.unwrapReceiver()

            when {
                // Case 1: Scope function receiver `message { ... }.run { send() }`
                receiverStack.isNotEmpty() && isActionExpr(receiver!!) -> {
                    unhandledActions.removeIntersecting(receiverStack.pop())
                }

                // Case 2: Direct chaining `message { ... }.send()`
                receiver is FirFunctionCall && isActionExpr(receiver) -> {
                    receiver.source.toSourceKey()?.let { key ->
                        unhandledActions.removeIntersecting(key)
                    }
                }
                // Case 3: Variable usage `a.send()`
                receiver is FirQualifiedAccessExpression -> {
                    (receiver.calleeReference.symbol as? FirVariableSymbol<*>)?.let { symbol ->
                        val key = symbol.source.toSourceKey()

                        variableMapping.removeIntersecting(key)
                        unhandledActions.removeIntersecting(key)
                    }
                }
            }
        }

        /**
         * Handles a scope function call.
         *
         * @param call The call to handle.
         */
        private fun handleScopeFunctionCall(call: FirFunctionCall) {
            val receiver = call.explicitReceiver ?: call.dispatchReceiver
            receiver?.let { receiver ->
                if (isActionExpr(receiver)) {
                    receiver.source.toSourceKey()?.let { key ->
                        receiverStack.add(key)
                        unhandledActions.entries
                            .find {
                                it.key.isIntersecting(key)
                            }?.value
                            ?.statementType = ActionStatementType.LAMBDA
                    }
                }
            }

            // Process lambda arguments explicitly
            call.arguments.forEach { arg ->
                when (arg) {
                    is FirBlock -> arg.statements.forEach {
                        it.accept(this, null)
                    }

                    is FirAnonymousFunctionExpression -> {
                        arg.anonymousFunction.acceptChildren(this, null)
                    }

                    else -> arg.accept(this, null)
                }
            }

            if (receiver != null && isActionExpr(receiver)) {
                receiverStack.removeLastOrNull()
            }
        }

        /**
         * Handles an action call.
         *
         * @param call The call to handle.
         */
        private fun handleActionCall(call: FirFunctionCall) {
            call.source.toSourceKey()?.let { key ->
                val actionType = call.resolvedType
                    .toClassSymbol(session)
                    ?.isSubclassOf(simpleActionTypeLT!!, session, false, true)
                    ?.let {
                        ActionType.ACTION
                    } ?: ActionType.SIMPLE_ACTION

                val statementType = when {
                    variableMapping.keys.find { it.isIntersecting(key) } != null -> ActionStatementType.VARIABLE
                    receiverStack.isEmpty() -> ActionStatementType.PLAIN_CALL
                    else -> ActionStatementType.LAMBDA
                }

                unhandledActions[key] = ActionMeta(actionType, statementType)
            }
        }

        override fun visitProperty(property: FirProperty) {
            property.initializer?.accept(this, null)
            property.initializer?.let { init ->
                if (init is FirFunctionCall && isActionExpr(init)) {
                    init.source.toSourceKey()?.let { key ->
                        variableMapping[key] = property.symbol
                    }
                }
            }
        }

        /**
         * Unwraps a receiver.
         *
         * @param receiver The receiver to unwrap.
         * @return The unwrapped receiver.
         */
        private fun FirExpression.unwrapReceiver(): FirExpression {
            var current: FirExpression = this
            while (current is FirQualifiedAccessExpression && current.explicitReceiver != null) {
                current = current.explicitReceiver!!
            }
            return current
        }

        /**
         * Converts a [KtSourceElement] to a source key.
         *
         * @param source The source element to convert.
         * @return The source key.
         */
        private fun KtSourceElement?.toSourceKey(): SourceKey? = this?.let {
            SourceKey(startOffset, endOffset)
        }

        /**
         * Checks whether the given call is a send() call.
         *
         * @param call The call to check.
         * @return Whether the call is a send() call.
         */
        private fun isSendCall(call: FirFunctionCall): Boolean =
            (call.calleeReference.resolved?.symbol as? FirNamedFunctionSymbol)
                ?.callableId
                ?.asSingleFqName()
                ?.asString()
                ?.let { it.startsWith(API_PACKAGE) && it.endsWith(SEND_FUN_NAME) } == true

        /**
         * Checks whether the given expression produces an Action.
         *
         * @param expr The expression to check.
         * @return Whether the expression produces an Action.
         */
        private fun isActionExpr(expr: FirExpression): Boolean = expr.resolvedType.toClassSymbol(session)?.let {
            actionTypeLT != null &&
                simpleActionTypeLT != null &&
                (
                    it.isSubclassOf(actionTypeLT, session, false, true) ||
                        it.isSubclassOf(simpleActionTypeLT, session, false, true)
                )
        } == true

        /**
         * Checks whether the given call is a scope function call.
         *
         * @param call The call to check.
         * @return Whether the call is a scope function call.
         */
        private fun isScopeFunctionCall(call: FirFunctionCall): Boolean =
            call.calleeReference.name.asString() in scopeFunctionNames

        /**
         * Removes intersecting source keys from the set.
         *
         * @param key The key to remove intersecting keys from.
         */
        private fun MutableMap<SourceKey, *>.removeIntersecting(key: SourceKey?) {
            if (key == null) return
            entries.removeIf { it.key.isIntersecting(key) }
        }

        private fun SourceKey.isIntersecting(other: SourceKey): Boolean =
            startOffset >= other.startOffset && endOffset <= other.endOffset

        @OptIn(SymbolInternals::class)
        private fun FqName.resolveActionType(session: FirSession): ConeClassLikeType? =
            (session.symbolProvider.getClassLikeSymbolByClassId(ClassId.topLevel(this)) as? FirRegularClassSymbol)
                ?.fir
                ?.defaultType()
                ?.lowerBoundIfFlexible() as? ConeClassLikeType
    }
}
