package eu.vendeli.aide.fir

import eu.vendeli.aide.utils.ANNOTATIONS_PKG
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.predicate.DeclarationPredicate
import org.jetbrains.kotlin.name.FqName

class AideFirCheckersExtension(
    session: FirSession,
    private val doAutoSend: Boolean,
) : FirAdditionalCheckersExtension(session) {
    private val commandHandlerFq = FqName("$ANNOTATIONS_PKG.CommandHandler")
    private val callbackQueryHandlerFq = FqName("$ANNOTATIONS_PKG.CommandHandler.CallbackQuery")
    private val inputHandlerFq = FqName("$ANNOTATIONS_PKG.InputHandler")
    private val commonHandlerFq = FqName("$ANNOTATIONS_PKG.CommonHandler")
    private val unprocessedHandlerFq = FqName("$ANNOTATIONS_PKG.UnprocessedHandler")
    private val updateHandlerFq = FqName("$ANNOTATIONS_PKG.UpdateHandler")

    override val declarationCheckers: DeclarationCheckers = object : DeclarationCheckers() {
        override val functionCheckers = setOf(ActionCallChecker(doAutoSend))
    }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        DeclarationPredicate
            .create {
                annotated(
                    commandHandlerFq,
                    callbackQueryHandlerFq,
                    inputHandlerFq,
                    commonHandlerFq,
                    unprocessedHandlerFq,
                    updateHandlerFq,
                )
            }.let { register(it) }
    }
}
