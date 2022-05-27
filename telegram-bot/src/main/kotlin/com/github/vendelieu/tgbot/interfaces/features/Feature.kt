package com.github.vendelieu.tgbot.interfaces.features

import com.github.vendelieu.tgbot.interfaces.TgAction

interface Feature {
    val parameters: MutableMap<String, Any?>
}

interface Features : TgAction

interface OptionAble : Features
interface MarkupAble : Features
interface EntityAble : Features
interface CaptionAble : Features

interface AllFeaturesAble : OptionAble, MarkupAble, EntityAble
