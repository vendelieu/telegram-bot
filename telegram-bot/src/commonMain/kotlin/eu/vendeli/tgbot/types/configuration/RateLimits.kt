package eu.vendeli.tgbot.types.configuration

/**
 * A class containing the configuration of constraints for incoming requests.
 *
 * @property period The period for which requests will be regulated. (in milliseconds)
 * @property rate The number of allowed requests for the specified period.
 */
data class RateLimits(
    var period: Long = 0L,
    var rate: Long = 0L,
) {
    internal fun prettyPrint() = "[\uD83D\uDEA6 Period: $period, Rate: $rate]"

    companion object {
        val NOT_LIMITED = RateLimits(0L, 0L)
    }
}
