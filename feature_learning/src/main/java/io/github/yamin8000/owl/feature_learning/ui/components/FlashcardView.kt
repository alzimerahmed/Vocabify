/*
 *     freeDictionaryApp/freeDictionaryApp.feature_learning.main
 *     FlashcardView.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     FlashcardView.kt Last modified at 2026/1/1
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_learning.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_learning.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_learning.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_learning.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.feature_learning.domain.Flashcard
import io.github.yamin8000.owl.strings.R

private val MinCardHeight = 240.dp
private val MaxCardWidth = 480.dp

/**
 * A single tap-to-reveal flashcard. The flip is a user-triggered Y-rotation:
 * the front face is visible while the rotation is under 90°, the back face
 * (pre-mirrored, counter-rotated) takes over past 90°. Front shows the word,
 * back shows the cached phonetic + definitions (or a lookup hint when there
 * is no cached data).
 */
@Composable
fun FlashcardView(
    card: Flashcard,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        label = "cardFlip"
    )
    val flipDescription = stringResource(R.string.flip_card)
    val tapToReveal = stringResource(R.string.tap_to_reveal)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = MaxCardWidth)
            .clickable(onClick = onFlip)
            .semantics {
                contentDescription = if (isFlipped) flipDescription else "$flipDescription. $tapToReveal"
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Box(
            modifier = Modifier.heightIn(min = MinCardHeight),
            contentAlignment = Alignment.Center
        ) {
            CardFront(
                card = card,
                modifier = Modifier.graphicsLayer {
                    rotationY = rotation
                    // hide the front face once it turns edge-on
                    if (rotation >= 90f) alpha = 0f
                }
            )
            CardBack(
                card = card,
                modifier = Modifier.graphicsLayer {
                    // pre-mirrored face: counter-rotate so the back reads
                    // correctly once the container passes 90°
                    rotationY = rotation - 180f
                    if (rotation < 90f) alpha = 0f
                }
            )
        }
    }
}

@Composable
private fun CardFront(
    card: Flashcard,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Sizes.xLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = card.word,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        card.phonetic?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(top = Sizes.Medium)
            )
        }
        Text(
            text = stringResource(R.string.tap_to_reveal),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(top = Sizes.Large)
        )
    }
}

@Composable
private fun CardBack(
    card: Flashcard,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Sizes.xLarge)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Sizes.Medium, Alignment.CenterVertically)
    ) {
        Text(
            text = card.word,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        card.phonetic?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        if (card.definitions.isEmpty()) {
            Text(
                text = stringResource(R.string.no_cached_definition),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        } else {
            card.definitions.forEach { definition ->
                Text(
                    text = definition,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
