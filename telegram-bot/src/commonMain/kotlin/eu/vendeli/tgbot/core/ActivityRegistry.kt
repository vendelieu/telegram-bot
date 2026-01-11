package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.types.component.CommonMatcher
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.UpdateType

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
    private val matchers = mutableMapOf<UpdateType, MutableList<Pair<CommonMatcher, Int>>>()

    // UpdateType -> List<ActivityId> (multiple handlers per type allowed)
    private val updateTypeHandlers = mutableMapOf<UpdateType, MutableList<Int>>()

    // Fallback
    private var unprocessedId: Int? = null

    fun clear() {
        activities.clear()
        commands.clear()
        inputs.clear()
        matchers.clear()
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

    fun registerMatcher(pattern: CommonMatcher, type: UpdateType, activityId: Int) {
        matchers.getOrPut(type) { mutableListOf() }.add(pattern to activityId)
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

    suspend fun findMatcher(input: String, context: ProcessingContext): Activity? =
        matchers[context.update.type]
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
}
