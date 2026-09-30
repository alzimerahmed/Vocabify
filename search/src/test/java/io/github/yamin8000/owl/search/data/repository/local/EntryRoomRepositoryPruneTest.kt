/*
 *     Vocabify/Vocabify.search.test
 *     EntryRoomRepositoryPruneTest.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     EntryRoomRepositoryPruneTest.kt Last modified at 2026/6/25
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

package io.github.yamin8000.owl.search.data.repository.local

import io.github.yamin8000.owl.search.data.datasource.local.dao.DAOs
import io.github.yamin8000.owl.search.data.datasource.local.entity.EntryEntity
import io.github.yamin8000.owl.search.data.repository.local.EntryRoomRepository
import io.github.yamin8000.owl.search.domain.repository.local.MeaningRepository
import io.github.yamin8000.owl.search.domain.repository.local.PhoneticRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class EntryRoomRepositoryPruneTest {

    private val oldest = EntryEntity(word = "old", createdAt = 1, id = 1)
    private val middle = EntryEntity(word = "middle", createdAt = 2, id = 2)
    private val newest = EntryEntity(word = "new", createdAt = 3, id = 3)

    @Test
    fun `prune keeps the newest entries and removes the oldest`() = runTest {
        val dao = mockk<DAOs.EntryDao>()
        coEvery { dao.all() } returns listOf(oldest, middle, newest)
        coEvery { dao.deleteAll(any<List<EntryEntity>>()) } returns 1

        val repository = EntryRoomRepository(
            dao = dao,
            phoneticRepository = mockk(),
            meaningRepository = mockk()
        )

        val removed = repository.prune(maxEntries = 2)

        assertEquals(1, removed)
        coVerify {
            dao.deleteAll(withArg { deleted -> assertEquals(listOf(oldest), deleted) })
        }
    }

    @Test
    fun `prune does nothing when the cache is within the limit`() = runTest {
        val dao = mockk<DAOs.EntryDao>()
        coEvery { dao.all() } returns listOf(EntryEntity(word = "w", createdAt = 1, id = 1))

        val repository = EntryRoomRepository(
            dao = dao,
            phoneticRepository = mockk(),
            meaningRepository = mockk()
        )

        assertEquals(0, repository.prune(maxEntries = 5))
        coVerify(exactly = 0) { dao.deleteAll(any<List<EntryEntity>>()) }
    }
}
