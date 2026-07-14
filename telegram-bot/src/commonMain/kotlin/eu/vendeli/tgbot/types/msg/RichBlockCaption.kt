package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * Caption of a rich formatted block.
 *
 * [Api reference](https://core.telegram.org/bots/api#richblockcaption)
 * @property text Block caption
 * @property credit Optional. Block credit which corresponds to the HTML tag <cite>
 */
@Serializable
data class RichBlockCaption(
    val text: RichText,
    val credit: RichText? = null,
)
