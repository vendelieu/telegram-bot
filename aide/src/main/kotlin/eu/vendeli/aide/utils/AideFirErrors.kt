package eu.vendeli.aide.utils

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactory0
import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies.NAME_IDENTIFIER
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.rendering.RootDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.warning0

object AideFirErrors : BaseDiagnosticRendererFactory() {
    val SEND_CALL_MISSING: KtDiagnosticFactory0 by warning0<PsiElement>(NAME_IDENTIFIER)

    @Suppress("ktlint:standard:property-naming")
    override val MAP: KtDiagnosticFactoryToRendererMap =
        KtDiagnosticFactoryToRendererMap("Aide").apply {
            put(
                SEND_CALL_MISSING,
                "Missing send call",
            )
        }

    init {
        RootDiagnosticRendererFactory.registerFactory(this)
    }
}
