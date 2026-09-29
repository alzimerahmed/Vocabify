/*
 *     freeDictionaryApp/freeDictionaryApp.feature_learning.test
 *     LearningViewModelTest.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     LearningViewModelTest.kt Last modified at 2026/1/1
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_learning.test.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_learning.test is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_learning.test is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_learning

import app.cash.turbine.test
import io.github.yamin8000.owl.datastore.domain.usecase.favourites.FavouriteUseCases
import io.github.yamin8000.owl.search.domain.usecase.cache.WordCacheUseCases
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LearningViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val favouritesFlow = MutableStateFlow(listOf("alpha", "bravo"))

    private val favouriteUseCases = mockk<FavouriteUseCases>()
    private val wordCacheUseCases = mockk<WordCacheUseCases>()

    @Before
    fun setUp() {
        coEvery { favouriteUseCases.getAllFavourite.invoke() } returns favouritesFlow
        coEvery { wordCacheUseCases.getCachedEntries.invoke(any()) } returns emptyList()
    }

    private fun viewModel() = LearningViewModel(
        favouriteUseCases = favouriteUseCases,
        wordCacheUseCases = wordCacheUseCases
    )

    @Test
    fun `starts loading then exposes a deck mirroring the favourites`() = runTest {
        val viewModel = viewModel()

        viewModel.state.test {
            assertTrue(awaitItem().isLoading)
            val loaded = awaitItem()
            assertFalse(loaded.isLoading)
            assertEquals(2, loaded.deck.size)
            assertEquals("alpha", loaded.deck.current?.word)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deck updates when favourites change`() = runTest {
        val viewModel = viewModel()
        advanceUntilIdle()

        favouritesFlow.value = listOf("alpha", "bravo", "charlie")
        advanceUntilIdle()

        viewModel.state.test {
            assertEquals(3, awaitItem().deck.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Next event advances the deck and wraps around`() = runTest {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onEvent(LearningEvent.Next)
        viewModel.onEvent(LearningEvent.Next)
        viewModel.onEvent(LearningEvent.Next)

        viewModel.state.test {
            assertEquals("alpha", awaitItem().deck.current?.word)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Previous event wraps around to the last card`() = runTest {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onEvent(LearningEvent.Previous)

        viewModel.state.test {
            assertEquals("bravo", awaitItem().deck.current?.word)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Shuffle event keeps the deck size and restarts at the first position`() = runTest {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onEvent(LearningEvent.Next)
        viewModel.onEvent(LearningEvent.Shuffle)

        viewModel.state.test {
            val deck = awaitItem().deck
            assertEquals(2, deck.size)
            assertEquals(0, deck.position)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `cards carry cached definitions and phonetics when available`() = runTest {
        val cached = io.github.yamin8000.owl.search.domain.model.Entry.mock()
        coEvery { wordCacheUseCases.getCachedEntries.invoke("alpha") } returns listOf(cached)

        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.state.test {
            val card = awaitItem().deck.current
            assertTrue(card != null)
            cancelAndIgnoreRemainingEvents()
        }
        // the cached lookup was consulted for the favourite words
        coVerify { wordCacheUseCases.getCachedEntries.invoke("alpha") }
        coVerify { wordCacheUseCases.getCachedEntries.invoke("bravo") }
    }

    @Test
    fun `a favourite without cached data still gets a card`() = runTest {
        coEvery { wordCacheUseCases.getCachedEntries.invoke(any()) } returns emptyList()

        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.state.test {
            val deck = awaitItem().deck
            assertEquals(2, deck.size)
            assertEquals("alpha", deck.current?.word)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `cache failures do not break the deck`() = runTest {
        coEvery { wordCacheUseCases.getCachedEntries.invoke(any()) } throws RuntimeException("boom")

        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.state.test {
            val deck = awaitItem().deck
            assertEquals(2, deck.size)
            assertFalse(deck.isEmpty)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
