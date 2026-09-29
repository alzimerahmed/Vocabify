/*
 *     freeDictionaryApp/freeDictionaryApp.feature_learning.main
 *     DeckState.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     DeckState.kt Last modified at 2026/1/1
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

package io.github.yamin8000.owl.feature_learning.domain

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random

/**
 * Immutable state of a flashcard deck. Cards are addressed through an
 * [order] permutation so shuffling never mutates the underlying card list.
 */
data class DeckState(
    val cards: ImmutableList<Flashcard>,
    val order: List<Int>,
    val position: Int
) {
    val size: Int get() = order.size
    val isEmpty: Boolean get() = order.isEmpty()
    val isAtStart: Boolean get() = position == 0
    val isAtEnd: Boolean get() = position == size - 1

    /** The card currently on top of the deck, or null for an empty deck. */
    val current: Flashcard?
        get() = order.getOrNull(position)?.let { cards[it] }
}

/** Pure deck transitions — no Android dependencies, fully unit-testable. */
object Deck {

    fun create(cards: List<Flashcard>): DeckState {
        return DeckState(
            cards = cards.toImmutableList(),
            order = cards.indices.toList(),
            position = 0
        )
    }

    /** Advances to the next card, wrapping around to the first card. */
    fun next(state: DeckState): DeckState {
        if (state.isEmpty) return state
        return state.copy(position = (state.position + 1) % state.size)
    }

    /** Goes back to the previous card, wrapping around to the last card. */
    fun previous(state: DeckState): DeckState {
        if (state.isEmpty) return state
        return state.copy(position = (state.position - 1 + state.size) % state.size)
    }

    /**
     * Shuffles the visiting order and restarts from the first card.
     * Never produces the identical order when the deck has more than one
     * card, so "shuffle" always visibly changes the sequence.
     */
    fun shuffle(state: DeckState, random: Random = Random.Default): DeckState {
        if (state.size < 2) return state
        var newOrder: List<Int>
        do {
            newOrder = state.order.shuffled(random)
        } while (newOrder == state.order)
        return state.copy(order = newOrder, position = 0)
    }
}
