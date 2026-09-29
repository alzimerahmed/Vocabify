/*
 *     Vocabify/freeDictionaryApp.search.main
 *     PruneCache.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     PruneCache.kt Last modified at 2026/6/25
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

package io.github.yamin8000.owl.search.domain.usecase.cache

import io.github.yamin8000.owl.search.domain.repository.local.EntryRepository

/**
 * Keeps only the last [maxEntries] cached word lookups and removes
 * older ones, so the offline cache stays bounded.
 */
class PruneCache(
    private val entryRepository: EntryRepository
) {
    suspend operator fun invoke(maxEntries: Int = DEFAULT_MAX_ENTRIES): Int {
        return entryRepository.prune(maxEntries)
    }

    companion object {
        const val DEFAULT_MAX_ENTRIES = 50
    }
}
