package eu.vendeli.tgbot.types.business

import eu.vendeli.tgbot.types.media.Sticker
import kotlinx.serialization.Serializable

/**

 *
 * Api reference: https://core.telegram.org/bots/api#businessintro
 * @property title Optional. Title text of the business intro
 * @property message Optional. Message text of the business intro
 * @property sticker Optional. Sticker of the business intro
 */
@Serializable
data class BusinessIntro(
    val title: String? = null,
    val message: String? = null,
    val sticker: Sticker? = null,
)
