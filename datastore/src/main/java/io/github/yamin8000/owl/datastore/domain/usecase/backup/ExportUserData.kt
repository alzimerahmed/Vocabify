/*
 *     Vocabify/Vocabify.datastore.main
 *     ExportUserData.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     ExportUserData.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/Vocabify.datastore.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/Vocabify.datastore.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/Vocabify.datastore.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.datastore.domain.usecase.backup

import com.squareup.moshi.Moshi
import io.github.yamin8000.owl.datastore.domain.model.UserBackup
import io.github.yamin8000.owl.datastore.domain.repository.FavouriteRepository
import io.github.yamin8000.owl.datastore.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.first

/**
 * Builds the JSON backup document from the current search history
 * and favourites.
 */
class ExportUserData(
    private val historyRepository: HistoryRepository,
    private val favouriteRepository: FavouriteRepository,
    private val moshi: Moshi
) {
    suspend operator fun invoke(): String {
        val history = historyRepository.all().first()
        val favourites = favouriteRepository.all().first()
        val backup = UserBackup(
            history = history.filter { it.isNotBlank() }.distinct(),
            favourites = favourites.filter { it.isNotBlank() }.distinct()
        )
        val adapter = moshi.adapter(UserBackup::class.java)
        return adapter.toJson(backup)
    }
}
