package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.types.component.CommonMatcher
import eu.vendeli.tgbot.types.component.MessageKind
import eu.vendeli.tgbot.types.component.MessageReference
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.utils.common.safeCast
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

    // UpdateType -> List<Pair<MessageKindFilter, ActivityId>> (empty filter = any kind)
    private val updateTypeHandlers = mutableMapOf<UpdateType, MutableList<Pair<Set<MessageKind>, Int>>>()

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

    fun registerUpdateTypeHandler(
        type: UpdateType,
        activityId: Int,
        messageKind: Set<MessageKind> = emptySet(),
    ) {
        updateTypeHandlers.getOrPut(type) { mutableListOf() }.add(messageKind to activityId)
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
        updateTypeHandlers[type]?.mapNotNull { (_, id) -> activities[id] } ?: emptyList()

    /**
     * Returns update-type handlers eligible for [update], applying the per-handler [MessageKind]
     * filter: empty filter matches any kind; a non-empty filter only matches when the update is a
     * [MessageReference] whose detected [MessageReference.messageKind] is in the filter set.
     */
    fun getUpdateTypeHandlers(update: ProcessedUpdate): List<Activity> {
        val entries = updateTypeHandlers[update.type] ?: return emptyList()
        val kind = update.safeCast<MessageReference>()?.messageKind
        return entries.mapNotNull { (filter, id) ->
            if (filter.isEmpty() || kind in filter) activities[id] else null
        }
    }

    fun getUnprocessedHandler(): Activity? =
        unprocessedId?.let { activities[it] }

    fun getAllActivities(): Collection<Activity> = activities.values

    fun getCommandsForType(type: UpdateType): Map<String, Activity> =
        commands[type]?.mapValues { activities[it.value]!! } ?: emptyMap()

    fun prettyPrint(): String = buildString {
        appendLine("ActivityRegistry State:")

        appendLine("Command handlers:")
        if (commands.isEmpty()) appendLine("  (none)")
        commands.forEach { (type, typeCommands) ->
            appendLine("  $type:")
            typeCommands.forEach { (command, activityId) ->
                val activity = activities[activityId]
                appendLine("    \"$command\" -> ${activity?.prettyPrint()}")
            }
        }

        appendLine("\nInput handlers:")
        if (inputs.isEmpty()) appendLine("  (none)")
        inputs.forEach { (inputId, activityId) ->
            val activity = activities[activityId]
            appendLine("  \"$inputId\" -> ${activity?.prettyPrint()}")
        }

        appendLine("\nCommon handlers:")
        if (commonHandlers.isEmpty()) appendLine("  (none)")
        commonHandlers.forEach { (type, typeMatchers) ->
            appendLine("  $type:")
            typeMatchers.forEach { (matcher, activityId) ->
                val activity = activities[activityId]
                appendLine("    $matcher -> ${activity?.prettyPrint()}")
            }
        }

        appendLine("\nUpdateType handlers:")
        if (updateTypeHandlers.isEmpty()) appendLine("  (none)")
        updateTypeHandlers.forEach { (type, handlers) ->
            appendLine("  $type:")
            handlers.forEach { (filter, activityId) ->
                val activity = activities[activityId]
                val filterStr = if (filter.isEmpty()) "" else " messageKind=$filter"
                appendLine("    ->$filterStr ${activity?.prettyPrint()}")
            }
        }

        appendLine("\nUnprocessed handler:")
        unprocessedId?.let { activityId ->
            val activity = activities[activityId]
            appendLine("  -> ${activity?.prettyPrint()}")
        } ?: appendLine("  (none)")
    }
}
