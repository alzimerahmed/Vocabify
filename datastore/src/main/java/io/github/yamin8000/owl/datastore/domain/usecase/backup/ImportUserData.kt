/*
 *     Vocabify/freeDictionaryApp.datastore.main
 *     ImportUserData.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     ImportUserData.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/freeDictionaryApp.datastore.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/freeDictionaryApp.datastore.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/freeDictionaryApp.datastore.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.datastore.domain.usecase.backup

import com.squareup.moshi.Moshi
import io.github.yamin8000.owl.datastore.domain.model.UserBackup
import io.github.yamin8000.owl.datastore.domain.repository.FavouriteRepository
import io.github.yamin8000.owl.datastore.domain.repository.HistoryRepository

/**
 * Imports a previously exported backup. Import merges into the
 * existing data (duplicates are ignored by the stores) so a bad or
 * partial file can never wipe the user's current data.
 *
 * Returns the number of imported entries (history + favourites),
 * or null when [json] is not a valid backup document.
 */
class ImportUserData(
    private val historyRepository: HistoryRepository,
    private val favouriteRepository: FavouriteRepository,
    private val moshi: Moshi
) {
    suspend operator fun invoke(json: String): Int? {
        val backup = try {
            moshi.adapter(UserBackup::class.java).fromJson(json)
        } catch (ignored: Exception) {
            null
        } ?: return null

        if (backup.version > UserBackup.CURRENT_VERSION) return null

        var imported = 0
        backup.history.filter { it.isNotBlank() }.forEach {
            historyRepository.add(it)
            imported++
        }
        backup.favourites.filter { it.isNotBlank() }.forEach {
            favouriteRepository.add(it)
            imported++
        }
        return imported
    }
}
