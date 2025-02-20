package eu.vendeli.tgbot.types.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

@Serializable
enum class EmojiType(
    val literal: String,
) {
    @SerialName("👍")
    ThumbUp("👍"),

    @SerialName("👎")
    ThumbDown("👎"),

    @SerialName("❤")
    Heart("❤"),

    @SerialName("🔥")
    Fire("🔥"),

    @SerialName("🥰")
    Love("🥰"),

    @SerialName("👏")
    Applause("👏"),

    @SerialName("😁")
    Smiling("😁"),

    @SerialName("🤔")
    Thinking("🤔"),

    @SerialName("🤯")
    ExplodingHead("🤯"),

    @SerialName("😱")
    ScreamingInFear("😱"),

    @SerialName("🤬")
    Swearing("🤬"),

    @SerialName("😢")
    Crying("😢"),

    @SerialName("🎉")
    Firecracker("🎉"),

    @SerialName("🤩")
    Amused("🤩"),

    @SerialName("🤮")
    Vomiting("🤮"),

    @SerialName("💩")
    Poop("💩"),

    @SerialName("🙏")
    FoldedHand("🙏"),

    @SerialName("👌")
    OK("👌"),

    @SerialName("🕊")
    DoveOfPeace("🕊"),

    @SerialName("🤡")
    Clown("🤡"),

    @SerialName("🥱")
    Yawning("🥱"),

    @SerialName("🥴")
    Woozy("🥴"),

    @SerialName("😍")
    HeartEyes("😍"),

    @SerialName("🐳")
    Whale("🐳"),

    @SerialName("❤‍🔥")
    BurningHeart("❤‍🔥"),

    @SerialName("🌚")
    MoonFace("🌚"),

    @SerialName("🌭")
    HotDog("🌭"),

    @SerialName("💯")
    Hundred("💯"),

    @SerialName("🤣")
    Lmao("🤣"),

    @SerialName("⚡")
    Lightning("⚡"),

    @SerialName("🍌")
    Banana("🍌"),

    @SerialName("🏆")
    Cup("🏆"),

    @SerialName("💔")
    BrokenHeart("💔"),

    @SerialName("🤨")
    RaisedEyebrow("🤨"),

    @SerialName("😐")
    NeutralFace("😐"),

    @SerialName("🍓")
    Strawberry("🍓"),

    @SerialName("🍾")
    Champagne("🍾"),

    @SerialName("💋")
    Kiss("💋"),

    @SerialName("🖕")
    FU("🖕"),

    @SerialName("😈")
    Devil("😈"),

    @SerialName("😴")
    Zzz("😴"),

    @SerialName("😭")
    LoudCrying("😭"),

    @SerialName("🤓")
    Nerd("🤓"),

    @SerialName("👻")
    Ghost("👻"),

    @SerialName("👨‍💻")
    Technician("👨‍💻"),

    @SerialName("👀")
    Eyes("👀"),

    @SerialName("🎃")
    Pumpkin("🎃"),

    @SerialName("🙈")
    SeeNoEvil("🙈"),

    @SerialName("😇")
    HaloFace("😇"),

    @SerialName("😨")
    Fearful("😨"),

    @SerialName("🤝")
    Handshake("🤝"),

    @SerialName("✍")
    WritingHand("✍"),

    @SerialName("🤗")
    Hugs("🤗"),

    @SerialName("🫡")
    Salute("🫡"),

    @SerialName("🎅")
    Santa("🎅"),

    @SerialName("🎄")
    ChristmasTree("🎄"),

    @SerialName("☃")
    Snowman("☃"),

    @SerialName("💅")
    NailPolish("💅"),

    @SerialName("🤪")
    CrazyFace("🤪"),

    @SerialName("🗿")
    Moai("🗿"),

    @SerialName("🆒")
    Cool("🆒"),

    @SerialName("💘")
    HeartWithArrow("💘"),

    @SerialName("🙉")
    HearNoEvil("🙉"),

    @SerialName("🦄")
    Unicorn("🦄"),

    @SerialName("😘")
    KissingFace("😘"),

    @SerialName("💊")
    Pill("💊"),

    @SerialName("🙊")
    SpeakNoEvil("🙊"),

    @SerialName("😎")
    SunglassesFace("😎"),

    @SerialName("👾")
    AlienMonster("👾"),

    @SerialName("🤷‍♂")
    ManShrugs("🤷‍♂"),

    @SerialName("🤷")
    Shrugs("🤷"),

    @SerialName("🤷‍♀")
    WomanShrugs("🤷‍♀"),

    @SerialName("😡")
    GloomyFace("😡"),
}

/**
 * This object describes the type of a reaction. Currently, it can be one of
 * - ReactionTypeEmoji
 * - ReactionTypeCustomEmoji
 * - ReactionTypePaid
 *
 * [Api reference](https://core.telegram.org/bots/api#reactiontype)
 *
 */
@Serializable
sealed class ReactionType {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("emoji")
    data class Emoji(
        val emoji: EmojiType,
    ) : ReactionType() {
        @Suppress("unused")
        constructor(emoji: String) : this(EmojiType.entries.first { it.literal == emoji })
    }

    @Serializable
    @SerialName("custom_emoji")
    data class CustomEmoji(
        val customEmojiId: String,
    ) : ReactionType()

    @Serializable
    @SerialName("paid")
    class Paid : ReactionType()
}
