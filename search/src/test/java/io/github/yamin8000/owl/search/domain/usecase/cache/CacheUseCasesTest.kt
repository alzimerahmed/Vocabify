/*
 *     Vocabify/vocabify.search
 *     CacheUseCasesTest.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
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

package io.github.yamin8000.owl.search.domain.usecase.cache

import io.github.yamin8000.owl.search.domain.model.Definition
import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.search.domain.model.Phonetic
import io.github.yamin8000.owl.search.domain.repository.local.DefinitionRepository
import io.github.yamin8000.owl.search.domain.repository.local.EntryRepository
import io.github.yamin8000.owl.search.domain.repository.local.MeaningRepository
import io.github.yamin8000.owl.search.domain.repository.local.PhoneticRepository
import io.github.yamin8000.owl.search.domain.repository.local.TermRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CacheUseCasesTest {

    private val entryRepository = mockk<EntryRepository>()
    private val meaningRepository = mockk<MeaningRepository>()
    private val definitionRepository = mockk<DefinitionRepository>()
    private val phoneticRepository = mockk<PhoneticRepository>()
    private val termRepository = mockk<TermRepository>()

    private val entry = Entry(
        word = "Apple",
        phonetics = persistentListOf(Phonetic(text = "/ˈæp.əl/")),
        meanings = persistentListOf(
            Meaning(
                partOfSpeech = "noun",
                definitions = persistentListOf(Definition(definition = "a fruit")),
                synonyms = persistentListOf(),
                antonyms = persistentListOf()
            )
        )
    )

    @Test
    fun `CacheEntry skips words that are already cached`() = runTest {
        coEvery { entryRepository.findByTerm("apple") } returns listOf(entry)

        CacheEntry(
            entryRepository = entryRepository,
            meaningRepository = meaningRepository,
            definitionRepository = definitionRepository,
            phoneticRepository = phoneticRepository
        )(entry)

        coVerify(exactly = 0) { entryRepository.add(any()) }
        coVerify(exactly = 0) { meaningRepository.add(any()) }
    }

    @Test
    fun `CacheEntry stores entry, phonetics, meanings and definitions for new words`() = runTest {
        coEvery { entryRepository.findByTerm("apple") } returns emptyList()
        coEvery { entryRepository.add(any()) } returns 7L
        coEvery { phoneticRepository.add(any()) } returns 1L
        coEvery { meaningRepository.add(any()) } returns 2L
        coEvery { definitionRepository.add(any()) } returns 3L

        CacheEntry(
            entryRepository = entryRepository,
            meaningRepository = meaningRepository,
            definitionRepository = definitionRepository,
            phoneticRepository = phoneticRepository
        )(entry)

        val phoneticSlot = slot<Phonetic>()
        coVerify(exactly = 1) { phoneticRepository.add(capture(phoneticSlot)) }
        assertEquals(7L, phoneticSlot.captured.entryId)

        val definitionSlot = slot<Definition>()
        coVerify(exactly = 1) { definitionRepository.add(capture(definitionSlot)) }
        assertEquals(2L, definitionSlot.captured.meaningId)
    }

    @Test
    fun `GetCachedEntries delegates to the entry repository`() = runTest {
        coEvery { entryRepository.findByTerm("apple") } returns listOf(entry)

        val result = GetCachedEntries(entryRepository)("apple")

        assertEquals(listOf(entry), result)
        coVerify(exactly = 1) { entryRepository.findByTerm("apple") }
    }

    @Test
    fun `CacheWordData adds the word and content words, skipping already known terms`() = runTest {
        coEvery { termRepository.all() } returns listOf("fruit")
        coEvery { termRepository.add(any()) } returns 1L

        CacheWordData(termRepository)(entry)

        // "apple" (word), "noun" (part of speech), "a", "fruit" (definition words) —
        // "fruit" is already known and must not be re-added.
        coVerify(exactly = 1) { termRepository.add("apple") }
        coVerify(exactly = 1) { termRepository.add("noun") }
        coVerify(exactly = 0) { termRepository.add("fruit") }
    }
}
