/*
 *     Vocabify/Vocabify.feature.main
 *     LearningViewModel.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     LearningViewModel.kt Last modified at 2026/1/1
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

package io.github.yamin8000.owl.feature_learning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yamin8000.owl.datastore.domain.usecase.favourites.FavouriteUseCases
import io.github.yamin8000.owl.feature_learning.domain.Deck
import io.github.yamin8000.owl.feature_learning.domain.DeckState
import io.github.yamin8000.owl.feature_learning.domain.Flashcard
import io.github.yamin8000.owl.search.domain.usecase.cache.WordCacheUseCases
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val favouriteUseCases: FavouriteUseCases,
    private val wordCacheUseCases: WordCacheUseCases
) : ViewModel() {
    private val scope = viewModelScope

    private var _state = MutableStateFlow(LearningState())
    val state = _state.asStateFlow()

    init {
        scope.launch {
            favouriteUseCases.getAllFavourite().collect { favourites ->
                val cards = buildDeck(favourites)
                _state.update {
                    it.copy(deck = cards, isLoading = false)
                }
            }
        }
    }

    fun onEvent(event: LearningEvent) {
        when (event) {
            LearningEvent.Next -> _state.update { it.copy(deck = Deck.next(it.deck)) }
            LearningEvent.Previous -> _state.update { it.copy(deck = Deck.previous(it.deck)) }
            LearningEvent.Shuffle -> _state.update { it.copy(deck = Deck.shuffle(it.deck)) }
        }
    }

    /**
     * Builds one flashcard per favourite word. Definitions/phonetics come
     * from the offline Room cache; a word without cached data still gets a
     * card (front only) so the deck always mirrors the favourites list.
     */
    private suspend fun buildDeck(favourites: List<String>): DeckState {
        val cards = favourites.map { word ->
            val entries = runCatching { wordCacheUseCases.getCachedEntries(word) }
                .getOrDefault(emptyList())
            val entry = entries.firstOrNull()
            Flashcard(
                word = word,
                phonetic = entry?.phonetics?.firstNotNullOfOrNull { it.text },
                definitions = entry?.meanings
                    ?.flatMap { meaning ->
                        meaning.definitions.map { definition ->
                            val partOfSpeech = meaning.partOfSpeech
                            if (partOfSpeech.isBlank()) definition.definition
                            else "($partOfSpeech) ${definition.definition}"
                        }
                    }
                    ?.toImmutableList() ?: kotlinx.collections.immutable.persistentListOf()
            )
        }
        return Deck.create(cards)
    }
}
