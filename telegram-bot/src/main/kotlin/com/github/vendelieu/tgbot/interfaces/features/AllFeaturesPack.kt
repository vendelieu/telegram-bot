package com.github.vendelieu.tgbot.interfaces.features

import com.github.vendelieu.tgbot.types.internal.options.Options

interface AllFeaturesPack<Return : AllFeaturesAble, Opts : Options> :
    OptionsFeature<Return, Opts>,
    MarkupFeature<Return>,
    EntitiesFeature<Return>
