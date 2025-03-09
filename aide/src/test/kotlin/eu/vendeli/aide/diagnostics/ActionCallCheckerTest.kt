package eu.vendeli.aide.diagnostics

import eu.vendeli.aide.AbstractCompilerTest

class ActionCallCheckerTest : AbstractCompilerTest() {
    @Test
    fun positiveCases() = runTest("test-data/Positive.case.kt", useK2 = true)

    @Test
    fun missingSendWarnings() = runTest("test-data/MissingSendWarnings.case.kt", useK2 = true)

    @Test
    fun variableTracking() = runTest("test-data/VariableTracking.case.kt", useK2 = true)

    @Test
    fun scopeFunctions() = runTest("test-data/ScopeFunctions.case.kt", useK2 = true)

    @Test
    fun interfaceImplementation() = runTest("test-data/InterfaceImplementation.case.kt", useK2 = true)
}
