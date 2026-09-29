/*
 *     Vocabify/vocabify.search
 *     FreeDictionaryRetrofitApiRepositoryTest.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
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

package io.github.yamin8000.owl.search.data.repository.remote

import io.github.yamin8000.owl.search.data.datasource.remote.free.FreeDictionaryAPI
import io.github.yamin8000.owl.search.data.datasource.remote.free.dto.DefinitionDto
import io.github.yamin8000.owl.search.data.datasource.remote.free.dto.EntryDto
import io.github.yamin8000.owl.search.data.datasource.remote.free.dto.LicenseDto
import io.github.yamin8000.owl.search.data.datasource.remote.free.dto.MeaningDto
import io.github.yamin8000.owl.search.data.datasource.remote.free.dto.PhoneticDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FreeDictionaryRetrofitApiRepositoryTest {

    private val api = mockk<FreeDictionaryAPI>()
    private val repository = FreeDictionaryRetrofitApiRepository(api)

    @Test
    fun `maps API DTOs to domain entries`() = runTest {
        coEvery { api.search("apple") } returns listOf(
            EntryDto(
                word = "apple",
                phonetics = listOf(PhoneticDto(text = "/ˈæp.əl/", audio = "https://example.com/a.mp3")),
                meanings = listOf(
                    MeaningDto(
                        partOfSpeech = "noun",
                        definitions = listOf(
                            DefinitionDto(
                                definition = "a fruit",
                                example = "I ate an apple.",
                                synonyms = listOf("pome"),
                                antonyms = emptyList()
                            )
                        ),
                        synonyms = listOf("pome"),
                        antonyms = emptyList()
                    )
                ),
                license = LicenseDto(name = "test", url = "https://example.com/license"),
                sourceUrls = listOf("https://example.com")
            )
        )

        val entries = repository.searchWord("apple")

        coVerify(exactly = 1) { api.search("apple") }
        assertEquals(1, entries.size)
        val entry = entries.first()
        assertEquals("apple", entry.word)
        assertEquals("/ˈæp.əl/", entry.phonetics.first().text)
        assertEquals("noun", entry.meanings.first().partOfSpeech)
        assertEquals("a fruit", entry.meanings.first().definitions.first().definition)
        assertEquals(listOf("I ate an apple."), entry.meanings.first().definitions.first().examples.toList())
        assertEquals("test", entry.license?.name)
    }

    @Test
    fun `returns empty list when API has no entry for the word`() = runTest {
        coEvery { api.search("xyzzy") } returns emptyList()

        val entries = repository.searchWord("xyzzy")

        assertTrue(entries.isEmpty())
    }

    @Test
    fun `optional fields stay null when absent in the DTO`() = runTest {
        coEvery { api.search("bare") } returns listOf(
            EntryDto(
                word = "bare",
                phonetics = emptyList(),
                meanings = emptyList()
            )
        )

        val entry = repository.searchWord("bare").single()

        assertNull(entry.license)
        assertNull(entry.sourceUrls)
        assertNull(entry.id)
    }
}
