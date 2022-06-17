package eu.vendeli.tgbot.types

enum class ChatType(private val literal: String) {
    Private("private"), Group("group"), Supergroup("supergroup"), Channel("channel");

    override fun toString(): String = literal
}

data class Chat(
    val id: Long,
    val type: ChatType,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val photo: ChatPhoto? = null,
    val bio: String? = null,
    val hasPrivateForwards: Boolean? = null,
    val description: String? = null,
    val inviteLink: String? = null,
    val pinnedMessage: Message? = null,
    val permissions: ChatPermissions? = null,
    val slowModeDelay: Int? = null,
    val messageAutoDeleteTime: Int? = null,
    val hasProtectedContent: Boolean? = null,
    val stickerSetName: String? = null,
    val canSetStickerSet: Boolean? = null,
    val linkedChatId: Long? = null,
    val location: ChatLocation? = null,
)
