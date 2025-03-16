package eu.vendeli.aide.fir

import eu.vendeli.aide.dto.ActionMeta
import eu.vendeli.aide.dto.ActionStatementType
import eu.vendeli.aide.dto.ActionType
import eu.vendeli.aide.dto.SourceKey
import eu.vendeli.aide.utils.AideFirErrors
import eu.vendeli.aide.utils.botClassId
import eu.vendeli.aide.utils.userClassId
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirFunctionChecker
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.fir.scopes.impl.hasTypeOf
import org.jetbrains.kotlin.fir.visitors.FirVisitorVoid

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

        if (tracker.unhandledActions.isEmpty() && tracker.actionVariables.isEmpty()) return

        // Report unhandled action calls using their source positions
        declaration.accept(
            object : FirVisitorVoid() {
                override fun visitElement(element: FirElement) {
                    tracker.unhandledActions.forEach {
                        it.proceedUnhandled(element, hasBotParam, hasUserParam, reporter, context)
                    }
                    tracker.actionVariables.forEach {
                        it.proceedVariables(element, reporter, context)
                    }
                    element.acceptChildren(this)
                }
            },
            null,
        )
    }

    private fun Map.Entry<SourceKey, ActionMeta>.proceedUnhandled(
        element: FirElement,
        hasBotParam: Boolean,
        hasUserParam: Boolean,
        reporter: DiagnosticReporter,
        context: CheckerContext,
    ) {
        element.source?.let { source ->
            val isPlainCall = value.statementType == ActionStatementType.PLAIN_CALL
            val isSimpleAction = value.type == ActionType.SIMPLE_ACTION
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
                source.startOffset == key.startOffset &&
                source.endOffset == key.endOffset &&
                !irPassCondition
            ) {
                reporter.reportOn(source, AideFirErrors.SEND_CALL_MISSING, context)
            }
        }
    }

    private fun SourceKey.proceedVariables(
        element: FirElement,
        reporter: DiagnosticReporter,
        context: CheckerContext,
    ) {
        element.source?.let { source ->
            if (source.startOffset == startOffset && source.endOffset == endOffset) {
                reporter.reportOn(source, AideFirErrors.SEND_CALL_MISSING, context)
            }
        }
    }
}
