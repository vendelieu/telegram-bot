package eu.vendeli.tgbot.interfaces.helper

import eu.vendeli.tgbot.types.internal.ImplicitFile

interface ImplicitMediaData {
    var media: ImplicitFile

    @Deprecated("Not present.", level = DeprecationLevel.ERROR)
    var thumbnail: ImplicitFile?
        get() = null
        set(value) {}
}
