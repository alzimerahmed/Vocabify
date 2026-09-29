/*
 *     Vocabify/vocabify.search
 *     DictionarySourceRepository.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     This file is part of Vocabify/vocabify.search.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     Vocabify/vocabify.search is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/vocabify.search is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.search.domain.repository.remote

import io.github.yamin8000.owl.search.domain.model.Entry

/**
 * Common abstraction for every dictionary data source.
 *
 * Both current sources ([FreeDictionaryApiRepository] and
 * [WiktionaryApiRepository]) extend this interface, which lets consumers
 * (use cases, future multi-source aggregation in Feature 9) depend on the
 * abstraction instead of a concrete API.
 */
interface DictionarySourceRepository {
    suspend fun searchWord(word: String): List<Entry>
}
