package eu.vendeli.ksp

import eu.vendeli.ksp.utils.AbstractKspTest

class ProcessorK1Test : AbstractKspTest() {
    @Test
    fun defaultHandlers() = runTest("test-data/DefaultHandlers.kt")

    @Test
    fun inputChain() = runTest("test-data/InputChain.kt")

    @Test
    fun injectables() = runTest("test-data/Injectables.kt")

    @Test
    fun ctxProviders() = runTest("test-data/CtxProviders.kt")
}
