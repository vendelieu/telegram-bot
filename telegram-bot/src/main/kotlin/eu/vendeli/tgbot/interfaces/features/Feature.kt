package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.TgAction

/**
 * Parent Interface of Features and Actions
 */
interface Feature {
    /**
     * Parameter for storing API data.
     */
    val parameters: MutableMap<String, Any?>
}

/**
 * The ancestor interface of all Features
 */
interface Features : TgAction

/**
 * Mark the Action as able to have [OptionsFeature].
 */
interface OptionAble : Features

/**
 * Mark the Action as able to have [MarkupFeature].
 */
interface MarkupAble : Features

/**
 * Mark the Action as able to have [EntitiesFeature].
 */
interface EntityAble : Features

/**
 * Mark the Action as able to have [CaptionFeature].
 */
interface CaptionAble : Features

/**
 * Mark the Action as able to have [OptionsFeature], [MarkupFeature], [EntitiesFeature].
 */
interface AllFeaturesAble : OptionAble, MarkupAble, EntityAble
