package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.chain.WizardStateManager
import kotlin.reflect.KClass

/**
 * Annotation used to mark a class or object that contains a wizard flow.
 * The class/object should contain nested classes/objects that extend [WizardStep].
 * Example:
 * ```
 * @WizardHandler(trigger = ["/start"])
 * object MyWizard {
 *     object NameStep : WizardStep(isInitial = true) { ... }
 *     object AgeStep : WizardStep { ... }
 * }
 * ```
 *
 * @property trigger Array of command triggers that will start the wizard.
 * @property scope Scope in which the command will be checked.
 * @property stateManagers Array of state manager classes to use for storing wizard step state.
 *                         Each state manager should implement [WizardStateManager] with a specific type parameter.
 *                         KSP will match step's store() return type to the appropriate state manager.
 *                         Can be overridden per-step using [StateManager] annotation.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class WizardHandler(
    val trigger: Array<String>,
    val scope: Array<UpdateType> = [UpdateType.MESSAGE],
    val stateManagers: Array<KClass<out WizardStateManager<*>>> = [],
) {
    /**
     * Annotation to specify a state manager for a specific wizard step.
     * Overrides the default type-based matching from the parent [WizardHandler] annotation.
     *
     * Example:
     * ```
     * @WizardHandler(trigger = ["/start"], stateManagers = [DefaultStateManager::class])
     * object MyWizard {
     *     object NameStep : WizardStep { ... } // Uses DefaultStateManager
     *
     *     @WizardHandler.StateManager(CustomStateManager::class)
     *     object AgeStep : WizardStep { ... } // Uses CustomStateManager
     * }
     * ```
     *
     * @property value The state manager class to use for this step.
     */
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.SOURCE)
    annotation class StateManager(
        val value: KClass<out WizardStateManager<*>>,
    )
}
