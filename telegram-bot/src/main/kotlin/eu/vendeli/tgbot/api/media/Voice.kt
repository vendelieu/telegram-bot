@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VoiceOptions
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SendVoiceAction(private val voice: ImplicitFile<*>) :
    MediaAction<Message>,
    ActionState(),
    OptionsFeature<SendVoiceAction, VoiceOptions>,
    MarkupFeature<SendVoiceAction>,
    EntitiesContextBuilder,
    CaptionFeature<SendVoiceAction> {
    override val TgAction<Message>.method: TgMethod
        get() = TgMethod("sendVoice")
    override val TgAction<Message>.returnType: Class<Message>
        get() = getReturnType()
    override val OptionsFeature<SendVoiceAction, VoiceOptions>.options: VoiceOptions
        get() = VoiceOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"

    override val MediaAction<Message>.defaultType: MediaContentType
        get() = MediaContentType.Voice
    override val MediaAction<Message>.media: ImplicitFile<*>
        get() = voice
    override val MediaAction<Message>.dataField: String
        get() = "voice"
}

fun voice(block: () -> String) = SendVoiceAction(ImplicitFile.FromString(block()))

fun voice(ba: ByteArray) = SendVoiceAction(ImplicitFile.FromByteArray(ba))

fun voice(file: File) = SendVoiceAction(ImplicitFile.FromFile(file))
