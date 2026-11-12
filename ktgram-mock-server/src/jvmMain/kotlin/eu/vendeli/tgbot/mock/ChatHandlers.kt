package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val STATUS_MEMBER = "member"
private const val STATUS_LEFT = "left"
private const val STATUS_KICKED = "kicked"
private const val STATUS_RESTRICTED = "restricted"
private const val STATUS_ADMIN = "administrator"
private const val STATUS_OWNER = "creator"
private const val MAX_COMMANDS = 100
private const val PRIVATE_CHAT_MEMBERS = 2
private val COMMAND_NAME = Regex("^[a-z0-9_]{1,32}$")
private val MEMBER_TYPES = mapOf(
    STATUS_OWNER to "ChatMemberOwner",
    STATUS_ADMIN to "ChatMemberAdministrator",
    STATUS_MEMBER to "ChatMemberMember",
    STATUS_RESTRICTED to "ChatMemberRestricted",
    STATUS_LEFT to "ChatMemberLeft",
    STATUS_KICKED to "ChatMemberBanned",
)
private val CHAT_ACTIONS = setOf(
    "typing",
    "upload_photo",
    "record_video",
    "upload_video",
    "record_voice",
    "upload_voice",
    "upload_document",
    "choose_sticker",
    "find_location",
    "record_video_note",
    "upload_video_note",
)
private val PRESENT = setOf(STATUS_OWNER, STATUS_ADMIN, STATUS_MEMBER, STATUS_RESTRICTED)

internal object ChatHandlers {
    private fun CallContext.userId(): Long =
        params.long("user_id") ?: throw ApiException.badRequest("invalid user_id specified")

    fun statusOf(ctx: CallContext, chat: ChatState, userId: Long): String {
        chat.members[userId]?.let { return it }
        return when {
            userId == ctx.bot.id -> if (chat.isPrivate) STATUS_MEMBER else STATUS_ADMIN

            chat.isPrivate && userId == chat.id -> STATUS_MEMBER

            else -> if (ctx.bot.users.containsKey(userId) ||
                chat.isPrivate
            ) STATUS_LEFT else throw ApiException.badRequest("user not found")
        }
    }

    private fun CallContext.memberJson(chat: ChatState, userId: Long): JsonObject {
        val status = statusOf(this, chat, userId)
        val user = bot.users[userId] ?: if (userId == bot.id) bot.user else JsonObject(
            mapOf(
                "id" to JsonPrimitive(userId),
                "is_bot" to JsonPrimitive(false),
                "first_name" to JsonPrimitive("Mock"),
            ),
        )
        val overrides = mapOf("status" to JsonPrimitive(status), "user" to user) +
            (if (status == STATUS_RESTRICTED) mapOf("is_member" to JsonPrimitive(true)) else emptyMap()) +
            (
                if (status == STATUS_ADMIN ||
                    status == STATUS_OWNER
                ) chat.customTitles[userId]?.let { mapOf("custom_title" to JsonPrimitive(it)) } else null
            ).orEmpty() +
            (
                if (status == STATUS_MEMBER ||
                    status == STATUS_RESTRICTED
                ) chat.tags[userId]?.let { mapOf("tag" to JsonPrimitive(it)) } else null
            ).orEmpty()
        return generator.generateObject(MEMBER_TYPES.getValue(status), overrides = overrides)!!
    }

    val getChat: Handler = {
        val chat = chat()
        val pinned = chat.pinnedMessageId?.let { chat.messages[it] }
        val overrides = buildMap<String, JsonElement> {
            putAll(chat.toJson())
            putAll(ChatAdminHandlers.fullInfoOverrides(chat, bot.extra))
            pinned?.let { put("pinned_message", it) }
        }
        generator.generateObject("ChatFullInfo", overrides = overrides)!!
    }

    val getChatMember: Handler = {
        memberJson(chat(), userId())
    }

    val getChatMemberCount: Handler = {
        val chat = chat()
        if (chat.isPrivate) {
            JsonPrimitive(PRIVATE_CHAT_MEMBERS)
        } else {
            JsonPrimitive(chat.members.values.count { it in PRESENT } + if (chat.members.containsKey(bot.id)) 0 else 1)
        }
    }

    val getChatAdministrators: Handler = {
        val chat = chat()
        val includeBots = params.bool("return_bots") == true
        val admins = chat.members.filterValues { it == STATUS_ADMIN || it == STATUS_OWNER }.keys
        JsonArray(
            admins
                .map { memberJson(chat, it) }
                .filter { includeBots || (it["user"] as? JsonObject)?.bool("is_bot") != true },
        )
    }

