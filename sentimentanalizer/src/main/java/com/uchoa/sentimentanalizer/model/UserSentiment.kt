package com.uchoa.sentimentanalizer.model

import com.uchoa.sentimentanalizer.enums.Mood

data class UserSentiment(
    val mood: Mood,
    val score: Float,
    val magnitude: Float
)