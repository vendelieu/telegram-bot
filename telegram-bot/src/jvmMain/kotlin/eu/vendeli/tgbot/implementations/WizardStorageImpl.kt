package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.types.chain.WizardStep
import io.ktor.util.collections.ConcurrentMap

/**
 * Simple in-memory storage for wizard steps per user.
 * Stores the steps list that was returned from the wizard handler function.
 */
internal object WizardStepsStorage {
    private val storage = ConcurrentMap<Long, List<WizardStep>>()

    fun setSteps(userId: Long, steps: List<WizardStep>) {
        storage[userId] = steps
    }

    fun getSteps(userId: Long): List<WizardStep>? =
        storage[userId]

    fun removeSteps(userId: Long) {
        storage.remove(userId)
    }
}

