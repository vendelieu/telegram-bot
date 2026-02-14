---
name: add-ktnip-parameter-strategy
description: [Library contributor] Adds a new parameter resolution strategy to the ktnip KSP processor. Use when handler functions need to receive a new parameter type (e.g. ChatMember, InlineQuery), or when adding support for a new resolvable type in Activity invocation.
---

# Add Ktnip Parameter Strategy

## Workflow

### 1. Add ParameterResolutionStrategy

- In [ParameterResolutionStrategy.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/dto/ParameterResolutionStrategy.kt)
- Add new `data class` extending `ParameterResolutionStrategy`
- Include `typeName`, `isNullable`; add strategy-specific fields (e.g. `parameterName`, `updateTypeSimpleName`)

```kotlin
data class MyParam(
    override val typeName: TypeName,
    override val isNullable: Boolean,
    val myField: String,
) : ParameterResolutionStrategy()
```

### 2. Add TypeConstant (if new type)

- In [TypeConstants.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/utils/TypeConstants.kt)
- Add `val myTypeClass: TypeName = MyType::class.asTypeName()`

### 3. Update ParameterResolver

- In [ParameterResolver.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/codegen/ParameterResolver.kt)
- Add branch in `resolve()` when block:
  - Match `typeName` (use TypeConstants or `in TypeMapper.getAllTypedUpdateTypes()` for typed updates)
  - Return new strategy instance
  - Handle `@ParamMapping` for parameter name if from parameters map

### 4. Update InvocationCodeGenerator

- In [InvocationCodeGenerator.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/codegen/InvocationCodeGenerator.kt)
- Add branch in `generateParameterResolution()` when block
- Generate CodeBlock: `CodeBlock.of("val param%L = ...\n", parameterIndex, ...)`
- Add imports via `fileBuilder.addImport(...)` if needed
- Use `nullabilityMark` for `!!` when non-null

### 5. Update generateInvocationCode (if needed)

- If strategy affects `isBotNeeded`, `isUpdateNeeded`, `isParametersNeeded`, `isUserNeeded`: add to the `strategies.any { ... }` checks

## Reference

- [ParameterResolutionStrategy.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/dto/ParameterResolutionStrategy.kt)
- [ParameterResolver.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/codegen/ParameterResolver.kt)
- [InvocationCodeGenerator.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/codegen/InvocationCodeGenerator.kt)
- [TypeMapper.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/codegen/TypeMapper.kt) - for TypedUpdate: add to updateTypeMap
