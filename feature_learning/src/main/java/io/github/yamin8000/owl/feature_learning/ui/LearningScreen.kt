/*
 *     Vocabify/Vocabify.feature.main
 *     LearningScreen.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     LearningScreen.kt Last modified at 2026/1/1
 *     This file is part of Vocabify/Vocabify.feature.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     Vocabify/Vocabify.feature.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/Vocabify.feature.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_learning.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.SkipNext
import androidx.compose.material.icons.twotone.SkipPrevious
import androidx.compose.material.icons.twotone.Shuffle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yamin8000.owl.common.ui.adaptive.AdaptiveMaxWidthContent
import io.github.yamin8000.owl.common.ui.components.ClickableIcon
import io.github.yamin8000.owl.common.ui.components.EmptyList
import io.github.yamin8000.owl.common.ui.components.ScaffoldWithTitle
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.feature_learning.LearningEvent
import io.github.yamin8000.owl.feature_learning.LearningViewModel
import io.github.yamin8000.owl.feature_learning.domain.Flashcard
import io.github.yamin8000.owl.feature_learning.ui.components.FlashcardView
import io.github.yamin8000.owl.strings.R

@Composable
fun LearningScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    vm: LearningViewModel = hiltViewModel()
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    ScaffoldWithTitle(
        modifier = modifier,
        title = stringResource(R.string.learning),
        onBackClick = onBackClick
    ) {
        when {
            state.isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            state.deck.isEmpty -> Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                EmptyList(modifier = Modifier.fillMaxWidth())
                Text(
                    text = stringResource(R.string.learning_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = Sizes.xLarge)
                )
            }

            else -> DeckContent(
                position = state.deck.position,
                size = state.deck.size,
                onEvent = vm::onEvent,
                currentCard = state.deck.current
            )
        }
    }
}

@Composable
private fun DeckContent(
    position: Int,
    size: Int,
    currentCard: Flashcard?,
    onEvent: (LearningEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Flip state is per-card: reset whenever the deck shows another card.
    var isFlipped by rememberSaveable(position) { mutableStateOf(false) }
    LaunchedEffect(position) { isFlipped = false }

    val nextDescription = stringResource(R.string.next_card)
    val previousDescription = stringResource(R.string.previous_card)
    val shuffleDescription = stringResource(R.string.shuffle_deck)

    AdaptiveMaxWidthContent(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Sizes.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Sizes.Large, Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(R.string.card_progress, position + 1, size),
                style = MaterialTheme.typography.labelLarge
            )
            currentCard?.let { card ->
                FlashcardView(
                    card = card,
                    isFlipped = isFlipped,
                    onFlip = { isFlipped = !isFlipped },
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(Sizes.Large),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClickableIcon(
                    imageVector = Icons.TwoTone.SkipPrevious,
                    contentDescription = previousDescription,
                    onClick = { onEvent(LearningEvent.Previous) }
                )
                ClickableIcon(
                    imageVector = Icons.TwoTone.Shuffle,
                    contentDescription = shuffleDescription,
                    onClick = { onEvent(LearningEvent.Shuffle) }
                )
                ClickableIcon(
                    imageVector = Icons.TwoTone.SkipNext,
                    contentDescription = nextDescription,
                    onClick = { onEvent(LearningEvent.Next) }
                )
            }
        }
    }
}
