/*
 *     Vocabify/freeDictionaryApp.search.main
 *     WiktionaryEtymologyRepository.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     WiktionaryEtymologyRepository.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/freeDictionaryApp.search.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/freeDictionaryApp.search.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/freeDictionaryApp.search.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.search.domain.repository.remote

interface WiktionaryEtymologyRepository {
    /**
     * Returns the etymology of [word] from Wiktionary as plain text,
     * or null when the word has no etymology section.
     */
    suspend fun etymology(word: String): String?
}
