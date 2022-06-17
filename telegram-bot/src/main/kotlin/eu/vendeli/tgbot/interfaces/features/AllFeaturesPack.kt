package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.types.internal.options.Options

interface AllFeaturesPack<Return : AllFeaturesAble, Opts : Options> :
    OptionsFeature<Return, Opts>,
    MarkupFeature<Return>,
    EntitiesFeature<Return>
