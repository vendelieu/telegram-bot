package eu.vendeli.ksp

import eu.vendeli.ksp.utils.AbstractKspTest

class ProcessorK2Test : AbstractKspTest() {
    @Test
    fun defaultHandlers() = runTest("test-data/DefaultHandlers.kt", useK2 = true)

    @Test
    fun inputChain() = runTest("test-data/InputChain.kt", useK2 = true)

    @Test
    fun injectables() = runTest("test-data/Injectables.kt", useK2 = true)

    @Test
    fun ctxProviders() = runTest("test-data/CtxProviders.kt", useK2 = true)
}
