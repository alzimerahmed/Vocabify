/*
 *     Vocabify/Vocabify.feature.main
 *     OverlayWindowState.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     OverlayWindowState.kt Last modified at 2024/9/5
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

package io.github.yamin8000.owl.feature_overlay.ui

import io.github.yamin8000.owl.search.domain.model.Entry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class OverlayWindowState(
    val isSearching: Boolean = false,
    val searchTerm: String = "",
    val entries: ImmutableList<Entry> = persistentListOf(),
    val word: String = ""
)
