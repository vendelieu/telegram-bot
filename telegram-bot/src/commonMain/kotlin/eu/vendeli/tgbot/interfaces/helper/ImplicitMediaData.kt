package eu.vendeli.tgbot.interfaces.helper

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.component.ImplicitFile

interface ImplicitMediaData {
    var media: ImplicitFile

    @TgAPI.Ignore
    @Deprecated("Not present.", level = DeprecationLevel.ERROR)
    var thumbnail: ImplicitFile?
        get() = null
        set(value) {}
}
