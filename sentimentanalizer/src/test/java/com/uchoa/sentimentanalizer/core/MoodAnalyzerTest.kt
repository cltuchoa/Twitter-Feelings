package com.uchoa.sentimentanalizer.core

import com.uchoa.sentimentanalizer.enums.Mood
import org.junit.Test

import org.junit.Assert.*

class MoodAnalyzerTest {

    private val moodAnalyzer = MoodAnalyzer()

    @Test
    fun `Test mood evaluation based on score`() {
        val neutralValue = 0.0f
        val positiveValue = 0.5f
        val negativeValue = -0.5f

        assertEquals(moodAnalyzer.getMood(neutralValue), Mood.NEUTRAL)
        assertEquals(moodAnalyzer.getMood(positiveValue), Mood.POSITIVE)
        assertEquals(moodAnalyzer.getMood(negativeValue), Mood.NEGATIVE)
    }
}