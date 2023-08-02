package eu.vendeli.tgbot.types.forum

import com.fasterxml.jackson.annotation.JsonValue


enum class IconColor(private val color: Int) {
    BLUE(7322096),
    YELLOW(16766590),
    VIOLET(13338331),
    GREEN(9367192),
    ROSE(16749490),
    RED(16478047);

    override fun toString(): String = color.toString()

    @get:JsonValue
    val int: Int get() = color
}
