/*
 *     Vocabify/Vocabify.search.test
 *     GetEtymologyTest.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     GetEtymologyTest.kt Last modified at 2026/6/25
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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.IOException

class GetEtymologyTest {

    private val repository = mockk<WiktionaryEtymologyRepository>()
    private val getEtymology = GetEtymology(repository)

    @Test
    fun `returns the etymology for a known word`() = runTest {
        coEvery { repository.etymology("free") } returns "From Old English freo."

        assertEquals("From Old English freo.", getEtymology("free"))
    }

    @Test
    fun `returns null for blank input without touching the repository`() = runTest {
        assertNull(getEtymology("   "))

        coVerify(exactly = 0) { repository.etymology(any()) }
    }

    @Test
    fun `returns null when the repository fails`() = runTest {
        coEvery { repository.etymology("free") } throws IOException("offline")

        assertNull(getEtymology("free"))
    }
}
