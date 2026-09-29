/*
 *     Vocabify/vocabify.feature_history
 *     HistoryViewModelTest.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     This file is part of Vocabify/vocabify.feature_history.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     Vocabify/vocabify.feature_history is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/vocabify.feature_history is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.karlancer.feature_history.ui

import app.cash.turbine.test
import io.github.yamin8000.owl.datastore.domain.usecase.history.AddHistory
import io.github.yamin8000.owl.datastore.domain.usecase.history.GetAllHistory
import io.github.yamin8000.owl.datastore.domain.usecase.history.HistoryUseCases
import io.github.yamin8000.owl.datastore.domain.usecase.history.RemoveAllHistory
import io.github.yamin8000.owl.datastore.domain.usecase.history.RemoveHistory
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
class HistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val addHistory = mockk<AddHistory>(relaxed = true)
    private val removeHistory = mockk<RemoveHistory>(relaxed = true)
    private val removeAllHistory = mockk<RemoveAllHistory>(relaxed = true)
    private val getAllHistory = mockk<GetAllHistory>()

    private val historyFlow = MutableStateFlow(listOf("alpha", "beta"))

    private fun viewModel() = HistoryViewModel(
        useCases = HistoryUseCases(
            addHistory = addHistory,
            removeHistory = removeHistory,
            removeAllHistory = removeAllHistory,
            getAllHistory = getAllHistory
        )
    )

    @Test
    fun `exposes history emitted by the repository flow`() = runTest {
        coEvery { getAllHistory.invoke() } returns historyFlow

        val viewModel = viewModel()

        viewModel.state.test {
            assertEquals(emptyList<String>(), awaitItem().history)
            assertEquals(listOf("alpha", "beta"), awaitItem().history)
        }
    }

    @Test
    fun `state follows later emissions of the history flow`() = runTest {
        coEvery { getAllHistory.invoke() } returns historyFlow
        val viewModel = viewModel()
        advanceUntilIdle()

        historyFlow.value = listOf("alpha")
        advanceUntilIdle()

        assertEquals(listOf("alpha"), viewModel.state.value.history)
    }

    @Test
    fun `RemoveAll event removes all history`() = runTest {
        coEvery { getAllHistory.invoke() } returns historyFlow
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onEvent(HistoryEvent.RemoveAll)
        advanceUntilIdle()

        coVerify(exactly = 1) { removeAllHistory.invoke() }
    }

    @Test
    fun `RemoveHistory event removes the given entry`() = runTest {
        coEvery { getAllHistory.invoke() } returns historyFlow
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onEvent(HistoryEvent.RemoveHistory("alpha"))
        advanceUntilIdle()

        coVerify(exactly = 1) { removeHistory.invoke("alpha") }
    }
}
