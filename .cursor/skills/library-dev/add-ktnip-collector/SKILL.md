---
name: add-ktnip-collector
description: [Library contributor] Adds a new collector to the ktnip KSP processor. Use when implementing a new handler type (e.g. new annotation like @MyHandler), adding a collector for a new activity pattern, or extending the ktnip processor to collect additional annotated functions.
---

# Add Ktnip Collector

## Workflow

### 1. Create Annotation (if new)

- Define annotation in telegram-bot module (annotations package)
- Ensure it can be discovered via `findAnnotationRecursively` (meta-annotations supported)

### 2. Add Annotation Parser (if new annotation)

- In [AnnotationParser.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/annotation/AnnotationParser.kt): add `parseMyAnnotation(arguments)` returning parsed data
- Use `parseValueList`, `parseScopes` patterns from existing parsers

### 3. Create Collector Class

- Place in [ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/collectors/](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/collectors/)
- Extend **BaseCollector** (for activity-based handlers) or implement **Collector** directly
- Override `collect(resolver: Resolver, ctx: CollectorsContext)`

### 4. Collector Implementation

- Discovery: `resolver.getAnnotatedFnSymbols(ctx.pkg, MyAnnotation::class)`
- Parse: `function.annotations.findAnnotationRecursively(MyAnnotation::class)?.arguments`
- Metadata: `extractActivityMetadata(function)` (from BaseCollector)
- Generate: `generateAndRegisterActivity(function, metadata, ctx, parameters, updateType)`
- Register: `ctx.loadFun.addStatement("registerX(%S, %L.id)", id, activityId)`

### 5. Register in ActivityProcessor

- Add to `collectors` list in [ActivityProcessor.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/ActivityProcessor.kt) `processPackage`:
  - Order matters: BotCtxCollector, CommandCollector, InputCollector, CommonCollector, UpdateHandlerCollector, UnprocessedHandlerCollector, WizardCollector

### 6. ResolverExtensions (if needed)

- `getAnnotatedFnSymbols(pkg, MyAnnotation::class)` works for any annotation (generic)
- Custom discovery logic only if collecting non-function symbols

### 7. Add KSP Tests

- Add test data in `ktnip/src/jvmTest/resources/test-data/` (or extend DefaultHandlers.kt, WizardHandlers.kt, etc.)
- Add `runTest("test-data/YourFile.kt")` in [ProcessorTest.kt](ktnip/src/jvmTest/kotlin/eu/vendeli/ktnip/ProcessorTest.kt)
- Use `/* G-EXPECT ... */` block for golden output checks (see add-ktnip-ksp-tests skill)

## Reference

- [InputCollector.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/collectors/InputCollector.kt) - simple collector
- [CommandCollector.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/collectors/CommandCollector.kt) - multiple annotations, parameters, updateType
