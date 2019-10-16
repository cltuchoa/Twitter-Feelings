package com.uchoa.sentimentanalizer.core

import com.uchoa.sentimentanalizer.enums.Mood

class MoodAnalyzer {

    fun getMood(score: Float): Mood {
        return when {
            score < NEGATIVE_MOOD_THRESHOLD -> return Mood.NEGATIVE
            score > POSITIVE_MOOD_THRESHOLD -> Mood.POSITIVE
            else -> Mood.NEUTRAL
        }
    }

    companion object {
        private const val NEGATIVE_MOOD_THRESHOLD = -0.3
        private const val POSITIVE_MOOD_THRESHOLD = 0.3
    }
}