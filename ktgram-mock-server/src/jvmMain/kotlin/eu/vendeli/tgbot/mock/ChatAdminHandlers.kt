package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

private const val MAX_TITLE = 128
private const val MIN_MEMBERS_FOR_STICKER_SET = 100
private const val MAX_DESCRIPTION = 255
private const val MAX_LINK_NAME = 32
private const val MAX_MEMBER_LIMIT = 99_999
private const val GENERAL_TOPIC_ID = 1L
private const val NOT_MODIFIED = "TOPIC_NOT_MODIFIED"
private val DEFAULT_TOPIC_COLOR = 0x6FB9F0L

/** Chat settings, invite links, join requests, forum topics and reactions. */
internal object ChatAdminHandlers {
    private fun CallContext.group(): ChatState {
        val chat = chat()
        if (chat.isPrivate) throw ApiException.badRequest("method is not available in private chats")
        return chat
    }

    val setTitle: Handler = {
        val chat = group()
        val title = params.str("title").orEmpty()
        if (title.isEmpty() || title.codePointCount(0, title.length) > MAX_TITLE) {
            throw ApiException.badRequest("chat title must be 1-$MAX_TITLE characters long")
        }
        chat.title = title
        TRUE
    }

    val setDescription: Handler = {
        val chat = group()
        val description = params.str("description").orEmpty()
        if (description.length > MAX_DESCRIPTION) throw ApiException.badRequest("chat description is too long")
        chat.description = description.ifEmpty { null }
        TRUE
    }

    val setPhoto: Handler = {
        val chat = group()
        if (params.str("photo")?.startsWith("attach://") != true) throw ApiException.badRequest("PHOTO_INVALID")
        chat.hasPhoto = true
        TRUE
    }

    val deletePhoto: Handler = {
        group().hasPhoto = false
        TRUE
    }

    val setPermissions: Handler = {
        val chat = group()
        chat.permissions = params["permissions"] as? JsonObject
        TRUE
    }

    val setStickerSet: Handler = {
        val chat = group()
        requireStickerSetRights(chat)
        val name = params.str("sticker_set_name").orEmpty()
        if (!bot.extra.stickerSets.containsKey(name)) throw ApiException.badRequest("STICKERSET_INVALID")
        chat.stickerSetName = name
        TRUE
    }

    /** Telegram only lets supergroups with at least this many members have their own sticker set. */
    private fun requireStickerSetRights(chat: ChatState) {
        if (chat.type != "supergroup") throw ApiException.badRequest("method is available only in supergroups")
        if (chat.members.size < MIN_MEMBERS_FOR_STICKER_SET) {
            throw ApiException.badRequest("can't set supergroup sticker set")
        }
    }

    val deleteStickerSet: Handler = {
        val chat = group()
        requireStickerSetRights(chat)
        chat.stickerSetName = null
        TRUE
    }

    fun fullInfoOverrides(chat: ChatState, extra: ExtraState): Map<String, JsonElement> = buildMap {
        if (chat.isPrivate) {
            extra.emojiStatus[chat.id]?.let { status -> putAll(status) }
        }
        chat.description?.let { put("description", JsonPrimitive(it)) }
        chat.permissions?.let { put("permissions", it) }
        chat.stickerSetName?.let { put("sticker_set_name", JsonPrimitive(it)) }
        chat.primaryInviteLink?.let { put("invite_link", JsonPrimitive(it)) }
        if (chat.isForum) put("is_forum", JsonPrimitive(true))
    }

    // --- invite links ---------------------------------------------------------------------------------------

    private fun CallContext.newLink(chat: ChatState, base: JsonObject): JsonObject {
        val name = params.str("name")
        if (name != null && name.length > MAX_LINK_NAME) throw ApiException.badRequest("invite link name is too long")
        val limit = params.long("member_limit")
        if (limit != null && limit !in 1..MAX_MEMBER_LIMIT) throw ApiException.badRequest("invalid member_limit")
        val joinRequest = params.bool("creates_join_request") == true
        if (joinRequest && limit != null) {
            throw ApiException.badRequest("member_limit can't be specified for join request links")
        }
        val link = base.with("invite_link" to JsonPrimitive("https://t.me/+mock${bot.extra.next()}"))
        return link
            .with("creates_join_request" to JsonPrimitive(joinRequest))
            .also { chat.inviteLinks[it.str("invite_link")!!] = it }
    }

