@file:Suppress("MatchingDeclarationName", "ktlint:standard:filename")

package eu.vendeli.tgbot.api.verification

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.options.VerifyOptions
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class VerifyUserAction(
    userId: Long,
    customDescription: String? = null,
) : SimpleAction<Boolean>(),
    OptionsFeature<VerifyUserAction, VerifyOptions>,
    @TgAPI.Name("customDescriptionEntities")
    EntitiesFeature<VerifyUserAction> {
    @TgAPI.Name("verifyUser")
    override val method = "verifyUser"
    override val returnType = getReturnType()
    override val options = VerifyOptions()
    override val entitiesFieldName: String = "custom_description_entities"

    init {
        parameters["user_id"] = userId.toJsonElement()
        customDescription?.let { parameters["custom_description"] = it.toJsonElement() }
    }
}

/**
 * Verifies a user on behalf of the organization which is represented by the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#verifyuser)
 * @param userId Unique identifier of the target user
 * @param customDescription Custom description for the verification; 0-70 characters. Must be empty if the organization isn't allowed to provide a custom verification description.
 * @param customDescriptionParseMode Mode for parsing entities in the custom description. See formatting options for more details. Only "text_link" entities are supported.
 * @param customDescriptionEntities A JSON-serialized list of special entities that appear in the custom description, which can be specified instead of custom_description_parse_mode. Only "text_link" entities are supported.
 * @returns [Boolean]
 */
@TgAPI
inline fun verifyUser(userId: Long, customDescription: String? = null): VerifyUserAction =
    VerifyUserAction(userId, customDescription)

@TgAPI
inline fun verifyUser(userId: Long, customDescription: () -> String? = { null }): VerifyUserAction =
    verifyUser(userId, customDescription())

@TgAPI
inline fun verifyUser(user: User, customDescription: () -> String? = { null }): VerifyUserAction =
    verifyUser(user.id, customDescription())
