/*
 *     freeDictionaryApp/freeDictionaryApp.feature_learning.main
 *     Flashcard.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     Flashcard.kt Last modified at 2026/1/1
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
import kotlinx.collections.immutable.persistentListOf

/**
 * A single learning flashcard built from a favourite word and its cached
 * lookup data. [definitions] is empty when the word was never looked up
 * (or the cache was pruned) — the card back then shows a lookup hint.
 */
data class Flashcard(
    val word: String,
    val phonetic: String? = null,
    val definitions: ImmutableList<String> = persistentListOf()
) {
    val hasCachedData: Boolean get() = definitions.isNotEmpty() || phonetic != null
}