    private fun CallContext.baseLink(extra: Map<String, JsonElement> = emptyMap()): JsonObject = JsonObject(
        buildMap {
            put("creator", bot.user)
            put("is_primary", JsonPrimitive(false))
            put("is_revoked", JsonPrimitive(false))
            listOf("name", "expire_date", "member_limit", "subscription_period", "subscription_price").forEach { key ->
                params[key]?.let { put(key, it) }
            }
            putAll(extra)
        },
    )

    val createLink: Handler = {
        val chat = group()
        newLink(chat, baseLink())
    }

    val createSubscriptionLink: Handler = {
        val chat = group()
        if (chat.type != "channel") throw ApiException.badRequest("PRICING_CHAT_INVALID")
        newLink(chat, baseLink())
    }

    val exportLink: Handler = {
        val chat = group()
        chat.primaryInviteLink?.let { chat.inviteLinks.remove(it) }
        val primary = newLink(chat, baseLink(mapOf("is_primary" to JsonPrimitive(true))))
        chat.primaryInviteLink = primary.str("invite_link")
        JsonPrimitive(chat.primaryInviteLink)
    }

    private fun CallContext.existingLink(chat: ChatState): JsonObject {
        val url = params.str("invite_link").orEmpty()
        val link = chat.inviteLinks[url] ?: throw ApiException.badRequest("INVITE_HASH_EXPIRED")
        if (link.bool("is_revoked") == true) throw ApiException.badRequest("INVITE_HASH_EXPIRED")
        return link
    }

    val editLink: Handler = {
        val chat = group()
        val link = existingLink(chat)
        val changed = JsonObject(
            link + listOf("name", "expire_date", "member_limit", "creates_join_request", "subscription_period")
                .mapNotNull { key -> params[key]?.let { key to it } },
        )
        chat.inviteLinks[link.str("invite_link")!!] = changed
        changed
    }

    val revokeLink: Handler = {
        val chat = group()
        val link = existingLink(chat)
        val revoked = link.with("is_revoked" to JsonPrimitive(true))
        chat.inviteLinks[link.str("invite_link")!!] = revoked
        revoked
    }

    // --- join requests --------------------------------------------------------------------------------------

    private fun CallContext.resolveRequest(status: String): JsonElement {
        val chat = group()
        val userId = params.long("user_id") ?: throw ApiException.badRequest("invalid user_id specified")
        if (!chat.joinRequests.remove(userId)) {
            val isMember = chat.members[userId] in setOf("member", "administrator", "creator", "restricted")
            throw ApiException.badRequest(
                if (isMember &&
                    status == "member"
                ) "USER_ALREADY_PARTICIPANT" else "HIDE_REQUESTER_MISSING",
            )
        }
        chat.members[userId] = status
        return TRUE
    }

    val approveJoin: Handler = { resolveRequest("member") }
    val declineJoin: Handler = { resolveRequest("left") }

    // --- forum topics ---------------------------------------------------------------------------------------

    private fun CallContext.forum(): ChatState {
        val chat = group()
        if (!chat.isForum) throw ApiException.badRequest("the chat is not a forum")
        return chat
    }

    private fun CallContext.topicId(chat: ChatState): Long {
        val id = params.long("message_thread_id") ?: throw ApiException.badRequest("message_thread_id is empty")
        if (!chat.topics.containsKey(id)) throw ApiException.badRequest("message thread not found")
        return id
    }

    private fun CallContext.validName(): String {
        val name = params.str("name").orEmpty()
        if (name.isEmpty() || name.length > MAX_TITLE) throw ApiException.badRequest("TOPIC_NAME_INVALID")
        return name
    }

    val createTopic: Handler = {
        val chat = forum()
        val name = validName()
        // The General topic owns id 1, created topics get the next free id like in Telegram.
        var id = chat.nextMessageId()
        while (id == GENERAL_TOPIC_ID) id = chat.nextMessageId()
        val topic = JsonObject(
            buildMap {
                put("message_thread_id", JsonPrimitive(id))
                put("name", JsonPrimitive(name))
                put("icon_color", JsonPrimitive(params.long("icon_color") ?: DEFAULT_TOPIC_COLOR))
                params["icon_custom_emoji_id"]?.let { put("icon_custom_emoji_id", it) }
            },
        )
        chat.topics[id] = topic
        topic
    }

