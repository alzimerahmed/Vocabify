/*
 *     Vocabify/vocabify.feature_settings
 *     SettingsViewModelTest.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     This file is part of Vocabify/vocabify.feature_settings.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     Vocabify/vocabify.feature_settings is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/vocabify.feature_settings is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_settings.ui

import app.cash.turbine.test
import io.github.yamin8000.owl.common.domain.model.DictionarySource
import io.github.yamin8000.owl.common.util.TTS
import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import io.github.yamin8000.owl.datastore.domain.usecase.settings.GetDictionarySource
import io.github.yamin8000.owl.datastore.domain.usecase.settings.GetStartingBlank
import io.github.yamin8000.owl.datastore.domain.usecase.settings.GetTheme
import io.github.yamin8000.owl.datastore.domain.usecase.settings.GetTTS
import io.github.yamin8000.owl.datastore.domain.usecase.settings.GetVibration
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SetDictionarySource
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SetStartingBlank
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SetTheme
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SetTTS
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SetVibration
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getTTS = mockk<GetTTS>()
    private val setTTS = mockk<SetTTS>(relaxed = true)
    private val getTheme = mockk<GetTheme>()
    private val setTheme = mockk<SetTheme>(relaxed = true)
    private val getVibration = mockk<GetVibration>()
    private val setVibration = mockk<SetVibration>(relaxed = true)
    private val getStartingBlank = mockk<GetStartingBlank>()
    private val setStartingBlank = mockk<SetStartingBlank>(relaxed = true)
    private val getSource = mockk<GetDictionarySource>()
    private val setSource = mockk<SetDictionarySource>(relaxed = true)
    private val tts = mockk<TTS>()

    private fun viewModel() = SettingsViewModel(
        useCases = SettingUseCases(
            getTTS = getTTS,
            setTTS = setTTS,
            getTheme = getTheme,
            setTheme = setTheme,
            getVibration = getVibration,
            setVibration = setVibration,
            getStartingBlank = getStartingBlank,
            setStartingBlank = setStartingBlank,
            getSource = getSource,
            setSource = setSource
        ),
        tts = tts
    )

    private fun stubSettings() {
        coEvery { getTTS.invoke() } returns "en-US"
        coEvery { getTheme.invoke() } returns ThemeType.Dark
        coEvery { getVibration.invoke() } returns false
        coEvery { getStartingBlank.invoke() } returns false
        coEvery { getSource.invoke() } returns DictionarySource.Wiktionary
        coEvery { tts.languages() } returns listOf(Locale.US, Locale.UK)
        every { tts.engine } returns null
    }

    @Test
    fun `loads persisted settings and TTS availability on subscribe`() = runTest {
        stubSettings()
        val viewModel = viewModel()

        viewModel.state.test {
            awaitItem() // initial default state
            val loaded = awaitItem()
            assertEquals(ThemeType.Dark, loaded.theme)
            assertEquals("en-US", loaded.ttsLang)
            assertFalse(loaded.isVibrating)
            assertFalse(loaded.isStartingBlank)
            assertEquals(DictionarySource.Wiktionary, loaded.source)
            assertEquals(listOf(Locale.US, Locale.UK), loaded.languages.toList())
            assertFalse(loaded.isTtsAvailable)
        }
    }

    @Test
    fun `OnVibrationChange updates state and persists the value`() = runTest {
        stubSettings()
        val viewModel = viewModel()

        viewModel.state.test {
            awaitItem() // initial default state
            awaitItem() // loaded settings
            viewModel.onAction(SettingsAction.OnVibrationChange(true))
            assertTrue(awaitItem().isVibrating)
        }
        advanceUntilIdle()
        coVerify(exactly = 1) { setVibration.invoke(true) }
    }

    @Test
    fun `OnThemeChange updates state and persists the theme`() = runTest {
        stubSettings()
        val viewModel = viewModel()

        viewModel.state.test {
            awaitItem() // initial default state
            awaitItem() // loaded settings
            viewModel.onAction(SettingsAction.OnThemeChange(ThemeType.Light))
            assertEquals(ThemeType.Light, awaitItem().theme)
        }
        advanceUntilIdle()
        coVerify(exactly = 1) { setTheme.invoke(ThemeType.Light) }
    }

    @Test
    fun `OnSourceChanged updates state and persists the source`() = runTest {
        stubSettings()
        val viewModel = viewModel()

        viewModel.state.test {
            awaitItem() // initial default state
            awaitItem() // loaded settings
            viewModel.onAction(SettingsAction.OnSourceChanged(DictionarySource.FreeDictionary))
            assertEquals(DictionarySource.FreeDictionary, awaitItem().source)
        }
        advanceUntilIdle()
        coVerify(exactly = 1) { setSource.invoke(DictionarySource.FreeDictionary) }
    }

    @Test
    fun `OnTabChanged updates the selected tab without persisting anything`() = runTest {
        stubSettings()
        val viewModel = viewModel()

        viewModel.state.test {
            awaitItem() // initial default state
            awaitItem() // loaded settings
            viewModel.onAction(SettingsAction.OnTabChanged(SettingsTab.Advanced))
            assertEquals(SettingsTab.Advanced, awaitItem().currentTab)
        }
        coVerify(exactly = 0) { setTheme.invoke(any()) }
        coVerify(exactly = 0) { setSource.invoke(any()) }
    }
}
