/*
 *     Vocabify/vocabify.search
 *     SearchListUiTest.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
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

package io.github.yamin8000.owl.search.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import io.github.yamin8000.owl.search.domain.model.Definition
import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.search.ui.components.SearchList
import io.github.yamin8000.owl.strings.R
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SearchListUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val entry = Entry(
        word = "apple",
        meanings = persistentListOf(
            Meaning(
                partOfSpeech = "noun",
                definitions = persistentListOf(Definition(definition = "a round fruit")),
                synonyms = persistentListOf(),
                antonyms = persistentListOf()
            )
        )
    )

    private fun setContent(isOnline: Boolean, onAddToFavourite: () -> Unit = {}) {
        composeRule.setContent {
            MaterialTheme {
                SearchList(
                    isOnline = isOnline,
                    word = "apple",
                    onAddToFavourite = onAddToFavourite,
                    onShareWord = {},
                    onExpandText = {},
                    onTextToSpeech = {},
                    onPlayAudio = {},
                    entries = listOf(entry).toImmutableList()
                )
            }
        }
    }

    private fun offlineErrorText(): String =
        ApplicationProvider.getApplicationContext<android.content.Context>()
            .getString(R.string.general_net_error)

    @Test
    fun `search results show the word card and entry definitions`() {
        setContent(isOnline = true)

        composeRule.onNodeWithText("apple").assertIsDisplayed()
        composeRule.onNodeWithText("a round fruit").assertIsDisplayed()
    }

    @Test
    fun `offline banner is shown when offline and hidden when online`() {
        setContent(isOnline = false)
        composeRule.onNodeWithText(offlineErrorText()).assertIsDisplayed()

        setContent(isOnline = true)
        composeRule.onNodeWithText(offlineErrorText()).assertDoesNotExist()
    }

    @Test
    fun `tapping favourite on the word card invokes the callback`() {
        var favouriteClicked = false
        setContent(isOnline = true, onAddToFavourite = { favouriteClicked = true })

        val favouritesLabel = ApplicationProvider.getApplicationContext<android.content.Context>()
            .getString(R.string.favourites)
        composeRule.onNodeWithContentDescription(favouritesLabel).performClick()

        assertEquals(true, favouriteClicked)
    }
}
