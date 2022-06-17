package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.types.internal.options.Options

/**
 * interface that adds OptionsFeature, MarkupFeature, EntitiesFeature to the action
 *
 * @param Return Action class itself.
 * @param Opts Options class.
 */
interface AllFeaturesPack<Return : AllFeaturesAble, Opts : Options> :
    OptionsFeature<Return, Opts>,
    MarkupFeature<Return>,
    EntitiesFeature<Return>
