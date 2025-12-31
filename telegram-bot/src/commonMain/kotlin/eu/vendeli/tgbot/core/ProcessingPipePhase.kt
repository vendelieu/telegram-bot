package eu.vendeli.tgbot.core

/**
 * Represents a phase in the update processing pipeline.
 */
class ProcessingPipePhase private constructor(
    val name: String,
) {
    override fun toString(): String = "ProcessingPipePhase('$name')"

    companion object {
        /**
         * Initial phase for setting up the environment and basic validation.
         */
        val Setup = ProcessingPipePhase("Setup")

        /**
         * Phase for parsing update content, commands, and enriching context.
         */
        val Parsing = ProcessingPipePhase("Parsing")

        /**
         * Phase for finding the appropriate handler (Command/Input/Common) for the update.
         */
        val Match = ProcessingPipePhase("Match")

        /**
         * Phase for checking rate limits, guards, and other execution constraints.
         */
        val Validation = ProcessingPipePhase("Validation")

        /**
         * Phase occurring immediately before the handler invocation.
         */
        val PreInvoke = ProcessingPipePhase("PreInvoke")

        /**
         * The core phase where the resolved handler is executed.
         */
        val Invoke = ProcessingPipePhase("Invoke")

        /**
         * Final phase for cleanup or post-processing after successful invocation.
         */
        val PostInvoke = ProcessingPipePhase("PostInvoke")
    }
}
