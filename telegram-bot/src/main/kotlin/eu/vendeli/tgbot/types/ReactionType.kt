package eu.vendeli.tgbot.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

enum class EmojiType(val literal: String) {
    ThumbUp("👍"),
    ThumbDown("👎"),
    Heart("❤"),
    Fire("🔥"),
    Love("🥰"),
    Applause("👏"),
    Smiling("😁"),
    Thinking("🤔"),
    ExplodingHead("🤯"),
    ScreamingInFear("😱"),
    Swearing("🤬"),
    Crying("😢"),
    Firecracker("🎉"),
    Amused("🤩"),
    Vomiting("🤮"),
    Poop("💩"),
    FoldedHand("🙏"),
    OK("👌"),
    DoveOfPeace("🕊"),
    Clown("🤡"),
    Yawning("🥱"),
    Woozy("🥴"),
    HeartEyes("😍"),
    Whale("🐳"),
    BurningHeart("❤‍🔥"),
    MoonFace("🌚"),
    HotDog("🌭"),
    Hundred("💯"),
    Lmao("🤣"),
    Lightning("⚡"),
    Banana("🍌"),
    Cup("🏆"),
    BrokenHeart("💔"),
    RaisedEyebrow("🤨"),
    NeutralFace("😐"),
    Strawberry("🍓"),
    Champagne("🍾"),
    Kiss("💋"),
    FU("🖕"),
    Devil("😈"),
    Zzz("😴"),
    LoudCrying("😭"),
    Nerd("🤓"),
    Ghost("👻"),
    Technician("👨‍💻"),
    Eyes("👀"),
    Pumpkin("🎃"),
    SeeNoEvil("🙈"),
    HaloFace("😇"),
    Fearful("😨"),
    Handshake("🤝"),
    WritingHand("✍"),
    Hugs("🤗"),
    Salute("🫡"),
    Santa("🎅"),
    ChristmasTree("🎄"),
    Snowman("☃"),
    NailPolish("💅"),
    CrazyFace("🤪"),
    Moai("🗿"),
    Cool("🆒"),
    HeartWithArrow("💘"),
    HearNoEvil("🙉"),
    Unicorn("🦄"),
    KissingFace("😘"),
    Pill("💊"),
    SpeakNoEvil("🙊"),
    SunglassesFace("😎"),
    AlienMonster("👾"),
    ManShrugs("🤷‍♂"),
    Shrugs("🤷"),
    WomanShrugs("🤷‍♀"),
    GloomyFace("😡"),
    ;

    override fun toString(): String = literal
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ReactionType.Emoji::class, name = "emoji"),
    JsonSubTypes.Type(value = ReactionType.CustomEmoji::class, name = "custom_emoji"),
)
sealed class ReactionType(
    val type: String,
) {
    data class Emoji(
        val emoji: EmojiType,
    ) : ReactionType("emoji") {
        @Suppress("unused")
        constructor(emoji: String) : this(EmojiType.entries.first { it.literal == emoji })
    }

    data class CustomEmoji(val customEmoji: String) : ReactionType("custom_emoji")
}
