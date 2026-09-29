/*
 *     Vocabify/vocabify.feature_favourites
 *     FavouritesViewModelTest.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     This file is part of Vocabify/vocabify.feature_favourites.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     Vocabify/vocabify.feature_favourites is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/vocabify.feature_favourites is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_favourites

import app.cash.turbine.test
import io.github.yamin8000.owl.datastore.domain.usecase.favourites.AddFavourite
import io.github.yamin8000.owl.datastore.domain.usecase.favourites.FavouriteUseCases
import io.github.yamin8000.owl.datastore.domain.usecase.favourites.GetAllFavourite
import io.github.yamin8000.owl.datastore.domain.usecase.favourites.RemoveAllFavourite
import io.github.yamin8000.owl.datastore.domain.usecase.favourites.RemoveFavourite
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FavouritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val addFavourite = mockk<AddFavourite>(relaxed = true)
    private val removeFavourite = mockk<RemoveFavourite>(relaxed = true)
    private val removeAllFavourite = mockk<RemoveAllFavourite>(relaxed = true)
    private val getAllFavourite = mockk<GetAllFavourite>()

    private val favouritesFlow = MutableStateFlow(listOf("alpha"))

    private fun viewModel() = FavouritesViewModel(
        useCases = FavouriteUseCases(
            addFavourite = addFavourite,
            removeFavourite = removeFavourite,
            removeAllFavourite = removeAllFavourite,
            getAllFavourite = getAllFavourite
        )
    )

    @Test
    fun `exposes favourites emitted by the repository flow`() = runTest {
        coEvery { getAllFavourite.invoke() } returns favouritesFlow

        val viewModel = viewModel()

        viewModel.state.test {
            assertEquals(emptyList<String>(), awaitItem().favourites)
            assertEquals(listOf("alpha"), awaitItem().favourites)
        }
    }

    @Test
    fun `RemoveAll event removes all favourites`() = runTest {
        coEvery { getAllFavourite.invoke() } returns favouritesFlow
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onEvent(FavouriteEvent.RemoveAll)
        advanceUntilIdle()

        coVerify(exactly = 1) { removeAllFavourite.invoke() }
    }

    @Test
    fun `Remove event removes the given favourite`() = runTest {
        coEvery { getAllFavourite.invoke() } returns favouritesFlow
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onEvent(FavouriteEvent.Remove("alpha"))
        advanceUntilIdle()

        coVerify(exactly = 1) { removeFavourite.invoke("alpha") }
    }
}
