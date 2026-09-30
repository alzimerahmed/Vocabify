/*
 *     Vocabify/Vocabify.datastore.test
 *     BackupUseCasesTest.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     BackupUseCasesTest.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/Vocabify.datastore.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/Vocabify.datastore.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/Vocabify.datastore.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.datastore.domain.usecase.backup

import com.squareup.moshi.Moshi
import io.github.yamin8000.owl.datastore.domain.model.UserBackup
import io.github.yamin8000.owl.datastore.domain.repository.FavouriteRepository
import io.github.yamin8000.owl.datastore.domain.repository.HistoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BackupUseCasesTest {

    private val historyRepository = mockk<HistoryRepository>(relaxed = true)
    private val favouriteRepository = mockk<FavouriteRepository>(relaxed = true)
    private val moshi = Moshi.Builder().build()

    @Test
    fun `export produces a valid backup document`() = runTest {
        coEvery { historyRepository.all() } returns flowOf(listOf("free", "set"))
        coEvery { favouriteRepository.all() } returns flowOf(listOf("apple"))

        val json = ExportUserData(historyRepository, favouriteRepository, moshi)()

        val backup = moshi.adapter(UserBackup::class.java).fromJson(json)
        assertEquals(UserBackup(history = listOf("free", "set"), favourites = listOf("apple")), backup)
    }

    @Test
    fun `import merges entries into the existing stores`() = runTest {
        val json = moshi.adapter(UserBackup::class.java).toJson(
            UserBackup(history = listOf("free", ""), favourites = listOf("apple"))
        )

        val imported = ImportUserData(historyRepository, favouriteRepository, moshi)(json)

        assertEquals(2, imported)
        coVerify(exactly = 1) { historyRepository.add("free") }
        coVerify(exactly = 1) { favouriteRepository.add("apple") }
    }

    @Test
    fun `import rejects invalid json and unknown versions`() = runTest {
        val import = ImportUserData(historyRepository, favouriteRepository, moshi)

        assertEquals(null, import("not json"))
        assertEquals(
            null,
            import(moshi.adapter(UserBackup::class.java).toJson(UserBackup(version = 99)))
        )
    }
}
