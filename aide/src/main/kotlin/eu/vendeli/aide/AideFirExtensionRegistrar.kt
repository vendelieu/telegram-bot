@file:Suppress("ktlint:standard:curly-spacing", "ktlint:standard:unary-op-spacing")

package eu.vendeli.aide

import com.google.auto.service.AutoService
import eu.vendeli.aide.fir.AideFirCheckersExtension
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

@AutoService(FirExtensionRegistrar::class)
internal class AideFirExtensionRegistrar(
    private val doAutoSend: Boolean,
) : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +{ session: FirSession -> AideFirCheckersExtension(session, doAutoSend) }
    }
}
