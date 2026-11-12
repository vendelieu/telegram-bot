package utils

import eu.vendeli.tgbot.mock.BotHandle

/**
 * The account the specs were written against, rebuilt on the mock: a user, a forum supergroup with four members
 * (one of them the creator, the bot is an administrator), a channel and a registered game. Everything else is
 * unknown to the mock, so the specs that expect "chat not found" or "user not found" for made-up ids still get it.
 */
object TestWorld {
    const val GROUP_MEMBER_1 = 100_000_002L
    const val GROUP_MEMBER_2 = 100_000_003L
    const val BOT_NAME = "testbot"
    const val GAME = "testestes"
    const val STICKER_FILE_ID = "CAACAgIAAxkBAAEKHjlk6Gex1hZNv_s6-by8ho52CXC9oAACbwEAAhZ8aAOK9nH8d3JcRjAE"
    private const val PHOTOS = 3

    fun seed(mock: BotHandle) {
        mock.setBotName(BOT_NAME)
        mock.addUser(FakeIds.TG_ID, "Test", "test_user")
        mock.addUser(GROUP_MEMBER_1, "Member")
        mock.addUser(GROUP_MEMBER_2, "Member")
        mock.addChat(FakeIds.TG_ID, "private", null)

        mock.addChat(FakeIds.CHAT_ID, "supergroup", "Test group", isForum = true)
        mock.setMemberStatus(FakeIds.CHAT_ID, FakeIds.TG_ID, "creator")
        mock.setMemberStatus(FakeIds.CHAT_ID, mock.id, "administrator")
        mock.setMemberStatus(FakeIds.CHAT_ID, GROUP_MEMBER_1, "member")
        mock.setMemberStatus(FakeIds.CHAT_ID, GROUP_MEMBER_2, "member")

        mock.addChat(FakeIds.CHANNEL_ID, "channel", "Test channel")
        mock.setMemberStatus(FakeIds.CHANNEL_ID, mock.id, "administrator")

        mock.addStickerSet("Test_2_by_${mock.username}", "test_2", stickerCount = 3)
        mock.addFile(STICKER_FILE_ID)
        mock.addGame(GAME, "test", "test2")
        mock.setProfilePhotos(FakeIds.TG_ID, PHOTOS)
    }
}
