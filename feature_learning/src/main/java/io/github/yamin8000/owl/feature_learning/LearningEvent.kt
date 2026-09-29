/*
 *     freeDictionaryApp/freeDictionaryApp.feature_learning.main
 *     LearningEvent.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     LearningEvent.kt Last modified at 2026/1/1
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

package io.github.yamin8000.owl.feature_learning

sealed interface LearningEvent {
    data object Next : LearningEvent
    data object Previous : LearningEvent
    data object Shuffle : LearningEvent
}