    private fun CallContext.changeMember(
        status: String,
        allowedIn: (ChatState) -> Boolean = {
            !it.isPrivate
        },
    ): JsonElement {
        val chat = chat()
        val id = userId()
        if (!allowedIn(chat)) throw ApiException.badRequest("method is available only in supergroup and channel chats")
        chat.members[id] = status
        return TRUE
    }

    val ban: Handler = { changeMember(STATUS_KICKED) }
    val restrict: Handler = { changeMember(STATUS_RESTRICTED) { it.type == "supergroup" } }
    val promote: Handler = { changeMember(STATUS_ADMIN) { it.type == "supergroup" || it.type == "channel" } }

    val unban: Handler = {
        val chat = chat()
        val id = userId()
        val onlyIfBanned = params.bool("only_if_banned") ?: false
        val current = chat.members[id]
        if (current == STATUS_KICKED || !onlyIfBanned) chat.members[id] = STATUS_LEFT
        TRUE
    }

    val leave: Handler = {
        val chat = chat()
        bot.chats.remove(chat.id)
        TRUE
    }

    val pin: Handler = {
        val chat = chat()
        val id = params.long("message_id") ?: throw ApiException.badRequest("message_id is empty")
        if (chat.messages[id] == null) throw ApiException.badRequest("message to pin not found")
        chat.pinnedMessageId = id
        TRUE
    }

    val unpin: Handler = {
        val chat = chat()
        val id = params.long("message_id")
        if (id == null || chat.pinnedMessageId == id) chat.pinnedMessageId = null
        TRUE
    }

    val unpinAll: Handler = {
        chat().pinnedMessageId = null
        TRUE
    }

    val chatAction: Handler = {
        deliveryChat()
        val action = params.str("action").orEmpty()
        // The scrapped description lists the actions in prose, without quotes, so they cannot be derived from it.
        if (action !in CHAT_ACTIONS) throw ApiException.badRequest("wrong parameter action in request")
        TRUE
    }

    private fun CallContext.commandsKey(): String =
        (params["scope"] as? JsonObject)?.toString().orEmpty() + "|" + params.str("language_code").orEmpty()

    val setMyCommands: Handler = {
        val commands = (params["commands"] as? JsonArray).orEmpty().mapNotNull { it as? JsonObject }
        if (commands.size > MAX_COMMANDS) throw ApiException.badRequest("too many commands specified")
        for (c in commands) {
            if (!COMMAND_NAME.matches(c.str("command").orEmpty())) throw ApiException.badRequest("BOT_COMMAND_INVALID")
            val description = c.str("description").orEmpty()
            if (description.isEmpty() || description.length > MAX_DESCRIPTION) {
                throw ApiException.badRequest("BOT_COMMAND_DESCRIPTION_INVALID")
            }
        }
        bot.commands[commandsKey()] = JsonArray(commands)
        TRUE
    }

    val getMyCommands: Handler = { bot.commands[commandsKey()] ?: JsonArray(emptyList()) }

    val deleteMyCommands: Handler = {
        bot.commands.remove(commandsKey())
        TRUE
    }

    val getMe: Handler = {
        generator.generateObject(
            "User",
            overrides = bot.user + mapOf(
                "can_join_groups" to JsonPrimitive(true),
                "can_read_all_group_messages" to JsonPrimitive(false),
                "supports_inline_queries" to JsonPrimitive(false),
            ),
        )!!
    }

    val answerCallback: Handler = {
        val id = params.str("callback_query_id").orEmpty()
        val text = params.str("text")
        if (text != null && text.length > MAX_CALLBACK_TEXT) throw ApiException.badRequest("MESSAGE_TOO_LONG")
        if (bot.callbackQueries.replace(id, false, true).not()) {
            throw ApiException.badRequest("query is too old and response timeout expired or query ID is invalid")
        }
        TRUE
    }

    val answerInline: Handler = {
        if (params.str("inline_query_id").isNullOrEmpty()) throw ApiException.badRequest("QUERY_ID_INVALID")
        TRUE
    }

    private const val MAX_DESCRIPTION = 256
    private const val MAX_CALLBACK_TEXT = 200

    fun userJson(
        id: Long,
        firstName: String,
        username: String?,
        lastName: String? = null,
    ): JsonObject = buildJsonObject {
        put("id", id)
        put("is_bot", false)
        put("first_name", firstName)
        lastName?.let { put("last_name", it) }
        username?.let { put("username", it) }
        put("language_code", "en")
    }
}
