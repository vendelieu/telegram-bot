---
name: add-ktnip-ksp-tests
description: [Library contributor] Adds tests for the ktnip KSP processor. Use when adding tests for a new collector, testing processor behavior with new annotations, or verifying KSP code generation for the telegram-bot library.
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
- Asserts `exitCode == OK` (or `COMPILATION_ERROR` when error directives present)
- Verifies generated files exist (ActivitiesData.kt, KtGramCtxLoader.kt, BotCtx.kt)
- Parses `/* G-EXPECT ... */` blocks for golden output checks

## Golden Output

**Two modes (hybrid):**

1. **Golden files** (preferred): If `test-data/golden/<TestName>/<FileName>.gold` exists, full file content is compared.
2. **G-EXPECT** (fallback): When no golden exists, use inline `/* G-EXPECT ... */` block in test data.

**G-EXPECT is optional** when golden files exist — expectations are discovered from golden files automatically.

**Update golden files:** `./gradlew :ktnip:updateGolden` — run when generated output changes intentionally.

## G-EXPECT (fallback)

Add `/* G-EXPECT ... */` block to test data when no golden file exists:

```kotlin
/* G-EXPECT
file=KtGramCtxLoader.kt
contains="registerCommand"
contains="registerActivity"

file=BotCtx.kt
contains="userData"
contains="getState"
notContains="TODO"

noError
*/
```

**Directives:** `file=`, `contains="..."`, `notContains="..."`, `matches="regex"`, `notMatches="regex"`, `noError`, `error contains=`, `error exact=`, `error count=`, `error file=X line=Y contains=`

## Reference

- [AbstractKspTest.kt](ktnip/src/jvmTest/kotlin/eu/vendeli/ktnip/utils/AbstractKspTest.kt)
- [ksptest/](ktnip/src/jvmTest/kotlin/eu/vendeli/ktnip/utils/ksptest/) — GExpectParser, ExpectationAssertor, GoldenFileHandler, etc.
- [ProcessorTest.kt](ktnip/src/jvmTest/kotlin/eu/vendeli/ktnip/ProcessorTest.kt)
- [DefaultHandlers.kt](ktnip/src/jvmTest/resources/test-data/DefaultHandlers.kt)
- [WizardHandlers.kt](ktnip/src/jvmTest/resources/test-data/WizardHandlers.kt)
- [Injectables.kt](ktnip/src/jvmTest/resources/test-data/Injectables.kt)
- [CtxProviders.kt](ktnip/src/jvmTest/resources/test-data/CtxProviders.kt)
