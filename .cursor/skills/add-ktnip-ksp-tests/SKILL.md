---
name: add-ktnip-ksp-tests
description: Adds tests for the ktnip KSP processor. Use when adding tests for a new collector, testing processor behavior with new annotations, or verifying KSP code generation for the telegram-bot library.
---

# Add Ktnip KSP Tests

## Workflow

### 1. Test Data

- Create Kotlin source in `ktnip/src/jvmTest/resources/test-data/`
- Name: `FeatureName.kt` (e.g. NewHandler.kt)
- Must import from telegram-bot (project dependency)
- Include valid handlers with annotations being tested

### 2. Add to ProcessorTest

- In [ProcessorTest.kt](ktnip/src/jvmTest/kotlin/eu/vendeli/ktnip/ProcessorTest.kt)
- Add `@Test fun featureName() = runTest("test-data/FeatureName.kt")`

### 3. Custom Assertions (optional)

- Use `compile(vararg SourceFile, ...)` instead of `runTest` for custom checks
- Returns `JvmCompilationResult`; inspect `exitCode`, `kspSourcesDir`, generated files
- Pass custom `symbolProcessors` or `processorProviders`

## runTest Behavior

- Loads source from resources
- Compiles with ActivityProcessorProvider (default)
- Asserts `exitCode == OK`
- Verifies generated files exist (ActivitiesData.kt, BotCtx.kt)

## Reference

- [AbstractKspTest.kt](ktnip/src/jvmTest/kotlin/eu/vendeli/ktnip/utils/AbstractKspTest.kt)
- [ProcessorTest.kt](ktnip/src/jvmTest/kotlin/eu/vendeli/ktnip/ProcessorTest.kt)
- [DefaultHandlers.kt](ktnip/src/jvmTest/resources/test-data/DefaultHandlers.kt)
- [Injectables.kt](ktnip/src/jvmTest/resources/test-data/Injectables.kt)
- [WizardHandlers.kt](ktnip/src/jvmTest/resources/test-data/WizardHandlers.kt)
