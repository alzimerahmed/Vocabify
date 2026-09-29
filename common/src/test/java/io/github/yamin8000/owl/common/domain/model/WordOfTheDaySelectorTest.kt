/*
 *     Vocabify/freeDictionaryApp.common.test
 *     WordOfTheDaySelectorTest.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     WordOfTheDaySelectorTest.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/freeDictionaryApp.common.test.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/freeDictionaryApp.common.test is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/freeDictionaryApp.common.test is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WordOfTheDaySelectorTest {

    private val words = listOf("alpha", "bravo", "charlie", "delta", "echo")

    @Test
    fun `same day always yields the same word`() {
        val first = WordOfTheDaySelector.select(words, epochDay = 20_000)
        repeat(10) {
            assertEquals(first, WordOfTheDaySelector.select(words, epochDay = 20_000))
        }
    }

    @Test
    fun `word is always a member of the candidate list`() {
        repeat(1_000) { day ->
            val word = WordOfTheDaySelector.select(words, epochDay = day.toLong())
            assertTrue(word in words)
        }
    }

    @Test
    fun `different days can yield different words`() {
        val picked = (0 until 100).map { WordOfTheDaySelector.select(words, it.toLong()) }.toSet()
        assertTrue("expected variety across days, got $picked", picked.size > 1)
    }

    @Test
    fun `selection covers the whole list over time`() {
        val picked = (0L until 500L).map { WordOfTheDaySelector.select(words, it) }.toSet()
        assertEquals(words.toSet(), picked)
    }

    @Test
    fun `empty list falls back`() {
        assertEquals("free", WordOfTheDaySelector.select(emptyList(), epochDay = 1))
    }

    @Test
    fun `bundled word list is non-empty and distinct`() {
        val bundled = WordOfTheDayWords.words
        assertTrue(bundled.isNotEmpty())
        assertEquals(bundled.size, bundled.toSet().size)
        assertTrue(bundled.all { it.isNotBlank() && it == it.lowercase() })
    }
}
