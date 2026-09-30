/*
 *     Vocabify/Vocabify.search.main
 *     GetEtymology.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     GetEtymology.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/Vocabify.search.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/Vocabify.search.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/Vocabify.search.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.search.domain.usecase.search

import io.github.yamin8000.owl.search.domain.repository.remote.WiktionaryEtymologyRepository

class GetEtymology(
    private val repository: WiktionaryEtymologyRepository
) {
    /**
     * Returns the etymology of [word], or null when it is unavailable
     * (unknown word, no etymology section, or network failure).
     */
    suspend operator fun invoke(word: String): String? {
        if (word.isBlank()) return null
        return try {
            repository.etymology(word.trim())
        } catch (ignored: Exception) {
            null
        }
    }
}
