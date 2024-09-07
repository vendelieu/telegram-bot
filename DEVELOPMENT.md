# Adding new Update part

1. Add new field to [Update](./telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/Update.kt)
2. Add new entry to [UpdateType](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/internal/UpdateType.kt)
3. Add new [ProcessedUpdate](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/internal/ProcessedUpdate.kt)
   subclass
4. Add new clause to [processUpdate()](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/utils/ProcessUpdate.kt)
5. Add [TypeRef](ksp/src/jvmMain/kotlin/eu/vendeli/ksp/utils/HelperUtils.kt) to ksp and
   to [lambda builder](ksp/src/jvmMain/kotlin/eu/vendeli/ksp/InvocationLambdaBuilder.kt)
6. Run helper ksp to add a new section for [FunctionalDSLUtils](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/utils/FunctionalDSLUtils.kt)