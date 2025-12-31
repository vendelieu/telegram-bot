# Adding new Update part

1. Add new field to [Update](./telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/common/Update.kt)
2. Add new entry to [UpdateType](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/component/UpdateType.kt)
3. Add new [ProcessedUpdate](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/component/ProcessedUpdate.kt)
   subclass
4. Add new clause
   to [processUpdate()](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/utils/common/ProcessUpdate.kt)
5. Update KSP module:
   - Add TypeName constant to [TypeConstants](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/utils/TypeConstants.kt)
   - Add mapping entry to [TypeMapper](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/codegen/TypeMapper.kt)
6. Run helper ksp to add a new section
   for [FunctionalDSLUtils](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/utils/common/FunctionalDSLUtils.kt)

# KSP Module Structure

The KSP processor is organized into the following packages:

- **annotation/** - Annotation parsing and extraction
  - `AnnotationParser` - Parses annotation arguments into typed values
  - `AnnotationExtractor` - Extracts annotations from functions with priority handling (function > class > default)
- **codegen/** - Code generation components
  - `ActivityCodeGenerator` - Generates Activity objects from function declarations
  - `InvocationCodeGenerator` - Generates invocation code blocks for Activities
  - `ParameterResolver` - Resolves function parameters to resolution strategies
  - `TypeMapper` - Maps TypeNames to update type classes
- **collectors/** - Handler collectors
  - `BaseCollector` - Base class with common functionality
  - `CommandCollector`, `InputCollector`, `CommonCollector`, etc.
- **dto/** - Data transfer objects
  - `ParameterResolutionStrategy` - Sealed class for parameter resolution strategies
  - `ActivityMetadata`, `CollectorsContext`, etc.
- **utils/** - Utility functions and constants
  - `TypeConstants` - Centralized type name constants

# Preparing release

1. If there's some api changes hit gradle `apiDump` task.
2. Run gradle's `prepareRelease` task.