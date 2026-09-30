/*
 *     Vocabify/Vocabify.feature_learning.test
 *     DeckTest.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     DeckTest.kt Last modified at 2026/1/1
 *     This file is part of Vocabify/Vocabify.feature_learning.test.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     Vocabify/Vocabify.feature_learning.test is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/Vocabify.feature_learning.test is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_learning

import io.github.yamin8000.owl.feature_learning.domain.Deck
import io.github.yamin8000.owl.feature_learning.domain.Flashcard
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class DeckTest {

    private val cards = listOf(
        Flashcard("alpha"),
        Flashcard("bravo", phonetic = "/ˈbrɑː.voʊ/"),
        Flashcard("charlie", definitions = persistentListOf("a code word"))
    )

    @Test
    fun `create builds a deck starting at the first card in input order`() {
        val deck = Deck.create(cards)

        assertEquals(3, deck.size)
        assertEquals(0, deck.position)
        assertEquals("alpha", deck.current?.word)
        assertEquals(cards, deck.order.map { deck.cards[it] })
    }

    @Test
    fun `empty deck has no current card and is empty`() {
        val deck = Deck.create(emptyList())

        assertTrue(deck.isEmpty)
        assertNull(deck.current)
        assertEquals(0, deck.size)
    }

    @Test
    fun `next advances and wraps around to the first card`() {
        var deck = Deck.create(cards)
        deck = Deck.next(deck)
        assertEquals("bravo", deck.current?.word)
        deck = Deck.next(deck)
        assertEquals("charlie", deck.current?.word)
        assertTrue(deck.isAtEnd)
        deck = Deck.next(deck)
        assertEquals("alpha", deck.current?.word)
        assertTrue(deck.isAtStart)
    }

    @Test
    fun `previous goes back and wraps around to the last card`() {
        var deck = Deck.create(cards)
        deck = Deck.previous(deck)
        assertEquals("charlie", deck.current?.word)
        assertTrue(deck.isAtEnd)
        deck = Deck.previous(deck)
        assertEquals("bravo", deck.current?.word)
    }

    @Test
    fun `navigation on an empty deck is a no-op`() {
        val deck = Deck.create(emptyList())

        assertEquals(deck, Deck.next(deck))
        assertEquals(deck, Deck.previous(deck))
    }

    @Test
    fun `shuffle keeps all cards, restarts at the first position and changes the order`() {
        val deck = Deck.create(cards)
        val shuffled = Deck.shuffle(deck, Random(42))

        assertEquals(deck.size, shuffled.size)
        assertEquals(0, shuffled.position)
        assertEquals(deck.cards, shuffled.cards)
        assertEquals(deck.order.toSet(), shuffled.order.toSet())
        assertFalse(shuffled.order == deck.order)
    }

    @Test
    fun `shuffle is deterministic for a fixed seed`() {
        val deck = Deck.create(cards)

        assertEquals(Deck.shuffle(deck, Random(7)), Deck.shuffle(deck, Random(7)))
    }

    @Test
    fun `single card deck shuffle is a no-op`() {
        val deck = Deck.create(listOf(Flashcard("solo")))

        assertEquals(deck, Deck.shuffle(deck))
    }

    @Test
    fun `progress flags track the position within the session`() {
        var deck = Deck.create(cards)
        assertTrue(deck.isAtStart)
        assertFalse(deck.isAtEnd)
        deck = Deck.next(deck)
        assertFalse(deck.isAtStart)
        assertFalse(deck.isAtEnd)
        deck = Deck.next(deck)
        assertFalse(deck.isAtStart)
        assertTrue(deck.isAtEnd)
    }
}
