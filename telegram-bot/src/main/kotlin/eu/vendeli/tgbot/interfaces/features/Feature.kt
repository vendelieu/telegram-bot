package eu.vendeli.tgbot.interfaces.features

/**
 * The ancestor interface of all Features
 */
interface Features

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
