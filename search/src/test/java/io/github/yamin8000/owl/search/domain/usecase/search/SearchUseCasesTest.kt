/*
 *     Vocabify/vocabify.search
 *     SearchUseCasesTest.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
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

package io.github.yamin8000.owl.search.domain.usecase.search

import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.search.domain.repository.remote.DictionarySourceRepository
import io.github.yamin8000.owl.search.domain.repository.remote.FreeDictionaryApiRepository
import io.github.yamin8000.owl.search.domain.repository.remote.WiktionaryApiRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchUseCasesTest {

    private val entry = Entry(
        word = "apple",
        meanings = persistentListOf()
    )

    @Test
    fun `SearchFreeDictionary returns entries from repository`() = runTest {
        val repository = mockk<FreeDictionaryApiRepository>()
        coEvery { repository.searchWord("apple") } returns listOf(entry)
        val useCase = SearchFreeDictionary(repository)

        val result = useCase("apple")

        assertEquals(listOf(entry), result)
        coVerify(exactly = 1) { repository.searchWord("apple") }
    }

    @Test
    fun `SearchWiktionary returns entries from repository`() = runTest {
        val repository = mockk<WiktionaryApiRepository>()
        coEvery { repository.searchWord("apple") } returns listOf(entry)
        val useCase = SearchWiktionary(repository)

        val result = useCase("apple")

        assertEquals(listOf(entry), result)
        coVerify(exactly = 1) { repository.searchWord("apple") }
    }

    @Test
    fun `use cases accept any DictionarySourceRepository implementation`() = runTest {
        val entry = Entry(word = "apple", meanings = persistentListOf())
        val freeDictionary = mockk<FreeDictionaryApiRepository>()
        val wiktionary = mockk<WiktionaryApiRepository>()
        coEvery { freeDictionary.searchWord("apple") } returns listOf(entry)
        coEvery { wiktionary.searchWord("apple") } returns listOf(entry)

        val sources = listOf<DictionarySourceRepository>(freeDictionary, wiktionary)
        val results = sources.map { it.searchWord("apple") }

        assertEquals(listOf(listOf(entry), listOf(entry)), results)
    }
}
