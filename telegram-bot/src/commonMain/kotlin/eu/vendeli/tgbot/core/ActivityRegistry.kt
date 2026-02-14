package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.types.component.CommonMatcher
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.utils.internal.prettyPrint

/**
 * Registry for activities with hierarchical indexes.
 *
 * Structure:
 * - activities: Map<ActivityId, Activity>
 * - commands: Map<UpdateType, Map<Command, ActivityId>>
 * - inputs: Map<InputId, ActivityId>
 * - matchers: Map<UpdateType, List<Pair<MatchPattern, ActivityId>>>
 * - updateTypeHandlers: Map<UpdateType, List<ActivityId>>
 * - unprocessed: ActivityId?
 */
class ActivityRegistry internal constructor() {
    // Core activity storage
    private val activities = mutableMapOf<Int, Activity>()

    // UpdateType -> Command -> ActivityId
    private val commands = mutableMapOf<UpdateType, MutableMap<String, Int>>()

    // InputId -> ActivityId
    private val inputs = mutableMapOf<String, Int>()

    // UpdateType -> List<Pattern, ActivityId> (order matters for matching)
    private val commonHandlers = mutableMapOf<UpdateType, MutableList<Pair<CommonMatcher, Int>>>()

    // UpdateType -> List<ActivityId> (multiple handlers per type allowed)
    private val updateTypeHandlers = mutableMapOf<UpdateType, MutableList<Int>>()

    // Fallback
    private var unprocessedId: Int? = null

    fun clear() {
        activities.clear()
        commands.clear()
        inputs.clear()
        commonHandlers.clear()
        updateTypeHandlers.clear()
        unprocessedId = null
    }

    // ===== Registration =====

    fun registerActivity(activity: Activity) {
        activities[activity.id] = activity
    }

    fun registerCommand(command: String, type: UpdateType, activityId: Int) {
        commands.getOrPut(type) { mutableMapOf() }[command] = activityId
    }

    fun registerInput(inputId: String, activityId: Int) {
        inputs[inputId] = activityId
    }

    fun registerCommonHandler(pattern: CommonMatcher, type: UpdateType, activityId: Int) {
        commonHandlers.getOrPut(type) { mutableListOf() }.add(pattern to activityId)
    }

    fun registerUpdateTypeHandler(type: UpdateType, activityId: Int) {
        updateTypeHandlers.getOrPut(type) { mutableListOf() }.add(activityId)
    }

    fun registerUnprocessed(activityId: Int) {
        unprocessedId = activityId
    }

    // ===== Lookup =====

    fun getActivity(id: Int): Activity? = activities[id]

    fun findCommand(command: String, context: ProcessingContext): Activity? =
        commands[context.update.type]?.get(command)?.let { activities[it] }

    fun findInput(inputId: String): Activity? =
        inputs[inputId]?.let { activities[it] }

    suspend fun findCommonHandler(input: String, context: ProcessingContext): Activity? =
        commonHandlers[context.update.type]
            ?.firstOrNull { (pattern, _) -> pattern.match(input, context) }
            ?.second
            ?.let { activities[it] }

    fun getUpdateTypeHandlers(type: UpdateType): List<Activity> =
        updateTypeHandlers[type]?.mapNotNull { activities[it] } ?: emptyList()

    fun getUnprocessedHandler(): Activity? =
        unprocessedId?.let { activities[it] }

    fun getAllActivities(): Collection<Activity> = activities.values

    fun getCommandsForType(type: UpdateType): Map<String, Activity> =
        commands[type]?.mapValues { activities[it.value]!! } ?: emptyMap()

    fun prettyPrint(): String = buildString {
        appendLine("ActivityRegistry State:")

        appendLine("Commands:")
        if (commands.isEmpty()) appendLine("  (none)")
        commands.forEach { (type, typeCommands) ->
            appendLine("  $type")
            typeCommands.forEach { (command, activityId) ->
                val activity = activities[activityId]
                appendLine("    \"$command\" -> ${activity?.prettyPrint()}")
            }
        }

        appendLine("\nInputs:")
        if (inputs.isEmpty()) appendLine("  (none)")
        inputs.forEach { (inputId, activityId) ->
            val activity = activities[activityId]
            appendLine("  \"$inputId\" -> ${activity?.prettyPrint()}")
        }

        appendLine("\nCommon Handlers:")
        if (commonHandlers.isEmpty()) appendLine("  (none)")
        commonHandlers.forEach { (type, typeMatchers) ->
            appendLine("  $type")
            typeMatchers.forEach { (matcher, activityId) ->
                val activity = activities[activityId]
                appendLine("    $matcher -> ${activity?.prettyPrint()}")
            }
        }

        appendLine("\nUpdate Type Handlers:")
        if (updateTypeHandlers.isEmpty()) appendLine("  (none)")
        updateTypeHandlers.forEach { (type, handlers) ->
            appendLine("  $type")
            handlers.forEach { activityId ->
                val activity = activities[activityId]
                appendLine("    -> ${activity?.prettyPrint()}")
            }
        }

        appendLine("\nUnprocessed Handler:")
        unprocessedId?.let { activityId ->
            val activity = activities[activityId]
            appendLine("  -> ${activity?.prettyPrint()}")
        } ?: appendLine("  (none)")
    }
}
