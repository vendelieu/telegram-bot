package eu.vendeli.ktnip

import eu.vendeli.ktnip.utils.AbstractKspTest

class ProcessorTest : AbstractKspTest() {
    @Test
    fun defaultHandlers() = runTest("test-data/DefaultHandlers.kt")

    @Test
    fun wizardHandlers() = runTest("test-data/WizardHandlers.kt")

    @Test
    fun injectables() = runTest("test-data/Injectables.kt")

    @Test
    fun ctxProviders() = runTest("test-data/CtxProviders.kt")
}
