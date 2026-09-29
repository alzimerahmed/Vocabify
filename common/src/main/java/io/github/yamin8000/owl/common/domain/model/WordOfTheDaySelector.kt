/*
 *     Vocabify/freeDictionaryApp.common.main
 *     WordOfTheDaySelector.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     WordOfTheDaySelector.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/freeDictionaryApp.common.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.domain.model

/**
 * Deterministic Word-of-the-Day selection (Feature 1).
 *
 * The word for a given day is derived purely from the date and the word
 * list, so the app, the home card and the home-screen widget always agree
 * on "today's word" without any network call or persisted state.
 *
 * Pure Kotlin — unit-testable, no Android dependencies.
 */
object WordOfTheDaySelector {

    /**
     * Deterministically picks the word of the day.
     *
     * @param words candidate words; must be non-empty and stable in order
     * across processes/days (a bundled constant list qualifies).
     * @param epochDay days since the epoch (e.g. from Calendar or
     * LocalDate.toEpochDay()); the same value must always yield the same
     * word.
     * @return today's word, or the [fallback] when [words] is empty.
     */
    fun select(
        words: List<String>,
        epochDay: Long,
        fallback: String = "free"
    ): String {
        if (words.isEmpty()) return fallback
        // A cheap deterministic mix of the day number so consecutive days
        // do not walk the list linearly (avoids alphabetical predictability).
        var seed = epochDay
        seed = seed xor (seed ushr 33)
        seed *= -0x61c88647_7c4d_9e05L // golden-ratio constant (0x9E3779B97F4A7C15)
        seed = seed xor (seed ushr 31)
        val index = (seed % words.size).toInt().let { if (it < 0) it + words.size else it }
        return words[index]
    }
}
