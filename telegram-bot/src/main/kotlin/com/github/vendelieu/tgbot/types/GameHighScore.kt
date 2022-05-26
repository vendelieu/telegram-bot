package com.github.vendelieu.tgbot.types

data class GameHighScore(
    val position: Int,
    val user: User,
    val score: Int
)
