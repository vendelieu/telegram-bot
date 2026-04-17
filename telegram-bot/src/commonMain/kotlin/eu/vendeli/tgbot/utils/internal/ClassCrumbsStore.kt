package eu.vendeli.tgbot.utils.internal

internal object ClassCrumbsStore {
    private val userClassCrumbs = mutableMapOf<Long, String>()
    fun get(userId: Long) = userClassCrumbs[userId]
    fun set(userId: Long, crumb: String) {
        userClassCrumbs[userId] = crumb
    }
}
