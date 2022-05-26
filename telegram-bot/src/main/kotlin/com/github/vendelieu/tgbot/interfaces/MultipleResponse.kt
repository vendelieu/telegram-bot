package com.github.vendelieu.tgbot.interfaces

interface MultipleResponse

interface MultiResponseOf<Type : MultipleResponse>

@Suppress("unused")
internal inline fun <reified Type : MultipleResponse> MultiResponseOf<Type>.getInnerType(): Class<Type> =
    Type::class.java