    val editTopic: Handler = {
        val chat = forum()
        val id = topicId(chat)
        val topic = chat.topics.getValue(id)
        val name = params.str("name")?.let { validName() }
        val updated = topic
            .let { t -> name?.let { t.with("name" to JsonPrimitive(it)) } ?: t }
            .let { t -> params["icon_custom_emoji_id"]?.let { t.with("icon_custom_emoji_id" to it) } ?: t }
        if (updated == topic) throw ApiException.badRequest(NOT_MODIFIED)
        chat.topics[id] = updated
        TRUE
    }

    val closeTopic: Handler = {
        val chat = forum()
        if (!chat.closedTopics.add(topicId(chat))) throw ApiException.badRequest(NOT_MODIFIED)
        TRUE
    }

    val reopenTopic: Handler = {
        val chat = forum()
        if (!chat.closedTopics.remove(topicId(chat))) throw ApiException.badRequest(NOT_MODIFIED)
        TRUE
    }

    val deleteTopic: Handler = {
        val chat = forum()
        val id = topicId(chat)
        chat.topics.remove(id)
        chat.closedTopics.remove(id)
        chat.messages.entries.removeIf { it.value.long("message_thread_id") == id }
        TRUE
    }

    val unpinAllTopicMessages: Handler = {
        val chat = forum()
        topicId(chat)
        TRUE
    }

    val editGeneral: Handler = {
        val chat = forum()
        val name = validName()
        if (chat.generalName == name) throw ApiException.badRequest(NOT_MODIFIED)
        chat.generalName = name
        TRUE
    }

    val closeGeneral: Handler = {
        val chat = forum()
        if (chat.generalClosed) throw ApiException.badRequest(NOT_MODIFIED)
        chat.generalClosed = true
        TRUE
    }

    val reopenGeneral: Handler = {
        val chat = forum()
        if (!chat.generalClosed) throw ApiException.badRequest(NOT_MODIFIED)
        chat.generalClosed = false
        TRUE
    }

    val hideGeneral: Handler = {
        val chat = forum()
        if (chat.generalHidden) throw ApiException.badRequest(NOT_MODIFIED)
        chat.generalHidden = true
        chat.generalClosed = true
        TRUE
    }

    val unhideGeneral: Handler = {
        val chat = forum()
        if (!chat.generalHidden) throw ApiException.badRequest(NOT_MODIFIED)
        chat.generalHidden = false
        TRUE
    }

    val iconStickers: Handler = {
        JsonArray(
            listOf(
                generator.generateObject(
                    "Sticker",
                    overrides = mapOf(
                        "type" to JsonPrimitive("custom_emoji"),
                        "custom_emoji_id" to JsonPrimitive("5434144690511290129"),
                    ),
                )!!,
            ),
        )
    }

    /** Checks the topic a message is sent to: it has to exist and must not be closed. */
    fun requireOpenTopic(chat: ChatState, threadId: Long?) {
        if (threadId == null || !chat.isForum || threadId == GENERAL_TOPIC_ID) return
        if (!chat.topics.containsKey(threadId)) throw ApiException.badRequest("message thread not found")
        if (threadId in chat.closedTopics) throw ApiException.badRequest("TOPIC_CLOSED")
    }

    // --- reactions ------------------------------------------------------------------------------------------

    val setReaction: Handler = {
        val chat = chat()
        val id = params.long("message_id") ?: throw ApiException.badRequest("message_id is empty")
        if (chat.messages[id] == null) throw ApiException.badRequest("message to react not found")
        val reactions = params["reaction"] as? JsonArray ?: JsonArray(emptyList())
        if (reactions.isEmpty()) chat.reactions.remove(id) else chat.reactions[id] = reactions
        TRUE
    }

    val deleteReaction: Handler = {
        val chat = chat()
        val id = params.long("message_id") ?: throw ApiException.badRequest("message_id is empty")
        if (chat.messages[id] == null) throw ApiException.badRequest("message to react not found")
        chat.reactions.remove(id)
        TRUE
    }

    val deleteAllReactions: Handler = {
        chat().reactions.clear()
        TRUE
    }
}
