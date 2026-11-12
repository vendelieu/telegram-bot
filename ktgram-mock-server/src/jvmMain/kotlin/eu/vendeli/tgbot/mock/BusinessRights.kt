package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonObject

private val REQUIRES = Regex("Requires the (can_\\w+) business bot right")
private const val REPLY_RIGHT = "can_reply"

/**
 * The business bot rights each method needs, read from the "Requires the can_x business bot right" sentence of its
 * description in the scrapped spec. The spec words three of them `can_change_*` while `BusinessBotRights` calls them
 * `can_edit_*`, so those are normalised.
 */
class BusinessRights(
    spec: ApiSpec,
) {
    private val required: Map<String, String> = spec.methods.values
        .filter { it.fieldsByName.containsKey("business_connection_id") }
        .mapNotNull { method ->
            val right = REQUIRES.find(method.description)?.groupValues?.get(1) ?: return@mapNotNull null
            method.name.lowercase() to right.replace("can_change_", "can_edit_")
        }.toMap()

    /** The right needed to call [method] on [connection], null when none applies. */
    fun needed(method: MethodSpec): String? {
        val name = method.name.lowercase()
        if (name in SELF_CHECKED || name == "getbusinessconnection") return null
        required[name]?.let { return it }
        val isOutgoing =
            method.name.startsWith("send") || method.name.startsWith("edit") || method.name.startsWith("stop")
        return if (isOutgoing) REPLY_RIGHT else null
    }

    /** `rights` absent means the connection was not given a rights object, everything is allowed then. */
    fun check(connection: JsonObject, right: String) {
        val rights = connection["rights"] as? JsonObject ?: return
        if (rights.bool(right) != true) throw ApiException.badRequest("not enough rights: $right is required")
    }

    private companion object {
        /** Methods that pick between rights themselves. */
        val SELF_CHECKED = setOf("deletebusinessmessages")
    }
}
